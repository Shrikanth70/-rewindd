package com.rewindd.app.feature.rewind

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.SavedItemRepositoryImpl
import com.rewindd.app.domain.model.ReviewState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RewindViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var db: RewinddDatabase
    private lateinit var repository: SavedItemRepository
    private lateinit var viewModel: RewindViewModel

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
        repository = SavedItemRepositoryImpl(
            dao = db.savedItemDao(),
            repositoryScope = CoroutineScope(testDispatcher + SupervisorJob())
        )
        viewModel = RewindViewModel(savedItemRepository = repository, undoTimeoutMs = 0L)
    }

    @After
    fun tearDown() {
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
    fun testEmptyQueueInitially() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.deckState.collect()
        }

        val state = viewModel.deckState.first { it !is RewindDeckState.Loading }
        assertTrue(state is RewindDeckState.EmptyQueue)
    }

    @Test
    fun testKeepActionAdvancesQueue() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.deckState.collect()
        }

        repository.saveUrl("https://example.com/item1")
        repository.saveUrl("https://example.com/item2")

        val state1 = viewModel.deckState.first { it is RewindDeckState.ActiveDeck && it.currentItem.url == "https://example.com/item1" } as RewindDeckState.ActiveDeck
        assertEquals("https://example.com/item1", state1.currentItem.url)

        // Keep item1
        viewModel.keepCurrentItem()

        // Wait until deck advances to item2
        val state2 = viewModel.deckState.first {
            it is RewindDeckState.ActiveDeck && it.currentItem.url == "https://example.com/item2"
        } as RewindDeckState.ActiveDeck
        assertEquals("https://example.com/item2", state2.currentItem.url)

        // Verify DB: item1 is REVIEWED
        val item1 = repository.getById(state1.currentItem.id)
        assertEquals(ReviewState.REVIEWED, item1?.reviewState)

        // Keep item2
        viewModel.keepCurrentItem()

        // Deck cleared!
        val finalState = viewModel.deckState.first { it is RewindDeckState.EmptyQueue }
        assertTrue(finalState is RewindDeckState.EmptyQueue)
    }

    @Test
    fun testArchiveMarksItemAsArchived() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.deckState.collect()
            viewModel.undoBannerItem.collect()
        }

        repository.saveUrl("https://example.com/archivable_mark")

        val active = viewModel.deckState.first { it is RewindDeckState.ActiveDeck } as RewindDeckState.ActiveDeck
        val itemId = active.currentItem.id

        // Archive
        viewModel.archiveCurrentItem()

        // Wait until undoBannerItem is set
        val bannerItem = viewModel.undoBannerItem.first { it != null }
        assertNotNull(bannerItem)

        // DB state: ARCHIVED
        val item = repository.getById(itemId)
        assertEquals(ReviewState.ARCHIVED, item?.reviewState)
    }

    @Test
    fun testArchiveAndUndoAction() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.deckState.collect()
            viewModel.undoBannerItem.collect()
        }

        repository.saveUrl("https://example.com/archivable")

        val active = viewModel.deckState.first { it is RewindDeckState.ActiveDeck } as RewindDeckState.ActiveDeck
        val itemId = active.currentItem.id

        // Archive
        viewModel.archiveCurrentItem()

        // Wait until deck becomes empty and undoBannerItem is set
        viewModel.deckState.first { it is RewindDeckState.EmptyQueue }
        val bannerItem = viewModel.undoBannerItem.first { it != null }
        assertNotNull(bannerItem)

        // Tap Undo immediately while banner is active
        viewModel.undoArchive()

        // Wait until deck is restored to ActiveDeck!
        val restoredDeck = viewModel.deckState.first { it is RewindDeckState.ActiveDeck && it.currentItem.id == itemId }
        assertNotNull(restoredDeck)

        // Wait until undo banner is cleared
        val bannerCleared = viewModel.undoBannerItem.first { it == null }
        assertEquals(null, bannerCleared)

        // DB state: UNREVIEWED
        val restoredItem = repository.getById(itemId)
        assertEquals(ReviewState.UNREVIEWED, restoredItem?.reviewState)
    }
}
