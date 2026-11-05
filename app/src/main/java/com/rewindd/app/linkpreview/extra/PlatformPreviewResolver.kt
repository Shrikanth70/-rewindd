package com.rewindd.app.linkpreview.extra

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object PlatformPreviewResolver {

    // Thread-safe in-memory cache for resolved URLs
    private val memoryCache = ConcurrentHashMap<String, String>()

    // Registry of platform handlers in evaluation order
    private val handlers: List<PlatformHandler> = listOf(
        PinterestHandler(),
        TwitterHandler(),
        ThreadsHandler(),
        LinkedInHandler(),
        RedditHandler(),
        TikTokHandler(),
        FacebookHandler(),
        VimeoHandler(),
        SpotifyHandler(),
        PopularServicesHandler()
    )

    private val externalFallbackHandler = ExternalFallbackHandler()

    /**
     * Attempts to resolve a cover image for platforms not covered by the legacy extractors.
     * Guaranteed to NEVER throw an exception. Returns null on failure.
     */
    suspend fun resolve(url: String): String? = withContext(Dispatchers.IO) {
        if (url.isBlank()) return@withContext null

        val lower = url.lowercase()

        // Hard rule: Skip YouTube and Instagram entirely as they already work in legacy logic
        if (lower.contains("youtube.com") || lower.contains("youtu.be") ||
            lower.contains("instagram.com") || lower.contains("instagr.am")
        ) {
            return@withContext null
        }

        // Return from in-memory cache if previously fetched
        memoryCache[url]?.let { return@withContext it }

        try {
            // 1. Check registered platform handlers
            for (handler in handlers) {
                if (handler.matches(url)) {
                    val result = handler.fetch(url)
                    if (result != null) {
                        memoryCache[url] = result
                        return@withContext result
                    }
                    // If matched handler returned null, stop or proceed to last resort
                    break
                }
            }

            // 2. Check optional external fallback (e.g. Microlink, off by default)
            if (externalFallbackHandler.matches(url)) {
                val extResult = externalFallbackHandler.fetch(url)
                if (extResult != null) {
                    memoryCache[url] = extResult
                    return@withContext extResult
                }
            }

            // 3. Last resort: Try site's favicon or apple-touch-icon
            val cleanedUrl = NetworkHelper.resolveRedirectsAndClean(url)
            val html = NetworkHelper.fetchHtmlWithUaRotation(cleanedUrl)
            if (html != null) {
                val icon = MetaTagExtractor.extractFaviconOrTouchIcon(html, cleanedUrl)
                if (icon != null && NetworkHelper.verifyHttpsImage(icon)) {
                    memoryCache[url] = icon
                    return@withContext icon
                }
            }
        } catch (_: Throwable) {
            // Guarantee: never throw
            return@withContext null
        }

        null
    }

    /**
     * Clears memory cache (useful for testing).
     */
    fun clearCache() {
        memoryCache.clear()
    }
}

