package com.rewindd.app.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.entity.CollectionEntity
import com.rewindd.app.core.database.entity.SavedItemEntity
import com.rewindd.app.core.database.entity.SavedItemTagEntity
import com.rewindd.app.core.database.entity.TagEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class RoomMigrationTest {

    private lateinit var db: RewinddDatabase
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RewinddDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCollectionAndTagInsertionAndCascade() = runBlocking {
        val collectionId = UUID.randomUUID().toString()
        val collection = CollectionEntity(
            id = collectionId,
            name = "Design Inspiration",
            colorToken = "rewind-yellow",
            createdAtUtc = System.currentTimeMillis(),
            updatedAtUtc = System.currentTimeMillis(),
            isPinned = true
        )
        db.collectionDao().insert(collection)

        val retrievedCollection = db.collectionDao().getById(collectionId)
        assertNotNull(retrievedCollection)
        assertEquals("Design Inspiration", retrievedCollection?.name)

        val tagId = UUID.randomUUID().toString()
        val tag = TagEntity(
            id = tagId,
            normalizedName = "ux",
            displayName = "#UX",
            colorToken = "lilac"
        )
        db.tagDao().insert(tag)

        val retrievedTag = db.tagDao().getById(tagId)
        assertNotNull(retrievedTag)
        assertEquals("ux", retrievedTag?.normalizedName)

        val itemId = UUID.randomUUID().toString()
        val item = SavedItemEntity(
            id = itemId,
            url = "https://example.com/ux-guide",
            normalizedUrl = "example.com/ux-guide",
            sourcePackage = "com.android.chrome",
            sourceLabel = "Chrome",
            title = "UX Guide",
            thumbnailUri = null,
            capturedAtUtc = System.currentTimeMillis(),
            updatedAtUtc = System.currentTimeMillis(),
            reviewState = "UNREVIEWED",
            isPinned = false,
            collectionId = collectionId,
            note = "Important design notes",
            metadataStatus = "SUCCESS",
            archivedAtUtc = null,
            contentHash = null
        )
        db.savedItemDao().insert(item)

        // Assign tag
        db.tagDao().insertItemTag(SavedItemTagEntity(savedItemId = itemId, tagId = tagId))

        // Retrieve with details
        val details = db.savedItemDao().getWithDetailsById(itemId)
        assertNotNull(details)
        assertEquals("UX Guide", details?.item?.title)
        assertEquals("Design Inspiration", details?.collection?.name)
        assertEquals(1, details?.tags?.size)
        assertEquals("ux", details?.tags?.first()?.normalizedName)

        // Test unassign collection
        db.collectionDao().unassignCollectionFromItems(collectionId)
        val detailsAfterUnassign = db.savedItemDao().getWithDetailsById(itemId)
        assertEquals(null, detailsAfterUnassign?.item?.collectionId)
        assertEquals(null, detailsAfterUnassign?.collection)

        // Test item deletion cascades to join table
        db.savedItemDao().deleteById(itemId)
        val detailsAfterDelete = db.savedItemDao().getWithDetailsById(itemId)
        assertEquals(null, detailsAfterDelete)
    }

    @Test
    fun testDailyAppUsageAndUserPreferencesPersistence() = runBlocking {
        // Test DailyAppUsage composite key insert and update
        val usage1 = com.rewindd.app.core.database.entity.DailyAppUsageEntity(
            dateLocal = "2026-10-14",
            packageName = "com.instagram.android",
            usageMillis = 1800000L,
            sessionCount = 5,
            pickupCount = 3,
            sourceStatus = "RELIABLE",
            lastCalculatedAtUtc = System.currentTimeMillis()
        )
        db.dailyAppUsageDao().upsertSingle(usage1)

        val retrieved1 = db.dailyAppUsageDao().getUsageForDate("2026-10-14")
        assertEquals(1, retrieved1.size)
        assertEquals(1800000L, retrieved1.first().usageMillis)
        assertEquals(5, retrieved1.first().sessionCount)

        // Upsert updated dwell time for same day & package (no duplicate rows)
        val usage1Updated = usage1.copy(usageMillis = 2400000L, sessionCount = 7)
        db.dailyAppUsageDao().upsertSingle(usage1Updated)

        val retrievedAfterUpdate = db.dailyAppUsageDao().getUsageForDate("2026-10-14")
        assertEquals(1, retrievedAfterUpdate.size)
        assertEquals(2400000L, retrievedAfterUpdate.first().usageMillis)
        assertEquals(7, retrievedAfterUpdate.first().sessionCount)

        // Test UserPreferences singleton
        val initialPrefs = db.userPreferencesDao().getPreferences()
        // If not initialized, insert
        val testPrefs = com.rewindd.app.core.database.entity.UserPreferencesEntity(
            id = 1,
            onboardingComplete = true,
            trackedAppsJson = "[\"com.instagram.android\",\"com.google.android.youtube\"]",
            dailyGoalsJson = "{\"com.instagram.android\":45}",
            trackingPausedUntilUtc = null,
            retentionMonths = 12,
            remindersEnabled = true
        )
        db.userPreferencesDao().upsertPreferences(testPrefs)

        val updatedPrefs = db.userPreferencesDao().getPreferences()
        assertNotNull(updatedPrefs)
        val domainPrefs = updatedPrefs!!.toDomain()
        assertTrue(domainPrefs.onboardingComplete)
        assertEquals(2, domainPrefs.trackedApps.size)
        assertEquals(45, domainPrefs.dailyGoalsMinutes["com.instagram.android"])
    }
}
