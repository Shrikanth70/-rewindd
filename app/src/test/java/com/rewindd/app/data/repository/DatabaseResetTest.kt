package com.rewindd.app.data.repository

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.core.database.entity.DailyAppUsageEntity
import com.rewindd.app.core.permissions.UsageAccessManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DatabaseResetTest {

    private lateinit var database: RewinddDatabase
    private lateinit var savedItemRepo: SavedItemRepository
    private lateinit var collectionRepo: CollectionRepository
    private lateinit var tagRepo: TagRepository
    private lateinit var usageStatsRepo: UsageStatsRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = RewinddDatabase.createInMemory(context)
        savedItemRepo = SavedItemRepositoryImpl(database.savedItemDao())
        collectionRepo = CollectionRepositoryImpl(database.collectionDao())
        tagRepo = TagRepositoryImpl(database.tagDao())
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
    fun resetAllData_clearsItemsCollectionsTagsAndUsage() = runBlocking {
        // 1. Seed data
        val saveResult = savedItemRepo.saveUrl("https://example.com/test", "com.test", "Test")
        assertTrue(saveResult is SaveResult.Success)
        val itemId = (saveResult as SaveResult.Success).item.id

        val collection = collectionRepo.createCollection("Design", "#FFC53D")
        val tag = tagRepo.createOrFindTag("tech")
        tagRepo.assignTagToItem(itemId, tag.id)

        database.dailyAppUsageDao().upsertUsage(
            listOf(
                DailyAppUsageEntity(
                    dateLocal = "2026-10-15",
                    packageName = "com.test",
                    usageMillis = 3600000L,
                    sessionCount = 5,
                    pickupCount = 2,
                    sourceStatus = "RELIABLE",
                    lastCalculatedAtUtc = System.currentTimeMillis()
                )
            )
        )

        // Verify data exists
        assertEquals(1, savedItemRepo.getAllItems().first().size)
        assertEquals(1, collectionRepo.getAllCollections().first().size)
        assertEquals(1, tagRepo.getAllTags().first().size)
        assertEquals(1, usageStatsRepo.getUsageForDateRange("2026-10-01", "2026-10-31").first().size)

        // 2. Perform Clear All
        savedItemRepo.deleteAll()
        collectionRepo.deleteAll()
        tagRepo.deleteAll()
        usageStatsRepo.deleteAllUsage()

        // 3. Verify everything is wiped clean
        assertTrue(savedItemRepo.getAllItems().first().isEmpty())
        assertTrue(collectionRepo.getAllCollections().first().isEmpty())
        assertTrue(tagRepo.getAllTags().first().isEmpty())
        assertTrue(usageStatsRepo.getUsageForDateRange("2026-10-01", "2026-10-31").first().isEmpty())
    }
}
