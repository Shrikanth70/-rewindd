package com.rewindd.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rewindd.app.domain.model.UserPreferences
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val onboardingComplete: Boolean,
    val trackedAppsJson: String,
    val dailyGoalsJson: String,
    val trackingPausedUntilUtc: Long?,
    val retentionMonths: Int,
    val remindersEnabled: Boolean
) {
    fun toDomain(): UserPreferences {
        val apps = mutableSetOf<String>()
        runCatching {
            val jsonArray = JSONArray(trackedAppsJson)
            for (i in 0 until jsonArray.length()) {
                apps.add(jsonArray.getString(i))
            }
        }

        val goals = mutableMapOf<String, Int>()
        runCatching {
            val jsonObj = JSONObject(dailyGoalsJson)
            val keys = jsonObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                goals[key] = jsonObj.getInt(key)
            }
        }

        return UserPreferences(
            onboardingComplete = onboardingComplete,
            trackedApps = apps,
            dailyGoalsMinutes = goals,
            trackingPausedUntilUtc = trackingPausedUntilUtc,
            retentionMonths = retentionMonths,
            remindersEnabled = remindersEnabled
        )
    }

    companion object {
        fun fromDomain(domain: UserPreferences): UserPreferencesEntity {
            val appsArray = JSONArray()
            domain.trackedApps.forEach { appsArray.put(it) }

            val goalsObj = JSONObject()
            domain.dailyGoalsMinutes.forEach { (pkg, mins) -> goalsObj.put(pkg, mins) }

            return UserPreferencesEntity(
                id = 1,
                onboardingComplete = domain.onboardingComplete,
                trackedAppsJson = appsArray.toString(),
                dailyGoalsJson = goalsObj.toString(),
                trackingPausedUntilUtc = domain.trackingPausedUntilUtc,
                retentionMonths = domain.retentionMonths,
                remindersEnabled = domain.remindersEnabled
            )
        }
    }
}
