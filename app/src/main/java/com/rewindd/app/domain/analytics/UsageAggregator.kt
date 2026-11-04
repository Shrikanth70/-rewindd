package com.rewindd.app.domain.analytics

import com.rewindd.app.domain.model.DailyAppUsage
import com.rewindd.app.domain.model.UsageSourceStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class RawUsageEvent(
    val packageName: String,
    val timestamp: Long,
    val eventType: Int
) {
    companion object {
        const val TYPE_FOREGROUND = 1
        const val TYPE_BACKGROUND = 2
        const val TYPE_SCREEN_OFF = 3
    }
}

object UsageAggregator {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun aggregateEvents(
        events: List<RawUsageEvent>,
        trackedApps: Set<String>,
        zoneId: ZoneId = ZoneId.systemDefault(),
        nowUtc: Long = System.currentTimeMillis()
    ): List<DailyAppUsage> {
        val sortedEvents = events.sortedBy { it.timestamp }

        // Track active foreground session
        var currentForegroundPkg: String? = null
        var currentForegroundStart: Long? = null

        // Spans to attribute: (dateLocal, packageName) -> MutableSpanData
        val dailyMap = mutableMapOf<Pair<String, String>, MutableSpanData>()

        fun closeCurrentForeground(endTime: Long, isOngoing: Boolean) {
            val pkg = currentForegroundPkg ?: return
            val start = currentForegroundStart ?: return
            if (pkg in trackedApps && endTime > start) {
                splitAndAttributeSpan(
                    packageName = pkg,
                    startTime = start,
                    endTime = endTime,
                    zoneId = zoneId,
                    isOngoing = isOngoing,
                    dailyMap = dailyMap
                )
            }
            currentForegroundPkg = null
            currentForegroundStart = null
        }

        for (event in sortedEvents) {
            when (event.eventType) {
                RawUsageEvent.TYPE_FOREGROUND -> {
                    if (event.packageName == currentForegroundPkg) {
                        // Same app internal transition/re-resumed; preserve start time
                        continue
                    }
                    // Different app moved to foreground; close previous app session at this timestamp
                    closeCurrentForeground(endTime = event.timestamp, isOngoing = false)

                    if (event.packageName in trackedApps) {
                        currentForegroundPkg = event.packageName
                        currentForegroundStart = event.timestamp
                    }
                }
                RawUsageEvent.TYPE_BACKGROUND -> {
                    if (event.packageName == currentForegroundPkg) {
                        closeCurrentForeground(endTime = event.timestamp, isOngoing = false)
                    }
                }
                RawUsageEvent.TYPE_SCREEN_OFF -> {
                    // Screen turned off or device locked; close active foreground session
                    closeCurrentForeground(endTime = event.timestamp, isOngoing = false)
                }
            }
        }

        // Handle still-active session clamped at nowUtc
        if (currentForegroundPkg != null && currentForegroundStart != null) {
            val clampedEnd = maxOf(currentForegroundStart!!, nowUtc)
            closeCurrentForeground(endTime = clampedEnd, isOngoing = true)
        }

        return dailyMap.map { (key, data) ->
            DailyAppUsage(
                dateLocal = key.first,
                packageName = key.second,
                usageMillis = data.durationMillis,
                sessionCount = data.sessionCount,
                pickupCount = maxOf(1, data.sessionCount),
                sourceStatus = if (data.isOngoing) UsageSourceStatus.PARTIAL else UsageSourceStatus.RELIABLE,
                lastCalculatedAtUtc = nowUtc
            )
        }.sortedWith(compareBy({ it.dateLocal }, { -it.usageMillis }))
    }

    private fun splitAndAttributeSpan(
        packageName: String,
        startTime: Long,
        endTime: Long,
        zoneId: ZoneId,
        isOngoing: Boolean,
        dailyMap: MutableMap<Pair<String, String>, MutableSpanData>
    ) {
        if (endTime <= startTime) return

        var currentStart = startTime
        var isInitialDay = true

        while (currentStart < endTime) {
            val startLocal = Instant.ofEpochMilli(currentStart).atZone(zoneId)
            val currentDate = startLocal.toLocalDate()
            val dateKey = currentDate.format(dateFormatter)

            val nextMidnight = currentDate.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
            val currentEnd = minOf(endTime, nextMidnight)
            val sliceDuration = currentEnd - currentStart

            if (sliceDuration > 0) {
                val span = dailyMap.getOrPut(Pair(dateKey, packageName)) { MutableSpanData() }
                span.durationMillis += sliceDuration
                if (isInitialDay) {
                    span.sessionCount += 1
                }
                if (isOngoing) {
                    span.isOngoing = true
                }
            }

            isInitialDay = false
            currentStart = currentEnd
        }
    }

    private class MutableSpanData(
        var durationMillis: Long = 0L,
        var sessionCount: Int = 0,
        var isOngoing: Boolean = false
    )
}
