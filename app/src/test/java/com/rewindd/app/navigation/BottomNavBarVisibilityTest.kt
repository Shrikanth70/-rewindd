package com.rewindd.app.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BottomNavBarVisibilityTest {

    @Test
    fun shouldShowBottomBar_returnsTrue_forRewindRoute() {
        assertTrue(
            "BottomNavBar must be visible on the rewind review screen",
            NavigationRules.shouldShowBottomBar("rewind")
        )
    }

    @Test
    fun shouldShowBottomBar_returnsTrue_forTopLevelRoutes() {
        assertTrue(NavigationRules.shouldShowBottomBar("home"))
        assertTrue(NavigationRules.shouldShowBottomBar("library"))
        assertTrue(NavigationRules.shouldShowBottomBar("rewind"))
    }

    @Test
    fun shouldShowBottomBar_returnsFalse_forOnboardingRoutes() {
        assertFalse(NavigationRules.shouldShowBottomBar("onboarding/tour"))
        assertFalse(NavigationRules.shouldShowBottomBar("onboarding/welcome"))
        assertFalse(NavigationRules.shouldShowBottomBar("onboarding/choose_apps"))
        assertFalse(NavigationRules.shouldShowBottomBar("onboarding/permissions"))
    }

    @Test
    fun shouldShowBottomBar_returnsFalse_forNullOrEmptyRoute() {
        assertFalse(NavigationRules.shouldShowBottomBar(null))
        assertFalse(NavigationRules.shouldShowBottomBar(""))
    }

    @Test
    fun shouldShowBottomBar_returnsFalse_forDetailAndSubScreens() {
        assertFalse(NavigationRules.shouldShowBottomBar("library/collection/col_123"))
        assertFalse(NavigationRules.shouldShowBottomBar("library/item/item_456"))
        assertFalse(NavigationRules.shouldShowBottomBar("insights/app_detail/com.instagram.android"))
        assertFalse(NavigationRules.shouldShowBottomBar("settings"))
        assertFalse(NavigationRules.shouldShowBottomBar("item_detail/item_456"))
        assertFalse(NavigationRules.shouldShowBottomBar("edit_item/item_456"))
    }
}
