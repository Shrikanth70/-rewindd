package com.rewindd.app.navigation

/**
 * Shared routing and navigation bar rules for the Rewindd application.
 */
object NavigationRules {
    private val TOP_LEVEL_ROUTES = setOf("home", "library", "rewind")

    /**
     * Determines whether the global BottomNavBar should be displayed for a given route.
     * All primary app screens (Home, Rewind review deck, Library, Insights) display the bottom navigation bar.
     * Sub-screens (CollectionDetail, AppUsageDetail, Settings, Edit/ItemDetail) and Onboarding flows hide the bar.
     */
    fun shouldShowBottomBar(route: String?): Boolean {
        if (route.isNullOrBlank()) return false
        return route in TOP_LEVEL_ROUTES
    }
}
