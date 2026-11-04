package com.rewindd.app.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Navigates to a top-level primary destination (Home, Rewind, Library, Insights)
 * following standard Jetpack Compose Navigation guidelines:
 * - Pops up to the start destination of the graph to avoid accumulating duplicate screens
 * - Preserves saved state and scroll position across tab switching
 * - Sets launchSingleTop to prevent duplicate instances of the same destination
 */
fun NavHostController.navigateToTopLevelDestination(route: String) {
    navigate(route) {
        val startDestId = try {
            graph.findStartDestination().id
        } catch (_: Exception) {
            null
        }
        if (startDestId != null) {
            popUpTo(startDestId) {
                saveState = true
            }
        } else {
            popUpTo("home") {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
