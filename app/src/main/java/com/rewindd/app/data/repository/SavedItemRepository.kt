package com.rewindd.app.data.repository

import com.rewindd.app.core.database.dao.SavedItemDao
import com.rewindd.app.core.database.entity.SavedItemEntity
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.domain.model.MetadataStatus
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.usecase.InstagramMetadataExtractor
import com.rewindd.app.domain.usecase.UrlMetadataExtractor
import com.rewindd.app.domain.usecase.UrlNormalizer
import com.rewindd.app.domain.usecase.UrlValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

sealed class SaveResult {
    data class Success(val item: SavedItem) : SaveResult()
    data class Duplicate(val existingItem: SavedItem) : SaveResult()
    data class Invalid(val reason: String) : SaveResult()
    data class Failure(val error: String) : SaveResult()
}

interface SavedItemRepository {
    fun getAllItems(): Flow<List<SavedItem>>
    fun getAllWithDetails(): Flow<List<SavedItemDetailsDomain>>
    fun getItemsByCollection(collectionId: String): Flow<List<SavedItemDetailsDomain>>
    fun getUnreviewedItems(): Flow<List<SavedItem>>
    fun getUnreviewedCount(): Flow<Int>
    suspend fun saveUrl(rawInput: String, sourcePackage: String? = null, sourceLabel: String? = null): SaveResult
    suspend fun getById(id: String): SavedItem?
    suspend fun getWithDetailsById(id: String): SavedItemDetailsDomain?
    suspend fun deleteById(id: String)
    suspend fun deleteAll()
    suspend fun updateReviewState(id: String, state: ReviewState)
    suspend fun keepItem(id: String)
    suspend fun archiveItem(id: String)
    suspend fun undoArchive(id: String)
    suspend fun updateDetails(id: String, title: String, note: String?, collectionId: String?, isPinned: Boolean)
    suspend fun assignItemToCollection(itemId: String, collectionId: String?)
    suspend fun removeItemFromCollection(itemId: String)
    suspend fun togglePin(id: String)
    suspend fun ensurePrototypeSeedData()
}

class SavedItemRepositoryImpl(
    private val dao: SavedItemDao,
    private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) : SavedItemRepository {

    override fun getAllItems(): Flow<List<SavedItem>> {
        return dao.getAllSavedItemsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllWithDetails(): Flow<List<SavedItemDetailsDomain>> {
        return dao.getAllWithDetailsFlow().map { relations ->
            relations.map { it.toDomain() }
        }
    }

    override fun getItemsByCollection(collectionId: String): Flow<List<SavedItemDetailsDomain>> {
        return dao.getItemsByCollectionFlow(collectionId).map { relations ->
            relations.map { it.toDomain() }
        }
    }

    override fun getUnreviewedItems(): Flow<List<SavedItem>> {
        return dao.getUnreviewedItemsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUnreviewedCount(): Flow<Int> {
        return dao.getUnreviewedCountFlow()
    }

    override suspend fun saveUrl(
        rawInput: String,
        sourcePackage: String?,
        sourceLabel: String?
    ): SaveResult {
        return when (val validation = UrlNormalizer.extractAndNormalize(rawInput)) {
            is UrlValidationResult.Invalid -> {
                SaveResult.Invalid(validation.reason)
            }
            is UrlValidationResult.Valid -> {
                try {
                    // Check duplicate
                    val existing = dao.findByNormalizedUrl(validation.normalizedUrl)
                    if (existing != null) {
                        return SaveResult.Duplicate(existing.toDomain())
                    }

                    val now = System.currentTimeMillis()
                    val youtubeThumb = UrlMetadataExtractor.extractYouTubeThumbnail(validation.originalUrl)
                    val initialMetadataStatus = if (youtubeThumb != null) MetadataStatus.SUCCESS else MetadataStatus.PENDING

                    val newItem = SavedItem(
                        id = UUID.randomUUID().toString(),
                        url = validation.originalUrl,
                        normalizedUrl = validation.normalizedUrl,
                        sourcePackage = sourcePackage,
                        sourceLabel = sourceLabel ?: validation.domain,
                        title = validation.domain, // Fallback title until metadata fetch
                        thumbnailUri = youtubeThumb,
                        capturedAtUtc = now,
                        updatedAtUtc = now,
                        reviewState = ReviewState.UNREVIEWED,
                        isPinned = false,
                        collectionId = null,
                        note = null,
                        metadataStatus = initialMetadataStatus,
                        archivedAtUtc = null,
                        contentHash = null
                    )

                    dao.insert(SavedItemEntity.fromDomain(newItem))

                    // Always trigger rich background enrichment (title, description, author, site, cover)
                    enrichMetadataAsync(newItem.id, newItem.url)

                    SaveResult.Success(newItem)
                } catch (e: Exception) {
                    SaveResult.Failure(e.localizedMessage ?: "Unknown storage error")
                }
            }
        }
    }

    init {
        // Lazy background backfill for previously saved links with missing metadata
        repositoryScope.launch(Dispatchers.IO) {
            delay(2500) // Polite delay after startup
            try {
                val backfillItems = dao.getItemsNeedingBackfill()
                for (item in backfillItems) {
                    try {
                        enrichItemMetadata(item.id, item.url)
                        delay(350) // Non-blocking pacing
                    } catch (_: Exception) {}
                }
            } catch (_: Exception) {}
        }
    }

    private fun enrichMetadataAsync(itemId: String, url: String) {
        repositoryScope.launch(Dispatchers.IO) {
            enrichItemMetadata(itemId, url)
        }
    }

    private suspend fun enrichItemMetadata(itemId: String, url: String) {
        try {
            if (InstagramMetadataExtractor.isInstagramUrl(url)) {
                val meta = InstagramMetadataExtractor.extract(url)
                val existing = dao.getById(itemId) ?: return
                val now = System.currentTimeMillis()
                if (meta?.thumbnailUrl != null) {
                    dao.update(
                        existing.copy(
                            thumbnailUri = meta.thumbnailUrl,
                            title = meta.title ?: existing.title,
                            description = meta.rawCaption ?: existing.description,
                            siteName = "Instagram",
                            metadataStatus = MetadataStatus.SUCCESS.name,
                            updatedAtUtc = now
                        )
                    )
                } else {
                    dao.update(
                        existing.copy(
                            metadataStatus = MetadataStatus.FAILED.name,
                            updatedAtUtc = now
                        )
                    )
                }
                return
            }

            val meta = com.rewindd.app.linkpreview.extra.RichLinkMetadataExtractor.extract(url)
            val existing = dao.getById(itemId) ?: return
            val now = System.currentTimeMillis()

            val newTitle = meta.title?.takeIf { it.isNotBlank() } ?: existing.title
            val newThumb = meta.thumbnailUri ?: existing.thumbnailUri
            val newDesc = meta.description?.takeIf { it.isNotBlank() } ?: existing.description
            val newSite = meta.siteName?.takeIf { it.isNotBlank() } ?: existing.siteName
            val newAuthor = meta.authorName?.takeIf { it.isNotBlank() } ?: existing.authorName

            val hasContent = meta.title != null || meta.thumbnailUri != null || meta.description != null

            dao.update(
                existing.copy(
                    title = newTitle,
                    thumbnailUri = newThumb,
                    description = newDesc,
                    siteName = newSite,
                    authorName = newAuthor,
                    metadataStatus = if (hasContent) MetadataStatus.SUCCESS.name else existing.metadataStatus,
                    updatedAtUtc = now
                )
            )
        } catch (e: Exception) {
            try {
                val existing = dao.getById(itemId) ?: return
                dao.update(
                    existing.copy(
                        metadataStatus = MetadataStatus.FAILED.name,
                        updatedAtUtc = System.currentTimeMillis()
                    )
                )
            } catch (_: Exception) {}
        }
    }

    override suspend fun getById(id: String): SavedItem? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getWithDetailsById(id: String): SavedItemDetailsDomain? {
        return dao.getWithDetailsById(id)?.toDomain()
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun updateReviewState(id: String, state: ReviewState) {
        val existing = dao.getById(id) ?: return
        val now = System.currentTimeMillis()
        val archivedAt = if (state == ReviewState.ARCHIVED) now else null
        dao.update(
            existing.copy(
                reviewState = state.name,
                updatedAtUtc = now,
                archivedAtUtc = archivedAt
            )
        )
    }

    override suspend fun keepItem(id: String) {
        updateReviewState(id, ReviewState.REVIEWED)
    }

    override suspend fun archiveItem(id: String) {
        updateReviewState(id, ReviewState.ARCHIVED)
    }

    override suspend fun undoArchive(id: String) {
        updateReviewState(id, ReviewState.UNREVIEWED)
    }

    override suspend fun updateDetails(
        id: String,
        title: String,
        note: String?,
        collectionId: String?,
        isPinned: Boolean
    ) {
        dao.updateDetails(
            id = id,
            title = title.trim(),
            note = note?.trim()?.ifBlank { null },
            collectionId = collectionId,
            isPinned = isPinned,
            updatedAtUtc = System.currentTimeMillis()
        )
    }

    override suspend fun togglePin(id: String) {
        val existing = dao.getById(id) ?: return
        val newPinned = !existing.isPinned
        dao.updatePinned(id, newPinned, System.currentTimeMillis())
    }

    override suspend fun assignItemToCollection(itemId: String, collectionId: String?) {
        dao.updateCollection(itemId, collectionId, System.currentTimeMillis())
    }

    override suspend fun removeItemFromCollection(itemId: String) {
        dao.updateCollection(itemId, null, System.currentTimeMillis())
    }

    override suspend fun ensurePrototypeSeedData() {
        // Vault begins completely clean for new users
    }
}
