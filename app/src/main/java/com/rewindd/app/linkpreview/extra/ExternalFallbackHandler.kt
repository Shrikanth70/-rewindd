package com.rewindd.app.linkpreview.extra

import org.json.JSONObject
import java.net.URLEncoder

object ExternalFallbackConfig {
    var isEnabled: Boolean = false
    var apiKey: String? = null
}

class ExternalFallbackHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        // Active only when feature flag is enabled and an API key is provided
        return ExternalFallbackConfig.isEnabled && !ExternalFallbackConfig.apiKey.isNullOrBlank()
    }

    override suspend fun fetch(url: String): String? {
        if (!ExternalFallbackConfig.isEnabled) return null
        val apiKey = ExternalFallbackConfig.apiKey ?: return null

        return try {
            val endpoint = "https://api.microlink.io?url=" + URLEncoder.encode(url, "UTF-8")
            val jsonStr = NetworkHelper.fetchJson(endpoint) ?: return null
            val json = JSONObject(jsonStr)
            val data = json.optJSONObject("data") ?: return null
            val imageObj = data.optJSONObject("image") ?: return null
            val imageUrl = imageObj.optString("url", "")
            if (imageUrl.startsWith("https://", ignoreCase = true) && NetworkHelper.verifyHttpsImage(imageUrl)) {
                imageUrl
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}

