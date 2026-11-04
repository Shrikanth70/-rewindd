package com.rewindd.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.core.database.entity.DailyAppUsageEntity
import com.rewindd.app.core.permissions.UsageAccessManager
import com.rewindd.app.domain.model.DailyAppUsage
import com.rewindd.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UsageStatsRepositoryTest {

    private lateinit var db: RewinddDatabase
    private lateinit var repository: UsageStatsRepository
    private lateinit var fakeUsageAccessManager: FakeUsageAccessManager

    class FakeUsageAccessManager(var hasPerm: Boolean = true) : UsageAccessManager {
        override fun hasPermission(): Boolean = hasPerm
        override fun createSettingsIntent(): android.content.Intent = android.content.Intent()
    }

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RewinddDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        fakeUsageAccessManager = FakeUsageAccessManager(hasPerm = true)
        repository = UsageStatsRepositoryImpl(
            dailyAppUsageDao = db.dailyAppUsageDao(),
            userPreferencesDao = db.userPreferencesDao(),
            usageAccessManager = fakeUsageAccessManager,
            context = context
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testUpsertPreventsDuplicates() = runBlocking {
        val record1 = DailyAppUsage(
            dateLocal = "2026-10-14",
            packageName = "com.instagram.android",
            usageMillis = 1800000L,
            sessionCount = 3
        )
        db.dailyAppUsageDao().upsertSingle(DailyAppUsageEntity.fromDomain(record1))

        var items = repository.getUsageForDateRange("2026-10-14", "2026-10-14").first()
        assertEquals(1, items.size)
        assertEquals(1800000L, items.first().usageMillis)

        // Updated dwell for same day & package
        val record1Updated = record1.copy(usageMillis = 2400000L, sessionCount = 5)
        db.dailyAppUsageDao().upsertSingle(DailyAppUsageEntity.fromDomain(record1Updated))

        items = repository.getUsageForDateRange("2026-10-14", "2026-10-14").first()
        assertEquals(1, items.size)
        assertEquals(2400000L, items.first().usageMillis)
        assertEquals(5, items.first().sessionCount)
    }

    @Test
    fun testBaselineDaysCount() = runBlocking {
        db.dailyAppUsageDao().upsertSingle(
            DailyAppUsageEntity(
                dateLocal = "2026-10-12",
                packageName = "com.instagram.android",
                usageMillis = 60000L,
                sessionCount = 1,
                pickupCount = 1,
                sourceStatus = "RELIABLE",
                lastCalculatedAtUtc = 1000L
            )
        )
        db.dailyAppUsageDao().upsertSingle(
            DailyAppUsageEntity(
                dateLocal = "2026-10-13",
                packageName = "com.google.android.youtube",
                usageMillis = 120000L,
                sessionCount = 2,
                pickupCount = 1,
                sourceStatus = "RELIABLE",
                lastCalculatedAtUtc = 2000L
            )
        )

        val daysCount = repository.getDistinctTrackedDaysCount().first()
        assertEquals(2, daysCount)
    }

    @Test
    fun testUpdateDailyGoalAndPreferences() = runBlocking {
        repository.updateDailyGoal("com.instagram.android", 45)
        val prefs = repository.getUserPreferences().first()
        assertEquals(45, prefs.dailyGoalsMinutes["com.instagram.android"])
    }

    @Test
    fun testPauseTracking() = runBlocking {
        val pauseUntil = System.currentTimeMillis() + 3600000L
        repository.setTrackingPaused(pauseUntil)
        val prefs = repository.getUserPreferences().first()
        assertTrue(prefs.isTrackingPaused())

        repository.setTrackingPaused(null)
        val unpaused = repository.getUserPreferences().first()
        assertFalse(unpaused.isTrackingPaused())
    }
}
