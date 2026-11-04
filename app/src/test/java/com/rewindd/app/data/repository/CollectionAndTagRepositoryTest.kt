package com.rewindd.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
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

@RunWith(RobolectricTestRunner::class)
class CollectionAndTagRepositoryTest {

    private lateinit var db: RewinddDatabase
    private lateinit var collectionRepository: CollectionRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var savedItemRepository: SavedItemRepository
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RewinddDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        collectionRepository = CollectionRepositoryImpl(db.collectionDao())
        tagRepository = TagRepositoryImpl(db.tagDao())
        savedItemRepository = SavedItemRepositoryImpl(db.savedItemDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCollectionCrudOperations() = runBlocking {
        val created = collectionRepository.createCollection(
            name = "Engineering & Android",
            colorToken = "rewind-yellow",
            isPinned = true
        )
        assertNotNull(created.id)
        assertEquals("Engineering & Android", created.name)
        assertTrue(created.isPinned)

        val all = collectionRepository.getAllCollections().first()
        assertEquals(1, all.size)
        assertEquals(created.id, all.first().id)

        // Update
        collectionRepository.updateCollection(created.copy(name = "Android Dev"))
        val updated = collectionRepository.getById(created.id)
        assertEquals("Android Dev", updated?.name)

        // Delete
        collectionRepository.deleteCollection(created.id)
        val afterDelete = collectionRepository.getAllCollections().first()
        assertTrue(afterDelete.isEmpty())
    }

    @Test
    fun testTagNormalizationAndAssignment() = runBlocking {
        val tag1 = tagRepository.createOrFindTag("#Kotlin")
        val tag2 = tagRepository.createOrFindTag("kotlin") // Should normalize and return same

        assertEquals(tag1.id, tag2.id)
        assertEquals("kotlin", tag1.normalizedName)
        assertEquals("#Kotlin", tag1.displayName)

        // Save an item
        val saveResult = savedItemRepository.saveUrl("https://kotlinlang.org")
        assertTrue(saveResult is SaveResult.Success)
        val itemId = (saveResult as SaveResult.Success).item.id

        // Set tags
        tagRepository.setTagsForItem(itemId, listOf(tag1.id))

        val details = savedItemRepository.getWithDetailsById(itemId)
        assertNotNull(details)
        assertEquals(1, details?.tags?.size)
        assertEquals("kotlin", details?.tags?.first()?.normalizedName)
    }

    @Test
    fun testSavedItemDetailsAndPinToggle() = runBlocking {
        val saveResult = savedItemRepository.saveUrl("https://developer.android.com")
        val item = (saveResult as SaveResult.Success).item

        val collection = collectionRepository.createCollection("Docs", "blue")

        // Update details
        savedItemRepository.updateDetails(
            id = item.id,
            title = "Android Developers Official",
            note = "Bookmark for compose guides",
            collectionId = collection.id,
            isPinned = true
        )

        val updated = savedItemRepository.getWithDetailsById(item.id)
        assertNotNull(updated)
        assertEquals("Android Developers Official", updated?.item?.title)
        assertEquals("Bookmark for compose guides", updated?.item?.note)
        assertEquals(collection.id, updated?.item?.collectionId)
        assertTrue(updated?.item?.isPinned == true)

        // Toggle pin
        savedItemRepository.togglePin(item.id)
        val unpinned = savedItemRepository.getById(item.id)
        assertTrue(unpinned?.isPinned == false)
    }

    @Test
    fun testCollectionItemAssignmentAndRemoval() = runBlocking {
        val collection = collectionRepository.createCollection("Design Systems", "lilac")
        val saveResult = savedItemRepository.saveUrl("https://material.io")
        val item = (saveResult as SaveResult.Success).item

        // Assign to collection
        savedItemRepository.assignItemToCollection(item.id, collection.id)
        val assigned = savedItemRepository.getById(item.id)
        assertEquals(collection.id, assigned?.collectionId)

        // Remove from collection
        savedItemRepository.removeItemFromCollection(item.id)
        val removed = savedItemRepository.getById(item.id)
        assertTrue(removed?.collectionId == null)
    }

    @Test
    fun testDeleteCollectionPreservesSavedItems() = runBlocking {
        val collection = collectionRepository.createCollection("Temporary", "hot-pink")
        val saveResult = savedItemRepository.saveUrl("https://google.com")
        val item = (saveResult as SaveResult.Success).item

        savedItemRepository.assignItemToCollection(item.id, collection.id)
        assertEquals(collection.id, savedItemRepository.getById(item.id)?.collectionId)

        // Delete collection
        collectionRepository.deleteCollection(collection.id)

        // Collection is gone
        assertTrue(collectionRepository.getById(collection.id) == null)

        // Saved item still exists, with unassigned collection
        val preservedItem = savedItemRepository.getById(item.id)
        assertNotNull(preservedItem)
        assertEquals("google.com", preservedItem?.title)
        assertTrue(preservedItem?.collectionId == null)
    }
}
