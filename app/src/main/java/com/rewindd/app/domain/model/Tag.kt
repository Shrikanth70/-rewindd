package com.rewindd.app.domain.model

data class Tag(
    val id: String,
    val normalizedName: String,
    val displayName: String,
    val colorToken: String? = null
)
