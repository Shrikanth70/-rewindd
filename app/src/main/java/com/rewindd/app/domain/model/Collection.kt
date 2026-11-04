package com.rewindd.app.domain.model

data class Collection(
    val id: String,
    val name: String,
    val colorToken: String,
    val createdAtUtc: Long,
    val updatedAtUtc: Long,
    val isPinned: Boolean = false,
    val sortOrder: Int = 0,
    val iconToken: String = "📁"
)
