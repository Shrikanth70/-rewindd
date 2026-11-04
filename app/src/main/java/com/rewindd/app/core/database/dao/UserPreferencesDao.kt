package com.rewindd.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rewindd.app.core.database.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {

    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getPreferencesFlow(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    suspend fun getPreferences(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPreferences(entity: UserPreferencesEntity)

    @Query("UPDATE user_preferences SET trackingPausedUntilUtc = :untilUtc WHERE id = 1")
    suspend fun setTrackingPaused(untilUtc: Long?)

    @Query("UPDATE user_preferences SET trackedAppsJson = :appsJson WHERE id = 1")
    suspend fun updateTrackedApps(appsJson: String)

    @Query("UPDATE user_preferences SET dailyGoalsJson = :goalsJson WHERE id = 1")
    suspend fun updateDailyGoals(goalsJson: String)
}
