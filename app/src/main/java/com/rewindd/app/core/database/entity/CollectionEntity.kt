package com.rewindd.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rewindd.app.domain.model.Collection

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val colorToken: String,
    val createdAtUtc: Long,
    val updatedAtUtc: Long,
    val isPinned: Boolean = false,
    val sortOrder: Int = 0,
    val iconToken: String = "📁"
) {
    fun toDomain(): Collection = Collection(
        id = id,
        name = name,
        colorToken = colorToken,
        createdAtUtc = createdAtUtc,
        updatedAtUtc = updatedAtUtc,
        isPinned = isPinned,
        sortOrder = sortOrder,
        iconToken = iconToken
    )

    companion object {
        fun fromDomain(domain: Collection): CollectionEntity = CollectionEntity(
            id = domain.id,
            name = domain.name,
            colorToken = domain.colorToken,
            createdAtUtc = domain.createdAtUtc,
            updatedAtUtc = domain.updatedAtUtc,
            isPinned = domain.isPinned,
            sortOrder = domain.sortOrder,
            iconToken = domain.iconToken
        )
    }
}
