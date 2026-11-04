package com.rewindd.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rewindd.app.core.database.entity.SavedItemTagEntity
import com.rewindd.app.core.database.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tags ORDER BY normalizedName ASC")
    fun getAllTagsFlow(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE normalizedName = :normalizedName LIMIT 1")
    suspend fun findByNormalizedName(normalizedName: String): TagEntity?

    @Query("SELECT * FROM tags WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemTag(crossRef: SavedItemTagEntity)

    @Query("DELETE FROM saved_item_tags WHERE savedItemId = :savedItemId AND tagId = :tagId")
    suspend fun removeItemTag(savedItemId: String, tagId: String)

    @Query("DELETE FROM saved_item_tags WHERE savedItemId = :savedItemId")
    suspend fun clearTagsForItem(savedItemId: String)

    @Query("DELETE FROM saved_item_tags")
    suspend fun deleteAllItemTags()

    @Query("DELETE FROM tags WHERE id = :tagId")
    suspend fun deleteTag(tagId: String)

    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()
}
