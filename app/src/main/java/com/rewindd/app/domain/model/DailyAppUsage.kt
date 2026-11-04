package com.rewindd.app.domain.model

enum class UsageSourceStatus {
    RELIABLE,
    PARTIAL,
    ESTIMATE
}

data class DailyAppUsage(
    val dateLocal: String,
    val packageName: String,
    val usageMillis: Long,
    val sessionCount: Int? = null,
    val pickupCount: Int? = null,
    val sourceStatus: UsageSourceStatus = UsageSourceStatus.RELIABLE,
    val lastCalculatedAtUtc: Long = System.currentTimeMillis()
)
