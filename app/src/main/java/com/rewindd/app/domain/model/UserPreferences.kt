package com.rewindd.app.domain.model

data class UserPreferences(
    val onboardingComplete: Boolean = false,
    val trackedApps: Set<String> = emptySet(),
    val dailyGoalsMinutes: Map<String, Int> = emptyMap(),
    val trackingPausedUntilUtc: Long? = null,
    val retentionMonths: Int = 12,
    val remindersEnabled: Boolean = false
) {
    fun isTrackingPaused(nowUtc: Long = System.currentTimeMillis()): Boolean {
        return trackingPausedUntilUtc != null && trackingPausedUntilUtc > nowUtc
    }
}
