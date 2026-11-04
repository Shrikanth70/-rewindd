package com.rewindd.app.data.repository

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.core.permissions.UsageAccessManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AppTrackingPersistenceTest {

    private lateinit var database: RewinddDatabase
    private lateinit var usageStatsRepo: UsageStatsRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = RewinddDatabase.createInMemory(context)
        usageStatsRepo = UsageStatsRepositoryImpl(
            dailyAppUsageDao = database.dailyAppUsageDao(),
            userPreferencesDao = database.userPreferencesDao(),
            usageAccessManager = object : UsageAccessManager {
                override fun hasPermission() = true
                override fun createSettingsIntent(): Intent = Intent()
            },
            context = context
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun updateTrackedApps_persistsCustomSelection() = runBlocking {
        val initialPrefs = usageStatsRepo.getUserPreferences().first()
        // Default set is populated
        assertTrue(initialPrefs.trackedApps.contains("com.instagram.android"))

        // Select only YouTube and Reddit
        val newSelection = setOf("com.google.android.youtube", "com.reddit.frontpage")
        usageStatsRepo.updateTrackedApps(newSelection)

        val updatedPrefs = usageStatsRepo.getUserPreferences().first()
        assertEquals(2, updatedPrefs.trackedApps.size)
        assertTrue(updatedPrefs.trackedApps.contains("com.google.android.youtube"))
        assertTrue(updatedPrefs.trackedApps.contains("com.reddit.frontpage"))
        assertFalse(updatedPrefs.trackedApps.contains("com.instagram.android"))
    }

    @Test
    fun setTrackingPaused_persistsPauseState() = runBlocking {
        val pauseUntil = System.currentTimeMillis() + 3600000L
        usageStatsRepo.setTrackingPaused(pauseUntil)

        val prefs = usageStatsRepo.getUserPreferences().first()
        assertEquals(pauseUntil, prefs.trackingPausedUntilUtc)
        assertTrue(prefs.isTrackingPaused())

        // Clear pause
        usageStatsRepo.setTrackingPaused(null)
        val resumedPrefs = usageStatsRepo.getUserPreferences().first()
        assertEquals(null, resumedPrefs.trackingPausedUntilUtc)
        assertFalse(resumedPrefs.isTrackingPaused())
    }
}
