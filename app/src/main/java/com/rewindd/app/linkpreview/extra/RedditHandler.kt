package com.rewindd.app.linkpreview.extra

import org.json.JSONObject
import java.net.URLEncoder

class RedditHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("reddit.com") || lower.contains("redd.it")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)

        // 1. Try public oEmbed endpoint
        val oembedUrl = "https://www.reddit.com/oembed?url=" + URLEncoder.encode(resolvedUrl, "UTF-8")
        val jsonStr = NetworkHelper.fetchJson(oembedUrl, NetworkHelper.UA_TWITTER)
        if (!jsonStr.isNullOrBlank()) {
            val thumb = parseThumbnail(jsonStr)
            if (thumb != null && NetworkHelper.verifyHttpsImage(thumb)) {
                return thumb
            }
        }

        // 2. Fetch HTML with Twitterbot or Facebook UA
        val html = NetworkHelper.fetchHtmlWithUaRotation(
            resolvedUrl,
            listOf(NetworkHelper.UA_TWITTER, NetworkHelper.UA_FACEBOOK, NetworkHelper.UA_MOBILE_CHROME)
        )
        if (html != null) {
            val metaImage = MetaTagExtractor.extractImageUrl(html, resolvedUrl)
            if (metaImage != null && NetworkHelper.verifyHttpsImage(metaImage)) {
                return metaImage
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

