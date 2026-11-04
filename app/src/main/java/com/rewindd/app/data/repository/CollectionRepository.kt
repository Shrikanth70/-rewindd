package com.rewindd.app.data.repository

import com.rewindd.app.core.database.dao.CollectionDao
import com.rewindd.app.core.database.entity.CollectionEntity
import com.rewindd.app.domain.model.Collection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

interface CollectionRepository {
    fun getAllCollections(): Flow<List<Collection>>
    suspend fun getById(id: String): Collection?
    suspend fun createCollection(name: String, colorToken: String, isPinned: Boolean = false, sortOrder: Int = 0, iconToken: String = "📁"): Collection
    suspend fun updateCollection(collection: Collection)
    suspend fun deleteCollection(id: String)
    suspend fun deleteAll()
}

class CollectionRepositoryImpl(
    private val dao: CollectionDao
) : CollectionRepository {

    override fun getAllCollections(): Flow<List<Collection>> {
        return dao.getAllCollectionsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: String): Collection? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun createCollection(
        name: String,
        colorToken: String,
        isPinned: Boolean,
        sortOrder: Int,
        iconToken: String
    ): Collection {
        val now = System.currentTimeMillis()
        val entity = CollectionEntity(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            colorToken = colorToken,
            createdAtUtc = now,
            updatedAtUtc = now,
            isPinned = isPinned,
            sortOrder = sortOrder,
            iconToken = iconToken
        )
        dao.insert(entity)
        return entity.toDomain()
    }

    override suspend fun updateCollection(collection: Collection) {
        val updated = CollectionEntity(
            id = collection.id,
            name = collection.name.trim(),
            colorToken = collection.colorToken,
            createdAtUtc = collection.createdAtUtc,
            updatedAtUtc = System.currentTimeMillis(),
            isPinned = collection.isPinned,
            sortOrder = collection.sortOrder,
            iconToken = collection.iconToken
        )
        dao.update(updated)
    }

    override suspend fun deleteCollection(id: String) {
        dao.unassignCollectionFromItems(id)
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
