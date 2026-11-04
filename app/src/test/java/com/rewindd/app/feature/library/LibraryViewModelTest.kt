package com.rewindd.app.feature.library

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.CollectionRepositoryImpl
import com.rewindd.app.data.repository.SaveResult
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.SavedItemRepositoryImpl
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.data.repository.TagRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LibraryViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var db: RewinddDatabase
    private lateinit var savedItemRepository: SavedItemRepository
    private lateinit var collectionRepository: CollectionRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var viewModel: LibraryViewModel
    private lateinit var repoScope: CoroutineScope

    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val directExecutor = java.util.concurrent.Executor { it.run() }
        db = Room.inMemoryDatabaseBuilder(context, RewinddDatabase::class.java)
            .allowMainThreadQueries()
            .setQueryExecutor(directExecutor)
            .setTransactionExecutor(directExecutor)
            .build()
        repoScope = CoroutineScope(testDispatcher + SupervisorJob())
        savedItemRepository = SavedItemRepositoryImpl(
            dao = db.savedItemDao(),
            repositoryScope = repoScope
        )
        collectionRepository = CollectionRepositoryImpl(db.collectionDao())
        tagRepository = TagRepositoryImpl(db.tagDao())

        viewModel = LibraryViewModel(
            savedItemRepository = savedItemRepository,
            collectionRepository = collectionRepository,
            tagRepository = tagRepository
        )
    }

    @After
    fun tearDown() {
        repoScope.cancel()
        testDispatcher.scheduler.advanceUntilIdle()
        try {
            val clearMethod = androidx.lifecycle.ViewModel::class.java.getDeclaredMethod("onCleared")
            clearMethod.isAccessible = true
            clearMethod.invoke(viewModel)
        } catch (_: Exception) {}
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun testTabFiltering() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.filteredItems.collect()
        }

        // Save unreviewed item
        savedItemRepository.saveUrl("https://example.com/item1")
        // Save another item and archive it
        val save2 = savedItemRepository.saveUrl("https://example.com/item2")
        val item2 = (save2 as SaveResult.Success).item
        savedItemRepository.archiveItem(item2.id)

        // Save third item and pin it
        val save3 = savedItemRepository.saveUrl("https://example.com/item3")
        val item3 = (save3 as SaveResult.Success).item
        savedItemRepository.togglePin(item3.id)

        // Tab: ALL (all active non-archived items)
        viewModel.setTab(LibraryTab.ALL)
        var items = viewModel.filteredItems.first { it.size == 2 }
        assertEquals(2, items.size)

        // Tab: INBOX
        viewModel.setTab(LibraryTab.INBOX)
        items = viewModel.filteredItems.first { it.size == 2 }
        assertEquals(2, items.size)

        // Tab: ARCHIVED
        viewModel.setTab(LibraryTab.ARCHIVED)
        items = viewModel.filteredItems.first { it.size == 1 }
        assertEquals(1, items.size)
        assertEquals(item2.id, items.first().item.id)

        // Tab: PINNED
        viewModel.setTab(LibraryTab.PINNED)
        items = viewModel.filteredItems.first { it.size == 1 }
        assertEquals(1, items.size)
        assertEquals(item3.id, items.first().item.id)
    }

    @Test
    fun testSearchQueryFiltering() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.filteredItems.collect()
        }

        savedItemRepository.saveUrl("https://kotlinlang.org")
        savedItemRepository.saveUrl("https://developer.android.com")

        viewModel.setSearchQuery("android")
        val results = viewModel.filteredItems.first { it.size == 1 }
        assertEquals(1, results.size)
        assertTrue(results.first().item.normalizedUrl.contains("android"))
    }

    @Test
    fun testCreateCollectionWithCallback() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allCollections.collect()
        }

        var createdId: String? = null
        viewModel.createCollection("Design Systems", "lilac", isPinned = true) { col ->
            createdId = col.id
        }

        val collections = viewModel.allCollections.first { it.isNotEmpty() }
        assertEquals(1, collections.size)
        assertEquals("Design Systems", collections.first().name)
        assertEquals("lilac", collections.first().colorToken)
        assertTrue(collections.first().isPinned)
        assertEquals(createdId, collections.first().id)
        advanceUntilIdle()
    }

    @Test
    fun testToggleCollectionPin() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allCollections.collect()
        }

        var col: com.rewindd.app.domain.model.Collection? = null
        viewModel.createCollection("Design Systems", "lilac", isPinned = false) { created ->
            col = created
        }

        val initial = viewModel.allCollections.first { it.isNotEmpty() }
        assertEquals(false, initial.first().isPinned)

        viewModel.toggleCollectionPin(col!!)

        val updated = viewModel.allCollections.first { it.first().isPinned }
        assertTrue(updated.first().isPinned)
        advanceUntilIdle()
    }
}
