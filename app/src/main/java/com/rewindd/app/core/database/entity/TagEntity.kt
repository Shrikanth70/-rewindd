package com.rewindd.app.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rewindd.app.domain.model.Tag

@Entity(
    tableName = "tags",
    indices = [Index(value = ["normalizedName"], unique = true)]
)
data class TagEntity(
    @PrimaryKey
    val id: String,
    val normalizedName: String,
    val displayName: String,
    val colorToken: String? = null
) {
    fun toDomain(): Tag = Tag(
        id = id,
        normalizedName = normalizedName,
        displayName = displayName,
        colorToken = colorToken
    )

    companion object {
        fun fromDomain(domain: Tag): TagEntity = TagEntity(
            id = domain.id,
            normalizedName = domain.normalizedName,
            displayName = domain.displayName,
            colorToken = domain.colorToken
        )
    }
}
