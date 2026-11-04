package com.rewindd.app.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.domain.model.ReviewState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SavedItemRepositoryTest {

    private lateinit var database: RewinddDatabase
    private lateinit var repository: SavedItemRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = RewinddDatabase.createInMemory(context)
        repository = SavedItemRepositoryImpl(database.savedItemDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveUrl_newValidUrl_persistsSuccessfully() = runBlocking {
        val rawUrl = "https://instagram.com/reel/C12345/?igsh=abc"
        val result = repository.saveUrl(rawUrl, sourcePackage = "com.instagram.android")

        assertTrue(result is SaveResult.Success)
        val saved = (result as SaveResult.Success).item
        assertEquals("https://instagram.com/reel/C12345", saved.normalizedUrl)
        assertEquals("com.instagram.android", saved.sourcePackage)

        val allItems = repository.getAllItems().first()
        assertEquals(1, allItems.size)
        assertEquals(saved.id, allItems[0].id)
    }

    @Test
    fun saveUrl_duplicateUrlWithDifferentTrackingParams_detectsDuplicate() = runBlocking {
        val firstUrl = "https://danluu.com/agent-loops-reliability?utm_source=twitter"
        val secondUrl = "https://danluu.com/agent-loops-reliability?utm_source=newsletter&utm_medium=email"

        val firstResult = repository.saveUrl(firstUrl)
        assertTrue(firstResult is SaveResult.Success)

        val secondResult = repository.saveUrl(secondUrl)
        assertTrue(secondResult is SaveResult.Duplicate)
        val duplicate = (secondResult as SaveResult.Duplicate).existingItem

        assertEquals("https://danluu.com/agent-loops-reliability", duplicate.normalizedUrl)

        // Verify no duplicate row was created in database
        val allItems = repository.getAllItems().first()
        assertEquals(1, allItems.size)
    }

    @Test
    fun deleteItem_removesFromDatabase() = runBlocking {
        val result = repository.saveUrl("https://youtube.com/watch?v=dQw4w9WgXcQ")
        assertTrue(result is SaveResult.Success)
        val item = (result as SaveResult.Success).item

        repository.deleteById(item.id)

        val itemAfterDelete = repository.getById(item.id)
        assertNull(itemAfterDelete)
        val allItems = repository.getAllItems().first()
        assertEquals(0, allItems.size)
    }

    @Test
    fun updateReviewState_updatesStateCorrectly() = runBlocking {
        val result = repository.saveUrl("https://substack.com/@danluu/post/1082")
        val item = (result as SaveResult.Success).item

        repository.updateReviewState(item.id, ReviewState.REVIEWED)

        val updated = repository.getById(item.id)
        assertNotNull(updated)
        assertEquals(ReviewState.REVIEWED, updated?.reviewState)
    }

    @Test
    fun ensurePrototypeSeedData_doesNotPopulateDummyData() = runBlocking {
        repository.ensurePrototypeSeedData()
        val allItems = repository.getAllItems().first()
        assertEquals(0, allItems.size)
    }

    @Test
    fun saveUrl_instagramUrl_triggersAsyncEnrichment() = runBlocking {
        com.rewindd.app.domain.usecase.InstagramMetadataExtractor.metadataFetcher = { _ ->
            com.rewindd.app.domain.usecase.InstagramMetadata(
                thumbnailUrl = "https://scontent.cdninstagram.com/v/mock_thumb.jpg",
                title = "Mock Creator on Instagram: \"Cool Reel\"",
                rawCaption = "Caption text"
            )
        }

        val result = repository.saveUrl("https://www.instagram.com/reel/DcgnHXLK2Sc/")
        assertTrue(result is SaveResult.Success)
        val initialItem = (result as SaveResult.Success).item

        // Allow background enrichment to complete
        kotlinx.coroutines.delay(300)

        val updated = repository.getById(initialItem.id)
        assertNotNull(updated)
        assertEquals("https://scontent.cdninstagram.com/v/mock_thumb.jpg", updated?.thumbnailUri)
        assertEquals(com.rewindd.app.domain.model.MetadataStatus.SUCCESS, updated?.metadataStatus)
        assertEquals("Mock Creator on Instagram: \"Cool Reel\"", updated?.title)

        com.rewindd.app.domain.usecase.InstagramMetadataExtractor.metadataFetcher = null
    }
}

