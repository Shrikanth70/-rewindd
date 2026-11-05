package com.rewindd.app.linkpreview.extra

import org.json.JSONObject
import java.net.URLEncoder

class TwitterHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("twitter.com") || lower.contains("x.com") || lower.contains("t.co")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)

        // 1. Try public oEmbed endpoint
        val oembedUrl = "https://publish.twitter.com/oembed?url=" + URLEncoder.encode(resolvedUrl, "UTF-8") + "&omit_script=true"
        val jsonStr = NetworkHelper.fetchJson(oembedUrl, NetworkHelper.UA_TWITTER)
        if (!jsonStr.isNullOrBlank()) {
            val imageFromEmbed = parseTwitterOEmbedHtml(jsonStr)
            if (imageFromEmbed != null && NetworkHelper.verifyHttpsImage(imageFromEmbed)) {
                return imageFromEmbed
            }
        }

        // 2. Fetch HTML with Twitterbot/1.0
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

    private fun parseTwitterOEmbedHtml(jsonStr: String): String? {
        return try {
            val json = JSONObject(jsonStr)
            val html = json.optString("html", "")
            if (html.isNotEmpty()) {
                val doc = org.jsoup.Jsoup.parse(html)
                val img = doc.select("img").attr("src")
                if (img.startsWith("https://", ignoreCase = true)) {
                    return img
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }
}

