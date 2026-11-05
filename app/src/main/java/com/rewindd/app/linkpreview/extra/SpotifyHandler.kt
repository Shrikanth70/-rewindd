package com.rewindd.app.linkpreview.extra

import org.json.JSONObject
import java.net.URLEncoder

class SpotifyHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("open.spotify.com") || lower.contains("spotify.link")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)

        // 1. Spotify public oEmbed endpoint
        val oembedUrl = "https://open.spotify.com/oembed?url=" + URLEncoder.encode(resolvedUrl, "UTF-8")
        val jsonStr = NetworkHelper.fetchJson(oembedUrl)
        if (!jsonStr.isNullOrBlank()) {
            val thumb = parseThumbnail(jsonStr)
            if (thumb != null && NetworkHelper.verifyHttpsImage(thumb)) {
                return thumb
            }
        }

        // 2. HTML meta tags with bot UA
        val html = NetworkHelper.fetchHtmlWithUaRotation(
            resolvedUrl,
            listOf(NetworkHelper.UA_FACEBOOK, NetworkHelper.UA_TWITTER, NetworkHelper.UA_MOBILE_CHROME)
        )
        if (html != null) {
            val candidate = MetaTagExtractor.extractImageUrl(html, resolvedUrl)
            if (candidate != null && NetworkHelper.verifyHttpsImage(candidate)) {
                return candidate
            }
        }

        return null
    }

    private fun parseThumbnail(jsonStr: String): String? {
        return try {
            val json = JSONObject(jsonStr)
            val thumb = json.optString("thumbnail_url", "")
            if (thumb.startsWith("https://", ignoreCase = true)) thumb else null
        } catch (_: Exception) {
            null
        }
    }
}

