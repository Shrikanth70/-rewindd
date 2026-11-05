package com.rewindd.app.linkpreview.extra

import org.json.JSONObject
import java.net.URLEncoder

class ThreadsHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        return url.lowercase().contains("threads.net")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)

        // 1. Fetch HTML with bot UAs
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

        // 2. oEmbed route if available
        val oembedUrl = "https://www.threads.net/oembed?url=" + URLEncoder.encode(resolvedUrl, "UTF-8")
        val jsonStr = NetworkHelper.fetchJson(oembedUrl, NetworkHelper.UA_FACEBOOK)
        if (!jsonStr.isNullOrBlank()) {
            val thumb = parseThumbnail(jsonStr)
            if (thumb != null && NetworkHelper.verifyHttpsImage(thumb)) {
                return thumb
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

