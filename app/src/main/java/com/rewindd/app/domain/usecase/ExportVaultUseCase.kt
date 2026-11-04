package com.rewindd.app.domain.usecase

import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.data.repository.UsageStatsRepository
import com.rewindd.app.domain.export.VaultDataExporter
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class ExportVaultUseCase(
    private val savedItemRepository: SavedItemRepository,
    private val collectionRepository: CollectionRepository,
    private val tagRepository: TagRepository,
    private val usageStatsRepository: UsageStatsRepository
) {
    suspend fun exportJson(): String {
        val items = savedItemRepository.getAllWithDetails().first()
        val collections = collectionRepository.getAllCollections().first()
        val tags = tagRepository.getAllTags().first()
        val prefs = usageStatsRepository.getUserPreferences().first()
        return VaultDataExporter.exportToJson(items, collections, tags, prefs)
    }

    suspend fun exportMarkdown(): String {
        val items = savedItemRepository.getAllWithDetails().first()
        val collections = collectionRepository.getAllCollections().first()
        val tags = tagRepository.getAllTags().first()
        return VaultDataExporter.exportToMarkdown(items, collections, tags)
    }

    suspend fun exportCsv(): String {
        val items = savedItemRepository.getAllWithDetails().first()
        return VaultDataExporter.exportItemsToCsv(items)
    }

    suspend fun exportScreenTimeCsv(lookbackDays: Int = 30): String {
        val today = LocalDate.now()
        val start = today.minusDays(lookbackDays.toLong()).toString()
        val end = today.toString()
        val usage = usageStatsRepository.getUsageForDateRange(start, end).first()
        return VaultDataExporter.exportScreenTimeToCsv(usage)
    }
}
