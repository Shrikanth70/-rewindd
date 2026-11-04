package com.rewindd.app.navigation

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigationArchitectureTest {

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        navController = NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "home") {
                composable("home") {}
                composable("rewind") {}
                composable("library") {}
                composable("settings") {}
                composable("library/collection/{id}") {}
                composable("library/item/{id}") {}
            }
        }
    }

    @Test
    fun testHomeToLibraryViaTopLevelNavigation() {
        assertEquals("home", navController.currentDestination?.route)

        // Action: Tap Library
        navController.navigateToTopLevelDestination("library")
        assertEquals("library", navController.currentDestination?.route)
    }

    @Test
    fun testCuratedCollectionsViewAllToLibraryThenTapHome() {
        assertEquals("home", navController.currentDestination?.route)

        // Action: View All opens Library via navigateToTopLevelDestination
        navController.navigateToTopLevelDestination("library")
        assertEquals("library", navController.currentDestination?.route)

        // Action: Tap Home in bottom navigation
        navController.navigateToTopLevelDestination("home")
        assertEquals("home", navController.currentDestination?.route)
    }

    @Test
    fun testRewindToHome() {
        navController.navigateToTopLevelDestination("rewind")
        assertEquals("rewind", navController.currentDestination?.route)

        navController.navigateToTopLevelDestination("home")
        assertEquals("home", navController.currentDestination?.route)
    }

    @Test
    fun testSettingsReturnToPrimaryNavigation() {
        navController.navigateToTopLevelDestination("home")
        navController.navigate("settings")
        assertEquals("settings", navController.currentDestination?.route)

        // Return to primary navigation via popBackStack
        navController.popBackStack()
        assertEquals("home", navController.currentDestination?.route)
    }

    @Test
    fun testSecondaryRouteSystemBack() {
        navController.navigateToTopLevelDestination("library")
        navController.navigate("library/collection/col-123")
        assertEquals("library/collection/{id}", navController.currentDestination?.route)

        // System back
        navController.popBackStack()
        assertEquals("library", navController.currentDestination?.route)
    }

    @Test
    fun testSwitchingBetweenPrimaryTabsDoesNotAccumulateDuplicates() {
        val tabs = listOf("home", "rewind", "library")
        repeat(5) {
            for (tab in tabs) {
                navController.navigateToTopLevelDestination(tab)
                assertEquals(tab, navController.currentDestination?.route)
            }
        }
        // Final destination is library
        assertEquals("library", navController.currentDestination?.route)

        // System back should pop directly to home start destination
        navController.popBackStack()
        assertEquals("home", navController.currentDestination?.route)
    }
}
