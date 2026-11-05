package com.rewindd.app.linkpreview.extra

import org.jsoup.Jsoup
import java.net.URI

object MetaTagExtractor {

    /**
     * Extracts preview cover image in exact priority order:
     * 1. og:image:secure_url
     * 2. og:image
     * 3. twitter:image
     * 4. twitter:image:src
     * 5. link rel="image_src"
     *
     * Converts relative URLs to absolute https URLs.
     */
    fun extractImageUrl(html: String, baseUrl: String): String? {
        if (NetworkHelper.isLoginWall(html)) return null

        val doc = try {
            Jsoup.parse(html, baseUrl)
        } catch (_: Exception) {
            return null
        }

        // 1. og:image:secure_url
        val ogSecure = resolveCandidate(doc.select("meta[property=og:image:secure_url]").attr("abs:content"), baseUrl)
        if (ogSecure != null) return ogSecure

        // 2. og:image
        val ogImage = resolveCandidate(doc.select("meta[property=og:image]").attr("abs:content"), baseUrl)
        if (ogImage != null) return ogImage

        // 3. twitter:image
        val twitterImage = resolveCandidate(doc.select("meta[name=twitter:image]").attr("abs:content"), baseUrl)
        if (twitterImage != null) return twitterImage

        // 4. twitter:image:src
        val twitterImageSrc = resolveCandidate(doc.select("meta[name=twitter:image:src]").attr("abs:content"), baseUrl)
        if (twitterImageSrc != null) return twitterImageSrc

        // 5. link rel="image_src"
        val linkImage = resolveCandidate(doc.select("link[rel=image_src]").attr("abs:href"), baseUrl)
        if (linkImage != null) return linkImage

        return null
    }

    /**
     * Extracts favicon or apple-touch-icon as a last resort.
     */
    fun extractFaviconOrTouchIcon(html: String, baseUrl: String): String? {
        val doc = try {
            Jsoup.parse(html, baseUrl)
        } catch (_: Exception) {
            return null
        }

        // 1. apple-touch-icon
        val appleTouch = resolveCandidate(doc.select("link[rel~=(?i)apple-touch-icon]").attr("abs:href"), baseUrl)
        if (appleTouch != null) return appleTouch

        // 2. icon / shortcut icon
        val icon = resolveCandidate(doc.select("link[rel~=(?i)icon]").attr("abs:href"), baseUrl)
        if (icon != null) return icon

        // 3. Fallback to /favicon.ico on domain
        return try {
            val uri = URI(baseUrl)
            if (uri.host != null && uri.scheme == "https") {
                "https://${uri.host}/favicon.ico"
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun resolveCandidate(candidate: String, baseUrl: String): String? {
        val trimmed = candidate.trim()
        if (trimmed.isEmpty()) return null

        val fullUrl = try {
            if (trimmed.startsWith("//")) {
                "https:$trimmed"
            } else if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                trimmed
            } else {
                URI(baseUrl).resolve(trimmed).toString()
            }
        } catch (_: Exception) {
            null
        } ?: return null

        // Require https
        return if (fullUrl.startsWith("https://", ignoreCase = true) && fullUrl.length > 8) {
            fullUrl
        } else {
            null
        }
    }
}

