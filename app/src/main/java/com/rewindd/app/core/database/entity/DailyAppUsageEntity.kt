package com.rewindd.app.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import com.rewindd.app.domain.model.DailyAppUsage
import com.rewindd.app.domain.model.UsageSourceStatus

@Entity(
    tableName = "daily_app_usage",
    primaryKeys = ["dateLocal", "packageName"],
    indices = [
        Index(value = ["dateLocal"]),
        Index(value = ["packageName"])
    ]
)
data class DailyAppUsageEntity(
    val dateLocal: String,
    val packageName: String,
    val usageMillis: Long,
    val sessionCount: Int?,
    val pickupCount: Int?,
    val sourceStatus: String,
    val lastCalculatedAtUtc: Long
) {
    fun toDomain(): DailyAppUsage = DailyAppUsage(
        dateLocal = dateLocal,
        packageName = packageName,
        usageMillis = usageMillis,
        sessionCount = sessionCount,
        pickupCount = pickupCount,
        sourceStatus = runCatching { UsageSourceStatus.valueOf(sourceStatus) }.getOrDefault(UsageSourceStatus.RELIABLE),
        lastCalculatedAtUtc = lastCalculatedAtUtc
    )

    companion object {
        fun fromDomain(domain: DailyAppUsage): DailyAppUsageEntity = DailyAppUsageEntity(
            dateLocal = domain.dateLocal,
            packageName = domain.packageName,
            usageMillis = domain.usageMillis,
            sessionCount = domain.sessionCount,
            pickupCount = domain.pickupCount,
            sourceStatus = domain.sourceStatus.name,
            lastCalculatedAtUtc = domain.lastCalculatedAtUtc
        )
    }
}
