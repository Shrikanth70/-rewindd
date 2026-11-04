package com.rewindd.app.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.rewindd.app.core.database.entity.CollectionEntity
import com.rewindd.app.core.database.entity.SavedItemEntity
import com.rewindd.app.core.database.entity.SavedItemTagEntity
import com.rewindd.app.core.database.entity.TagEntity
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.model.Tag

data class SavedItemWithDetails(
    @Embedded
    val item: SavedItemEntity,

    @Relation(
        parentColumn = "collectionId",
        entityColumn = "id"
    )
    val collection: CollectionEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SavedItemTagEntity::class,
            parentColumn = "savedItemId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
) {
    fun toDomain(): SavedItemDetailsDomain = SavedItemDetailsDomain(
        item = item.toDomain(),
        collection = collection?.toDomain(),
        tags = tags.map { it.toDomain() }
    )
}

data class SavedItemDetailsDomain(
    val item: SavedItem,
    val collection: Collection?,
    val tags: List<Tag>
)
