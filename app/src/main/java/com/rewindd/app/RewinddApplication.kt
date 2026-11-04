package com.rewindd.app

import android.app.Application
import com.rewindd.app.core.database.RewinddDatabase
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.CollectionRepositoryImpl
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.SavedItemRepositoryImpl
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.data.repository.TagRepositoryImpl

class RewinddApplication : Application() {

    lateinit var database: RewinddDatabase
        private set

    lateinit var savedItemRepository: SavedItemRepository
        private set

    lateinit var collectionRepository: CollectionRepository
        private set

    lateinit var tagRepository: TagRepository
        private set

    lateinit var usageAccessManager: com.rewindd.app.core.permissions.UsageAccessManager
        private set

    lateinit var usageStatsRepository: com.rewindd.app.data.repository.UsageStatsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = RewinddDatabase.getInstance(this)
        savedItemRepository = SavedItemRepositoryImpl(database.savedItemDao())
        collectionRepository = CollectionRepositoryImpl(database.collectionDao())
        tagRepository = TagRepositoryImpl(database.tagDao())
        usageAccessManager = com.rewindd.app.core.permissions.UsageAccessManagerImpl(this)
        usageStatsRepository = com.rewindd.app.data.repository.UsageStatsRepositoryImpl(
            dailyAppUsageDao = database.dailyAppUsageDao(),
            userPreferencesDao = database.userPreferencesDao(),
            usageAccessManager = usageAccessManager,
            context = this
        )
    }
}
