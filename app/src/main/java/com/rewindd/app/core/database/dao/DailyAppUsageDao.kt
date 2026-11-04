package com.rewindd.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rewindd.app.core.database.entity.DailyAppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyAppUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUsage(items: List<DailyAppUsageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSingle(item: DailyAppUsageEntity)

    @Query("SELECT * FROM daily_app_usage WHERE dateLocal BETWEEN :startDate AND :endDate ORDER BY dateLocal ASC, usageMillis DESC")
    fun getUsageForDateRangeFlow(startDate: String, endDate: String): Flow<List<DailyAppUsageEntity>>

    @Query("SELECT * FROM daily_app_usage WHERE packageName = :packageName AND dateLocal BETWEEN :startDate AND :endDate ORDER BY dateLocal ASC")
    fun getUsageForPackageFlow(packageName: String, startDate: String, endDate: String): Flow<List<DailyAppUsageEntity>>

    @Query("SELECT COUNT(DISTINCT dateLocal) FROM daily_app_usage WHERE usageMillis > 0")
    fun getDistinctTrackedDaysCountFlow(): Flow<Int>

    @Query("SELECT * FROM daily_app_usage WHERE dateLocal = :dateLocal")
    suspend fun getUsageForDate(dateLocal: String): List<DailyAppUsageEntity>

    @Query("DELETE FROM daily_app_usage WHERE dateLocal < :cutoffDate")
    suspend fun deleteOldUsage(cutoffDate: String)

    @Query("DELETE FROM daily_app_usage")
    suspend fun deleteAllUsage()
}
