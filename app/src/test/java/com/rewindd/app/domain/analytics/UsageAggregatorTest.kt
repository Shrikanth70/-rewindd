package com.rewindd.app.domain.analytics

import com.rewindd.app.domain.model.UsageSourceStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class UsageAggregatorTest {

    private val zoneId = ZoneId.of("UTC")

    @Test
    fun testEventPairingCalculatesAccurateDwell() {
        val date = LocalDate.of(2026, 10, 14)
        val dayStartUtc = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

        // Instagram open at 10:00 (10h into day), close at 10:30 (30 mins = 1,800,000ms)
        val event1 = RawUsageEvent("com.instagram.android", dayStartUtc + 36000000L, RawUsageEvent.TYPE_FOREGROUND)
        val event2 = RawUsageEvent("com.instagram.android", dayStartUtc + 37800000L, RawUsageEvent.TYPE_BACKGROUND)

        // Untracked app open at 11:00
        val event3 = RawUsageEvent("com.untracked.app", dayStartUtc + 39600000L, RawUsageEvent.TYPE_FOREGROUND)
        val event4 = RawUsageEvent("com.untracked.app", dayStartUtc + 41400000L, RawUsageEvent.TYPE_BACKGROUND)

        val trackedApps = setOf("com.instagram.android")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1, event2, event3, event4),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = dayStartUtc + 50000000L
        )

        assertEquals(1, results.size)
        val insta = results.first()
        assertEquals("2026-10-14", insta.dateLocal)
        assertEquals("com.instagram.android", insta.packageName)
        assertEquals(1800000L, insta.usageMillis)
        assertEquals(1, insta.sessionCount)
    }

    @Test
    fun testMidnightBoundarySplitting() {
        // Oct 14: 23:45 (15 mins before midnight)
        // Oct 15: 00:20 (20 mins after midnight)
        val oct14 = LocalDate.of(2026, 10, 14)
        val oct14Midnight = oct14.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

        val startTime = oct14Midnight - (15 * 60 * 1000L) // 23:45
        val endTime = oct14Midnight + (20 * 60 * 1000L)   // 00:20

        val event1 = RawUsageEvent("com.google.android.youtube", startTime, RawUsageEvent.TYPE_FOREGROUND)
        val event2 = RawUsageEvent("com.google.android.youtube", endTime, RawUsageEvent.TYPE_BACKGROUND)

        val trackedApps = setOf("com.google.android.youtube")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1, event2),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = endTime + 1000L
        )

        assertEquals(2, results.size)
        val day1 = results.first { it.dateLocal == "2026-10-14" }
        val day2 = results.first { it.dateLocal == "2026-10-15" }

        // Day 1 should have exactly 15 minutes (900,000ms)
        assertEquals(15 * 60 * 1000L, day1.usageMillis)
        assertEquals(1, day1.sessionCount)

        // Day 2 should have exactly 20 minutes (1,200,000ms)
        assertEquals(20 * 60 * 1000L, day2.usageMillis)
    }

    @Test
    fun testActiveSessionClamping() {
        val date = LocalDate.of(2026, 10, 14)
        val dayStartUtc = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

        // App opened at 14:00, user checks Rewindd at 14:25 (ongoing active session = 25m)
        val event1 = RawUsageEvent("com.android.chrome", dayStartUtc + (14 * 3600000L), RawUsageEvent.TYPE_FOREGROUND)
        val now = dayStartUtc + (14 * 3600000L) + (25 * 60000L)

        val trackedApps = setOf("com.android.chrome")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = now
        )

        assertEquals(1, results.size)
        val chrome = results.first()
        assertEquals(25 * 60000L, chrome.usageMillis)
        assertEquals(1, chrome.sessionCount)
        assertEquals(UsageSourceStatus.PARTIAL, chrome.sourceStatus)
    }

    @Test
    fun testAppSwitchingTerminatesPreviousAppSession() {
        val date = LocalDate.of(2026, 10, 14)
        val dayStartUtc = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

        // Instagram opened at 10:00 (10h into day)
        val instaStart = dayStartUtc + (10 * 3600000L)
        val event1 = RawUsageEvent("com.instagram.android", instaStart, RawUsageEvent.TYPE_FOREGROUND)

        // User switches directly to YouTube at 10:15 without Instagram background event
        val ytStart = instaStart + (15 * 60000L)
        val event2 = RawUsageEvent("com.google.android.youtube", ytStart, RawUsageEvent.TYPE_FOREGROUND)

        // YouTube closed at 10:35 (20m later)
        val ytEnd = ytStart + (20 * 60000L)
        val event3 = RawUsageEvent("com.google.android.youtube", ytEnd, RawUsageEvent.TYPE_BACKGROUND)

        val trackedApps = setOf("com.instagram.android", "com.google.android.youtube")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1, event2, event3),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = ytEnd + 60000L
        )

        assertEquals(2, results.size)
        val insta = results.first { it.packageName == "com.instagram.android" }
        val youtube = results.first { it.packageName == "com.google.android.youtube" }

        // Instagram must be exactly 15m, not lingering until nowUtc
        assertEquals(15 * 60000L, insta.usageMillis)
        assertEquals(1, insta.sessionCount)

        // YouTube must be exactly 20m
        assertEquals(20 * 60000L, youtube.usageMillis)
        assertEquals(1, youtube.sessionCount)
    }

    @Test
    fun testUntrackedAppSwitchingTerminatesTrackedAppSession() {
        val date = LocalDate.of(2026, 10, 14)
        val dayStartUtc = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

        // Instagram opened at 10:00
        val instaStart = dayStartUtc + (10 * 3600000L)
        val event1 = RawUsageEvent("com.instagram.android", instaStart, RawUsageEvent.TYPE_FOREGROUND)

        // Untracked Launcher/Home opened at 10:10
        val launcherStart = instaStart + (10 * 60000L)
        val event2 = RawUsageEvent("com.google.android.apps.nexuslauncher", launcherStart, RawUsageEvent.TYPE_FOREGROUND)

        // Query now at 12:00 (2 hours later)
        val now = dayStartUtc + (12 * 3600000L)

        val trackedApps = setOf("com.instagram.android")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1, event2),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = now
        )

        assertEquals(1, results.size)
        val insta = results.first()
        // Must be capped at 10m when launcher took foreground, not 2 hours
        assertEquals(10 * 60000L, insta.usageMillis)
        assertEquals(1, insta.sessionCount)
    }

    @Test
    fun testScreenOffTerminatesActiveAppSession() {
        val date = LocalDate.of(2026, 10, 14)
        val dayStartUtc = date.atStartOfDay(zoneId).toInstant().toEpochMilli()

        // Instagram opened at 22:00
        val instaStart = dayStartUtc + (22 * 3600000L)
        val event1 = RawUsageEvent("com.instagram.android", instaStart, RawUsageEvent.TYPE_FOREGROUND)

        // User locks screen / screen turns non-interactive at 22:12
        val screenOffTime = instaStart + (12 * 60000L)
        val event2 = RawUsageEvent("android", screenOffTime, RawUsageEvent.TYPE_SCREEN_OFF)

        // Query next morning at 08:00 (10 hours later)
        val nextMorning = dayStartUtc + (32 * 3600000L)

        val trackedApps = setOf("com.instagram.android")
        val results = UsageAggregator.aggregateEvents(
            events = listOf(event1, event2),
            trackedApps = trackedApps,
            zoneId = zoneId,
            nowUtc = nextMorning
        )

        assertEquals(1, results.size)
        val insta = results.first()
        // Must be 12 minutes, NOT 10 hours
        assertEquals(12 * 60000L, insta.usageMillis)
        assertEquals(1, insta.sessionCount)
    }
}
