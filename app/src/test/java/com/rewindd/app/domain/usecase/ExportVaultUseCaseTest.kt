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
import com.rewindd.app.domain.model.UsageSourceStatus
import com.rewindd.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExportVaultUseCaseTest {

    private lateinit var fakeSavedItemRepository: FakeSavedItemRepository
    private lateinit var fakeCollectionRepository: FakeCollectionRepository
    private lateinit var fakeTagRepository: FakeTagRepository
    private lateinit var fakeUsageStatsRepository: FakeUsageStatsRepository
    private lateinit var useCase: ExportVaultUseCase

    class FakeSavedItemRepository : SavedItemRepository {
        val detailsFlow = MutableStateFlow<List<SavedItemDetailsDomain>>(emptyList())
        override fun getAllItems(): Flow<List<SavedItem>> = MutableStateFlow(emptyList())
        override fun getAllWithDetails(): Flow<List<SavedItemDetailsDomain>> = detailsFlow.asStateFlow()
        override fun getItemsByCollection(collectionId: String): Flow<List<SavedItemDetailsDomain>> = detailsFlow.asStateFlow()
        override fun getUnreviewedItems(): Flow<List<SavedItem>> = MutableStateFlow(emptyList())
        override fun getUnreviewedCount(): Flow<Int> = MutableStateFlow(0)
        override suspend fun saveUrl(rawInput: String, sourcePackage: String?, sourceLabel: String?): com.rewindd.app.data.repository.SaveResult =
            com.rewindd.app.data.repository.SaveResult.Invalid("test")
        override suspend fun getById(id: String): SavedItem? = null
        override suspend fun getWithDetailsById(id: String): SavedItemDetailsDomain? = null
        override suspend fun deleteById(id: String) {}
        override suspend fun deleteAll() {}
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
        val collectionsFlow = MutableStateFlow<List<Collection>>(emptyList())
        override fun getAllCollections(): Flow<List<Collection>> = collectionsFlow.asStateFlow()
        override suspend fun getById(id: String): Collection? = null
        override suspend fun createCollection(name: String, colorToken: String, isPinned: Boolean, sortOrder: Int, iconToken: String): Collection =
            Collection("1", name, colorToken, 0L, 0L, isPinned, sortOrder, iconToken)
        override suspend fun updateCollection(collection: Collection) {}
        override suspend fun deleteCollection(id: String) {}
        override suspend fun deleteAll() {}
    }

    class FakeTagRepository : TagRepository {
        val tagsFlow = MutableStateFlow<List<Tag>>(emptyList())
        override fun getAllTags(): Flow<List<Tag>> = tagsFlow.asStateFlow()
        override suspend fun createOrFindTag(displayName: String, colorToken: String?): Tag = Tag("1", displayName, displayName.lowercase())
        override suspend fun assignTagToItem(savedItemId: String, tagId: String) {}
        override suspend fun removeTagFromItem(savedItemId: String, tagId: String) {}
        override suspend fun setTagsForItem(savedItemId: String, tagIds: List<String>) {}
        override suspend fun deleteTag(id: String) {}
        override suspend fun deleteAll() {}
    }

    class FakeUsageStatsRepository : UsageStatsRepository {
        val usageFlow = MutableStateFlow<List<DailyAppUsage>>(emptyList())
        val prefsFlow = MutableStateFlow(UserPreferences())
        override fun getUsageForDateRange(startDate: String, endDate: String): Flow<List<DailyAppUsage>> = usageFlow.asStateFlow()
        override fun getUsageForPackage(packageName: String, startDate: String, endDate: String): Flow<List<DailyAppUsage>> = usageFlow.asStateFlow()
        override fun getDistinctTrackedDaysCount(): Flow<Int> = MutableStateFlow(0)
        override fun getUserPreferences(): Flow<UserPreferences> = prefsFlow.asStateFlow()
        override suspend fun refreshUsage(lookbackDays: Int): Boolean = true
        override suspend fun updateTrackedApps(apps: Set<String>) {}
        override suspend fun updateDailyGoal(packageName: String, goalMinutes: Int) {}
        override suspend fun setTrackingPaused(untilUtc: Long?) {}
        override suspend fun deleteAllUsage() {}
    }

    @Before
    fun setup() {
        fakeSavedItemRepository = FakeSavedItemRepository()
        fakeCollectionRepository = FakeCollectionRepository()
        fakeTagRepository = FakeTagRepository()
        fakeUsageStatsRepository = FakeUsageStatsRepository()

        useCase = ExportVaultUseCase(
            savedItemRepository = fakeSavedItemRepository,
            collectionRepository = fakeCollectionRepository,
            tagRepository = fakeTagRepository,
            usageStatsRepository = fakeUsageStatsRepository
        )
    }

    @Test
    fun testExportJsonProducesValidStructure() = runTest {
        val now = System.currentTimeMillis()
        val item = SavedItem(
            id = "item_1",
            url = "https://example.com/article",
            normalizedUrl = "https://example.com/article",
            title = "Test Article",
            capturedAtUtc = now,
            updatedAtUtc = now
        )
        val detail = SavedItemDetailsDomain(item, null, emptyList())
        fakeSavedItemRepository.detailsFlow.value = listOf(detail)
        fakeCollectionRepository.collectionsFlow.value = listOf(
            Collection("col_1", "Tech", "blue", now, now)
        )

        val json = useCase.exportJson()
        assertNotNull(json)
        assertTrue(json.contains("\"appName\": \"Rewindd\""))
        assertTrue(json.contains("\"Test Article\""))
        assertTrue(json.contains("\"Tech\""))
    }

    @Test
    fun testExportMarkdownGeneratesReadableContent() = runTest {
        val now = System.currentTimeMillis()
        val item = SavedItem(
            id = "item_1",
            url = "https://example.com",
            normalizedUrl = "https://example.com",
            title = "Kotlin Lang",
            capturedAtUtc = now,
            updatedAtUtc = now
        )
        val detail = SavedItemDetailsDomain(item, null, emptyList())
        fakeSavedItemRepository.detailsFlow.value = listOf(detail)

        val md = useCase.exportMarkdown()
        assertNotNull(md)
        assertTrue(md.contains("# Rewindd Vault Export"))
        assertTrue(md.contains("Kotlin Lang"))
    }

    @Test
    fun testExportScreenTimeToCsvGeneratesColumns() = runTest {
        val usage = DailyAppUsage(
            dateLocal = "2026-10-16",
            packageName = "com.instagram.android",
            usageMillis = 1800000L,
            sessionCount = 5,
            pickupCount = 4,
            sourceStatus = UsageSourceStatus.RELIABLE
        )
        fakeUsageStatsRepository.usageFlow.value = listOf(usage)

        val csv = useCase.exportScreenTimeCsv(lookbackDays = 7)
        assertNotNull(csv)
        assertTrue(csv.contains("Date,PackageName,DwellMinutes,DwellMillis,Sessions,Pickups,Status"))
        assertTrue(csv.contains("2026-10-16,com.instagram.android,30.0,1800000,5,4,RELIABLE"))
    }
}
