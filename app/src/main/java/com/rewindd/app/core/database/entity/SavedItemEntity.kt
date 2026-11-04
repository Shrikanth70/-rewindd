package com.rewindd.app.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rewindd.app.domain.model.MetadataStatus
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem

@Entity(
    tableName = "saved_items",
    indices = [
        Index(value = ["normalizedUrl"], unique = true),
        Index(value = ["capturedAtUtc"]),
        Index(value = ["reviewState"]),
        Index(value = ["collectionId"])
    ]
)
data class SavedItemEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val normalizedUrl: String,
    val sourcePackage: String?,
    val sourceLabel: String?,
    val title: String,
    val thumbnailUri: String?,
    val capturedAtUtc: Long,
    val updatedAtUtc: Long,
    val reviewState: String, // UNREVIEWED, REVIEWED, ARCHIVED
    val isPinned: Boolean,
    val collectionId: String?,
    val note: String?,
    val metadataStatus: String, // PENDING, SUCCESS, FAILED, OFFLINE
    val archivedAtUtc: Long?,
    val contentHash: String?
) {
    fun toDomain(): SavedItem = SavedItem(
        id = id,
        url = url,
        normalizedUrl = normalizedUrl,
        sourcePackage = sourcePackage,
        sourceLabel = sourceLabel,
        title = com.rewindd.app.domain.model.cleanHtmlEntities(title),
        thumbnailUri = thumbnailUri,
        capturedAtUtc = capturedAtUtc,
        updatedAtUtc = updatedAtUtc,
        reviewState = try { ReviewState.valueOf(reviewState) } catch (e: Exception) { ReviewState.UNREVIEWED },
        isPinned = isPinned,
        collectionId = collectionId,
        note = note,
        metadataStatus = try { MetadataStatus.valueOf(metadataStatus) } catch (e: Exception) { MetadataStatus.PENDING },
        archivedAtUtc = archivedAtUtc,
        contentHash = contentHash
    )

    companion object {
        fun fromDomain(domain: SavedItem): SavedItemEntity = SavedItemEntity(
            id = domain.id,
            url = domain.url,
            normalizedUrl = domain.normalizedUrl,
            sourcePackage = domain.sourcePackage,
            sourceLabel = domain.sourceLabel,
            title = domain.title,
            thumbnailUri = domain.thumbnailUri,
            capturedAtUtc = domain.capturedAtUtc,
            updatedAtUtc = domain.updatedAtUtc,
            reviewState = domain.reviewState.name,
            isPinned = domain.isPinned,
            collectionId = domain.collectionId,
            note = domain.note,
            metadataStatus = domain.metadataStatus.name,
            archivedAtUtc = domain.archivedAtUtc,
            contentHash = domain.contentHash
        )
    }
}
