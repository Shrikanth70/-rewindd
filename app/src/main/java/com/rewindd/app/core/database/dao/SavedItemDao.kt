package com.rewindd.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rewindd.app.core.database.entity.SavedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {

    @Query("SELECT * FROM saved_items ORDER BY capturedAtUtc DESC")
    fun getAllSavedItemsFlow(): Flow<List<SavedItemEntity>>

    @Query("SELECT * FROM saved_items WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): SavedItemEntity?

    @Query("SELECT * FROM saved_items WHERE normalizedUrl = :normalizedUrl LIMIT 1")
    suspend fun findByNormalizedUrl(normalizedUrl: String): SavedItemEntity?

    @Query("SELECT * FROM saved_items WHERE reviewState = 'UNREVIEWED' ORDER BY capturedAtUtc ASC")
    fun getUnreviewedItemsFlow(): Flow<List<SavedItemEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: SavedItemEntity)

    @Update
    suspend fun update(item: SavedItemEntity)

    @Query("DELETE FROM saved_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM saved_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM saved_items WHERE reviewState = 'UNREVIEWED'")
    fun getUnreviewedCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM saved_items")
    suspend fun getTotalCount(): Int

    @androidx.room.Transaction
    @Query("SELECT * FROM saved_items ORDER BY capturedAtUtc DESC")
    fun getAllWithDetailsFlow(): Flow<List<com.rewindd.app.core.database.model.SavedItemWithDetails>>

    @androidx.room.Transaction
    @Query("SELECT * FROM saved_items WHERE id = :id LIMIT 1")
    suspend fun getWithDetailsById(id: String): com.rewindd.app.core.database.model.SavedItemWithDetails?

    @androidx.room.Transaction
    @Query("SELECT * FROM saved_items WHERE collectionId = :collectionId ORDER BY capturedAtUtc DESC")
    fun getItemsByCollectionFlow(collectionId: String): Flow<List<com.rewindd.app.core.database.model.SavedItemWithDetails>>

    @Query("UPDATE saved_items SET title = :title, note = :note, collectionId = :collectionId, isPinned = :isPinned, updatedAtUtc = :updatedAtUtc WHERE id = :id")
    suspend fun updateDetails(id: String, title: String, note: String?, collectionId: String?, isPinned: Boolean, updatedAtUtc: Long)

    @Query("UPDATE saved_items SET isPinned = :isPinned, updatedAtUtc = :updatedAtUtc WHERE id = :id")
    suspend fun updatePinned(id: String, isPinned: Boolean, updatedAtUtc: Long)

    @Query("UPDATE saved_items SET collectionId = :collectionId, updatedAtUtc = :updatedAtUtc WHERE id = :id")
    suspend fun updateCollection(id: String, collectionId: String?, updatedAtUtc: Long)

    @Query("SELECT * FROM saved_items WHERE (description IS NULL OR description = '') ORDER BY capturedAtUtc DESC LIMIT 30")
    suspend fun getItemsNeedingBackfill(): List<SavedItemEntity>
}
