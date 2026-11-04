package com.rewindd.app.data.repository

import com.rewindd.app.core.database.dao.TagDao
import com.rewindd.app.core.database.entity.SavedItemTagEntity
import com.rewindd.app.core.database.entity.TagEntity
import com.rewindd.app.domain.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import java.util.UUID

interface TagRepository {
    fun getAllTags(): Flow<List<Tag>>
    suspend fun createOrFindTag(displayName: String, colorToken: String? = null): Tag
    suspend fun assignTagToItem(savedItemId: String, tagId: String)
    suspend fun removeTagFromItem(savedItemId: String, tagId: String)
    suspend fun setTagsForItem(savedItemId: String, tagIds: List<String>)
    suspend fun deleteTag(id: String)
    suspend fun deleteAll()
}

class TagRepositoryImpl(
    private val dao: TagDao
) : TagRepository {

    override fun getAllTags(): Flow<List<Tag>> {
        return dao.getAllTagsFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun createOrFindTag(displayName: String, colorToken: String?): Tag {
        val trimmed = displayName.trim().removePrefix("#")
        val normalized = trimmed.lowercase(Locale.ROOT)
        val existing = dao.findByNormalizedName(normalized)
        if (existing != null) {
            return existing.toDomain()
        }

        val newTag = TagEntity(
            id = UUID.randomUUID().toString(),
            normalizedName = normalized,
            displayName = "#$trimmed",
            colorToken = colorToken
        )
        dao.insert(newTag)
        return newTag.toDomain()
    }

    override suspend fun assignTagToItem(savedItemId: String, tagId: String) {
        dao.insertItemTag(SavedItemTagEntity(savedItemId = savedItemId, tagId = tagId))
    }

    override suspend fun removeTagFromItem(savedItemId: String, tagId: String) {
        dao.removeItemTag(savedItemId = savedItemId, tagId = tagId)
    }

    override suspend fun setTagsForItem(savedItemId: String, tagIds: List<String>) {
        dao.clearTagsForItem(savedItemId)
        tagIds.forEach { tagId ->
            dao.insertItemTag(SavedItemTagEntity(savedItemId = savedItemId, tagId = tagId))
        }
    }

    override suspend fun deleteTag(id: String) {
        dao.deleteTag(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAllItemTags()
        dao.deleteAllTags()
    }
}
