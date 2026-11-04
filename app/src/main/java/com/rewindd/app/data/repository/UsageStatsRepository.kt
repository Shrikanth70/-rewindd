package com.rewindd.app.data.repository

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import com.rewindd.app.core.database.dao.DailyAppUsageDao
import com.rewindd.app.core.database.dao.UserPreferencesDao
import com.rewindd.app.core.database.entity.DailyAppUsageEntity
import com.rewindd.app.core.database.entity.UserPreferencesEntity
import com.rewindd.app.core.permissions.UsageAccessManager
import com.rewindd.app.domain.analytics.RawUsageEvent
import com.rewindd.app.domain.analytics.UsageAggregator
import com.rewindd.app.domain.model.DailyAppUsage
import com.rewindd.app.domain.model.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

interface UsageStatsRepository {
    fun getUsageForDateRange(startDate: String, endDate: String): Flow<List<DailyAppUsage>>
    fun getUsageForPackage(packageName: String, startDate: String, endDate: String): Flow<List<DailyAppUsage>>
    fun getDistinctTrackedDaysCount(): Flow<Int>
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun refreshUsage(lookbackDays: Int = 7): Boolean
    suspend fun updateTrackedApps(apps: Set<String>)
    suspend fun updateDailyGoal(packageName: String, goalMinutes: Int)
    suspend fun setTrackingPaused(untilUtc: Long?)
    suspend fun deleteAllUsage()
}

class UsageStatsRepositoryImpl(
    private val dailyAppUsageDao: DailyAppUsageDao,
    private val userPreferencesDao: UserPreferencesDao,
    private val usageAccessManager: UsageAccessManager,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsageStatsRepository {

    private val defaultTrackedApps = setOf(
        "com.instagram.android",
        "com.google.android.youtube",
        "com.android.chrome",
        "com.reddit.frontpage",
        "com.twitter.android",
        "com.zhiliaoapp.musically"
    )

    override fun getUsageForDateRange(startDate: String, endDate: String): Flow<List<DailyAppUsage>> {
        return dailyAppUsageDao.getUsageForDateRangeFlow(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUsageForPackage(packageName: String, startDate: String, endDate: String): Flow<List<DailyAppUsage>> {
        return dailyAppUsageDao.getUsageForPackageFlow(packageName, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDistinctTrackedDaysCount(): Flow<Int> {
        return dailyAppUsageDao.getDistinctTrackedDaysCountFlow()
    }

    override fun getUserPreferences(): Flow<UserPreferences> {
        return userPreferencesDao.getPreferencesFlow().map { entity ->
            entity?.toDomain() ?: UserPreferences(trackedApps = defaultTrackedApps)
        }
    }

    private val refreshMutex = Mutex()

    override suspend fun refreshUsage(lookbackDays: Int): Boolean = withContext(ioDispatcher) {
        if (!usageAccessManager.hasPermission()) return@withContext false
        if (!refreshMutex.tryLock()) {
            // Already refreshing, await current refresh completion to prevent redundant queries
            refreshMutex.withLock { return@withContext true }
        }

        try {
            val currentPrefs = userPreferencesDao.getPreferences()?.toDomain() 
                ?: UserPreferences(trackedApps = defaultTrackedApps)

            val now = System.currentTimeMillis()
            if (currentPrefs.isTrackingPaused(now)) return@withContext false

            val usageManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager 
                ?: return@withContext false

            val zoneId = ZoneId.systemDefault()
            val today = LocalDate.now(zoneId)
            val startTime = today.minusDays(lookbackDays.toLong())
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli()

            val rawEvents = mutableListOf<RawUsageEvent>()

            try {
                val events = usageManager.queryEvents(startTime, now)
                val event = UsageEvents.Event()
                while (events.hasNextEvent()) {
                    events.getNextEvent(event)
                    val type = when (event.eventType) {
                        UsageEvents.Event.MOVE_TO_FOREGROUND -> RawUsageEvent.TYPE_FOREGROUND
                        UsageEvents.Event.MOVE_TO_BACKGROUND,
                        23 /* UsageEvents.Event.ACTIVITY_STOPPED */ -> RawUsageEvent.TYPE_BACKGROUND
                        16 /* UsageEvents.Event.SCREEN_NON_INTERACTIVE */,
                        18 /* UsageEvents.Event.KEYGUARD_SHOWN */ -> RawUsageEvent.TYPE_SCREEN_OFF
                        else -> continue
                    }
                    rawEvents.add(
                        RawUsageEvent(
                            packageName = event.packageName,
                            timestamp = event.timeStamp,
                            eventType = type
                        )
                    )
                }
            } catch (_: Exception) {
                return@withContext false
            }

            val dailyAggregates = UsageAggregator.aggregateEvents(
                events = rawEvents,
                trackedApps = currentPrefs.trackedApps,
                zoneId = zoneId,
                nowUtc = now
            )

            dailyAppUsageDao.upsertUsage(dailyAggregates.map { DailyAppUsageEntity.fromDomain(it) })
            true
        } finally {
            refreshMutex.unlock()
        }
    }

    override suspend fun updateTrackedApps(apps: Set<String>) = withContext(ioDispatcher) {
        val current = userPreferencesDao.getPreferences()?.toDomain() 
            ?: UserPreferences()
        val updated = current.copy(trackedApps = apps)
        userPreferencesDao.upsertPreferences(UserPreferencesEntity.fromDomain(updated))
    }

    override suspend fun updateDailyGoal(packageName: String, goalMinutes: Int) = withContext(ioDispatcher) {
        val current = userPreferencesDao.getPreferences()?.toDomain() 
            ?: UserPreferences(trackedApps = defaultTrackedApps)
        val updatedGoals = current.dailyGoalsMinutes.toMutableMap()
        updatedGoals[packageName] = goalMinutes
        val updated = current.copy(dailyGoalsMinutes = updatedGoals)
        userPreferencesDao.upsertPreferences(UserPreferencesEntity.fromDomain(updated))
    }

    override suspend fun setTrackingPaused(untilUtc: Long?) = withContext(ioDispatcher) {
        val current = userPreferencesDao.getPreferences()?.toDomain() 
            ?: UserPreferences(trackedApps = defaultTrackedApps)
        val updated = current.copy(trackingPausedUntilUtc = untilUtc)
        userPreferencesDao.upsertPreferences(UserPreferencesEntity.fromDomain(updated))
    }

    override suspend fun deleteAllUsage() = withContext(ioDispatcher) {
        dailyAppUsageDao.deleteAllUsage()
    }
}
