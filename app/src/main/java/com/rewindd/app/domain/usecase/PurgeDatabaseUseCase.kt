package com.rewindd.app.domain.usecase

import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.data.repository.UsageStatsRepository

class PurgeDatabaseUseCase(
    private val savedItemRepository: SavedItemRepository,
    private val collectionRepository: CollectionRepository,
    private val tagRepository: TagRepository,
    private val usageStatsRepository: UsageStatsRepository
) {
    suspend fun purgeAllData() {
        savedItemRepository.deleteAll()
        collectionRepository.deleteAll()
        tagRepository.deleteAll()
        usageStatsRepository.deleteAllUsage()
    }
}
