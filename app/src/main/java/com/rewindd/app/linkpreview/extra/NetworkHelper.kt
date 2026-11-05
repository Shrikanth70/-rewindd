package com.rewindd.app.linkpreview.extra

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI
import java.util.concurrent.TimeUnit

object NetworkHelper {

    const val UA_FACEBOOK = "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)"
    const val UA_TWITTER = "Twitterbot/1.0"
    const val UA_MOBILE_CHROME = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"

    val USER_AGENTS_ORDER = listOf(
        UA_FACEBOOK,
        UA_TWITTER,
        UA_MOBILE_CHROME
    )

    private const val MAX_HTML_SIZE_BYTES = 128 * 1024 // 128 KB
    private const val CONNECT_TIMEOUT_SEC = 8L
    private const val READ_TIMEOUT_SEC = 8L

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    /**
     * Resolves short-links and redirects, then strips tracking query parameters.
     */
    suspend fun resolveRedirectsAndClean(urlStr: String): String = withContext(Dispatchers.IO) {
        val cleaned = stripTrackingParameters(urlStr)
        try {
            val request = Request.Builder()
                .url(cleaned)
                .head()
                .header("User-Agent", UA_FACEBOOK)
                .build()
            client.newCall(request).execute().use { response ->
                val finalUrl = response.request.url.toString()
                stripTrackingParameters(finalUrl)
            }
        } catch (_: Exception) {
            cleaned
        }
    }

    /**
     * Strips common tracking parameters like utm_*, fbclid, gclid, igshid, si, ref, etc.
     */
    fun stripTrackingParameters(urlStr: String): String {
        return try {
            val uri = URI(urlStr)
            val query = uri.query ?: return urlStr
            val trackingKeys = setOf(
                "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
                "fbclid", "gclid", "igshid", "si", "ref", "ref_src", "ref_url",
                "source", "campaign", "feature"
            )
            val newQuery = query.split("&")
                .filterNot { param ->
                    val key = param.substringBefore("=").lowercase()
                    trackingKeys.contains(key) || key.startsWith("utm_")
                }
                .joinToString("&")

            val base = uri.scheme + "://" + uri.authority + uri.path
            val q = if (newQuery.isNotBlank()) "?$newQuery" else ""
            val f = if (!uri.fragment.isNullOrBlank()) "#${uri.fragment}" else ""
            base + q + f
        } catch (_: Exception) {
            urlStr
        }
    }

    /**
     * Fetches HTML text up to 128KB with retry and backoff.
     * Treats 401, 403, 429 as failure and returns null.
     */
    suspend fun fetchHtmlWithUaRotation(
        urlStr: String,
        userAgents: List<String> = USER_AGENTS_ORDER
    ): String? = withContext(Dispatchers.IO) {
        for (ua in userAgents) {
            val html = fetchHtmlWithRetry(urlStr, ua)
            if (html != null && !isLoginWall(html)) {
                return@withContext html
            }
        }
        null
    }

    /**
     * Fetches HTML with up to 2 retries and exponential backoff.
     */
    suspend fun fetchHtmlWithRetry(urlStr: String, userAgent: String): String? = withContext(Dispatchers.IO) {
        var backoffMs = 400L
        for (attempt in 0..2) {
            try {
                val request = Request.Builder()
                    .url(urlStr)
                    .header("User-Agent", userAgent)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .build()

                client.newCall(request).execute().use { response ->
                    val code = response.code
                    // Treat 401, 403, 429 as permanent failure for this request
                    if (code == 401 || code == 403 || code == 429 || code == 999) {
                        return@withContext null
                    }
                    if (!response.isSuccessful) {
                        return@use null
                    }
                    val body = response.body ?: return@use null
                    val source = body.source()
                    val bytes = source.readByteArray(MAX_HTML_SIZE_BYTES.toLong().coerceAtMost(body.contentLength().let { if (it > 0) it else MAX_HTML_SIZE_BYTES.toLong() }))
                    return@withContext String(bytes, Charsets.UTF_8)
                }
            } catch (e: Exception) {
                if (attempt < 2) {
                    delay(backoffMs)
                    backoffMs *= 2
                }
            }
        }
        null
    }

    /**
     * Fetches JSON from an endpoint (e.g. oEmbed).
     */
    suspend fun fetchJson(urlStr: String, userAgent: String = UA_FACEBOOK): String? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(urlStr)
                .header("User-Agent", userAgent)
                .header("Accept", "application/json")
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Verifies that the URL is https and returns a 2xx image response.
     */
    suspend fun verifyHttpsImage(urlStr: String?): Boolean = withContext(Dispatchers.IO) {
        if (urlStr.isNullOrBlank()) return@withContext false
        if (!urlStr.startsWith("https://", ignoreCase = true)) return@withContext false

        try {
            val request = Request.Builder()
                .url(urlStr)
                .head()
                .header("User-Agent", UA_FACEBOOK)
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val contentType = response.header("Content-Type")?.lowercase()
                    // If Content-Type is provided, ensure it's an image or binary stream
                    if (contentType == null || contentType.startsWith("image/") || contentType.contains("octet-stream")) {
                        return@withContext true
                    }
                }
            }
            // Some CDNs reject HEAD; fallback to a small GET request
            val getRequest = Request.Builder()
                .url(urlStr)
                .header("User-Agent", UA_FACEBOOK)
                .header("Range", "bytes=0-1024")
                .build()
            client.newCall(getRequest).execute().use { response ->
                response.isSuccessful
            }
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Checks if HTML content represents a login wall or captcha gate.
     */
    fun isLoginWall(html: String): Boolean {
        val lower = html.lowercase()
        return lower.contains("<title>log in") ||
                lower.contains("<title>sign in") ||
                lower.contains("<title>just a moment") ||
                lower.contains("<title>attention required") ||
                lower.contains("<title>security check") ||
                lower.contains("class=\"authwall") ||
                lower.contains("id=\"authwall")
    }
}

