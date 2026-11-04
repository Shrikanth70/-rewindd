package com.rewindd.app.domain.usecase

import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.data.repository.UsageStatsRepository
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.DailyAppUsage
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.model.Tag
import com.rewindd.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PurgeDatabaseUseCaseTest {

    private lateinit var fakeSavedItemRepository: FakeSavedItemRepository
    private lateinit var fakeCollectionRepository: FakeCollectionRepository
    private lateinit var fakeTagRepository: FakeTagRepository
    private lateinit var fakeUsageStatsRepository: FakeUsageStatsRepository
    private lateinit var useCase: PurgeDatabaseUseCase

    class FakeSavedItemRepository : SavedItemRepository {
        var deletedAllCalled = false
        override fun getAllItems(): Flow<List<SavedItem>> = MutableStateFlow(emptyList())
        override fun getAllWithDetails(): Flow<List<SavedItemDetailsDomain>> = MutableStateFlow(emptyList())
        override fun getItemsByCollection(collectionId: String): Flow<List<SavedItemDetailsDomain>> = MutableStateFlow(emptyList())
        override fun getUnreviewedItems(): Flow<List<SavedItem>> = MutableStateFlow(emptyList())
        override fun getUnreviewedCount(): Flow<Int> = MutableStateFlow(0)
        override suspend fun saveUrl(rawInput: String, sourcePackage: String?, sourceLabel: String?): com.rewindd.app.data.repository.SaveResult =
            com.rewindd.app.data.repository.SaveResult.Invalid("test")
        override suspend fun getById(id: String): SavedItem? = null
        override suspend fun getWithDetailsById(id: String): SavedItemDetailsDomain? = null
        override suspend fun deleteById(id: String) {}
        override suspend fun deleteAll() { deletedAllCalled = true }
        override suspend fun updateReviewState(id: String, state: ReviewState) {}
        override suspend fun keepItem(id: String) {}
        override suspend fun archiveItem(id: String) {}
        override suspend fun undoArchive(id: String) {}
        override suspend fun updateDetails(id: String, title: String, note: String?, collectionId: String?, isPinned: Boolean) {}
        override suspend fun assignItemToCollection(itemId: String, collectionId: String?) {}
        override suspend fun removeItemFromCollection(itemId: String) {}
        override suspend fun togglePin(id: String) {}
        override suspend fun ensurePrototypeSeedData() {}
    }

    class FakeCollectionRepository : CollectionRepository {
        var deletedAllCalled = false
        override fun getAllCollections(): Flow<List<Collection>> = MutableStateFlow(emptyList())
        override suspend fun getById(id: String): Collection? = null
        override suspend fun createCollection(name: String, colorToken: String, isPinned: Boolean, sortOrder: Int, iconToken: String): Collection =
            Collection("1", name, colorToken, 0L, 0L, isPinned, sortOrder, iconToken)
        override suspend fun updateCollection(collection: Collection) {}
        override suspend fun deleteCollection(id: String) {}
        override suspend fun deleteAll() { deletedAllCalled = true }
    }

    class FakeTagRepository : TagRepository {
        var deletedAllCalled = false
        override fun getAllTags(): Flow<List<Tag>> = MutableStateFlow(emptyList())
        override suspend fun createOrFindTag(displayName: String, colorToken: String?): Tag = Tag("1", displayName, displayName.lowercase())
        override suspend fun assignTagToItem(savedItemId: String, tagId: String) {}
        override suspend fun removeTagFromItem(savedItemId: String, tagId: String) {}
        override suspend fun setTagsForItem(savedItemId: String, tagIds: List<String>) {}
        override suspend fun deleteTag(id: String) {}
        override suspend fun deleteAll() { deletedAllCalled = true }
    }

    class FakeUsageStatsRepository : UsageStatsRepository {
        var deleteAllUsageCalled = false
        override fun getUsageForDateRange(startDate: String, endDate: String): Flow<List<DailyAppUsage>> = MutableStateFlow(emptyList())
        override fun getUsageForPackage(packageName: String, startDate: String, endDate: String): Flow<List<DailyAppUsage>> = MutableStateFlow(emptyList())
        override fun getDistinctTrackedDaysCount(): Flow<Int> = MutableStateFlow(0)
        override fun getUserPreferences(): Flow<UserPreferences> = MutableStateFlow(UserPreferences())
        override suspend fun refreshUsage(lookbackDays: Int): Boolean = true
        override suspend fun updateTrackedApps(apps: Set<String>) {}
        override suspend fun updateDailyGoal(packageName: String, goalMinutes: Int) {}
        override suspend fun setTrackingPaused(untilUtc: Long?) {}
        override suspend fun deleteAllUsage() { deleteAllUsageCalled = true }
    }

    @Before
    fun setup() {
        fakeSavedItemRepository = FakeSavedItemRepository()
        fakeCollectionRepository = FakeCollectionRepository()
        fakeTagRepository = FakeTagRepository()
        fakeUsageStatsRepository = FakeUsageStatsRepository()

        useCase = PurgeDatabaseUseCase(
            savedItemRepository = fakeSavedItemRepository,
            collectionRepository = fakeCollectionRepository,
            tagRepository = fakeTagRepository,
            usageStatsRepository = fakeUsageStatsRepository
        )
    }

    @Test
    fun testPurgeAllDataCallsAllRepositories() = runTest {
        useCase.purgeAllData()

        assertTrue(fakeSavedItemRepository.deletedAllCalled)
        assertTrue(fakeCollectionRepository.deletedAllCalled)
        assertTrue(fakeTagRepository.deletedAllCalled)
        assertTrue(fakeUsageStatsRepository.deleteAllUsageCalled)
    }
}
