package com.rewindd.app.feature.home

import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.data.repository.SaveResult
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.domain.model.SavedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    class FakeSavedItemRepository : SavedItemRepository {
        val sampleItem = SavedItem(
            id = "item-1",
            url = "https://example.com/test",
            normalizedUrl = "example.com/test",
            title = "Test Article",
            sourceLabel = "Example",
            capturedAtUtc = System.currentTimeMillis(),
            updatedAtUtc = System.currentTimeMillis()
        )
        val unreviewedCountFlow = MutableStateFlow(3)
        val unreviewedItemsFlow = MutableStateFlow(listOf(sampleItem))

        override fun getAllItems(): Flow<List<SavedItem>> = MutableStateFlow(listOf(sampleItem))
        override fun getAllWithDetails(): Flow<List<SavedItemDetailsDomain>> = MutableStateFlow(emptyList())
        override fun getItemsByCollection(collectionId: String): Flow<List<SavedItemDetailsDomain>> = MutableStateFlow(emptyList())
        override fun getUnreviewedItems(): Flow<List<SavedItem>> = unreviewedItemsFlow.asStateFlow()
        override fun getUnreviewedCount(): Flow<Int> = unreviewedCountFlow.asStateFlow()
        override suspend fun saveUrl(rawInput: String, sourcePackage: String?, sourceLabel: String?): SaveResult = SaveResult.Invalid("test")
        override suspend fun getById(id: String): SavedItem? = sampleItem
        override suspend fun getWithDetailsById(id: String): SavedItemDetailsDomain? = null
        override suspend fun deleteById(id: String) {}
        override suspend fun deleteAll() {}
        override suspend fun updateReviewState(id: String, state: com.rewindd.app.domain.model.ReviewState) {}
        override suspend fun keepItem(id: String) {}
        override suspend fun archiveItem(id: String) {}
        override suspend fun undoArchive(id: String) {}
        override suspend fun updateDetails(id: String, title: String, note: String?, collectionId: String?, isPinned: Boolean) {}
        override suspend fun assignItemToCollection(itemId: String, collectionId: String?) {}
        override suspend fun removeItemFromCollection(itemId: String) {}
        override suspend fun togglePin(id: String) {}
        override suspend fun ensurePrototypeSeedData() {}
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUnreviewedCountAndTopItem() = runTest {
        val fakeSavedItems = FakeSavedItemRepository()
        val viewModel = HomeViewModel(savedItemRepository = fakeSavedItems)

        assertEquals(3, viewModel.unreviewedCount.first())
        val topItem = viewModel.topUnreviewedItem.first()
        assertNotNull(topItem)
        assertEquals("Test Article", topItem?.title)
    }
}
