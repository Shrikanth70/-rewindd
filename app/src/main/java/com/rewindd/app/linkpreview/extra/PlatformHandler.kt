package com.rewindd.app.linkpreview.extra

/**
 * Common interface for platform-specific link preview handlers.
 */
interface PlatformHandler {
    /**
     * Checks if this handler supports the given URL.
     */
    fun matches(url: String): Boolean

    /**
     * Fetches the preview cover image URL for the given link.
     * Must never throw an exception; returns null on failure.
     */
    suspend fun fetch(url: String): String?
}

