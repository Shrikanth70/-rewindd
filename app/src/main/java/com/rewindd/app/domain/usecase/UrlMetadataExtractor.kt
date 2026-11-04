package com.rewindd.app.domain.usecase

import com.rewindd.app.domain.model.SavedMediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.regex.Pattern

object UrlMetadataExtractor {

    private val YOUTUBE_PATTERN = Pattern.compile(
        "(?:youtube\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|.*[?&]v=|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})"
    )

    fun classifyMediaType(url: String): SavedMediaType {
        val lower = url.lowercase()
        return when {
            lower.contains("youtube.com") || lower.contains("youtu.be") -> SavedMediaType.YOUTUBE_VIDEO
            lower.contains("instagram.com/reel") || lower.contains("instagram.com/p/") || lower.contains("instagr.am") -> SavedMediaType.INSTAGRAM_REEL
            lower.contains("substack.com") || lower.contains("arxiv.org") || lower.contains("medium.com") -> SavedMediaType.WEB_ARTICLE
            else -> SavedMediaType.GENERIC_LINK
        }
    }

    private val OG_IMAGE_PATTERN = Pattern.compile(
        """<meta[^>]+property=["']og:image["'][^>]+content=["']([^"']+)["']""",
        Pattern.CASE_INSENSITIVE
    )

    fun extractYouTubeThumbnail(url: String): String? {
        val matcher = YOUTUBE_PATTERN.matcher(url)
        return if (matcher.find()) {
            val videoId = matcher.group(1)
            "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
        } else {
            null
        }
    }

    fun isSafeUrl(urlStr: String): Boolean {
        return try {
            val uri = URI(urlStr)
            val scheme = uri.scheme?.lowercase()
            if (scheme != "http" && scheme != "https") return false
            val host = uri.host?.lowercase() ?: return false
            if (host == "localhost" || host == "127.0.0.1" || host == "::1" || host == "0.0.0.0") return false
            if (host.startsWith("127.") || host.startsWith("10.") || host.startsWith("192.168.") || host.startsWith("169.254.")) return false
            
            // Check RFC 1918 172.16.0.0/12 (172.16.x.x - 172.31.x.x)
            if (host.startsWith("172.")) {
                val parts = host.split(".")
                if (parts.size >= 2) {
                    val secondOctet = parts[1].toIntOrNull()
                    if (secondOctet != null && secondOctet in 16..31) return false
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun extractMetadata(urlStr: String): Pair<String?, String?> = withContext(Dispatchers.IO) {
        val youtubeThumb = extractYouTubeThumbnail(urlStr)
        if (youtubeThumb != null) {
            return@withContext Pair(null, youtubeThumb)
        }

        if (!isSafeUrl(urlStr)) {
            return@withContext Pair(null, null)
        }

        try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.instanceFollowRedirects = true
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/114.0 Firefox/114.0")

            if (connection.responseCode in 200..299) {
                val reader = connection.inputStream.bufferedReader()
                val sb = StringBuilder()
                val buffer = CharArray(1024)
                var bytesRead = 0
                // Read at most 64KB
                while (reader.read(buffer).also { bytesRead = it } != -1 && sb.length < 65536) {
                    sb.append(buffer, 0, bytesRead)
                }
                reader.close()
                val html = sb.toString()

                val ogMatcher = OG_IMAGE_PATTERN.matcher(html)
                val rawOgImage = if (ogMatcher.find()) ogMatcher.group(1)?.trim() else null
                val safeOgImage = if (rawOgImage != null && isSafeUrl(rawOgImage)) rawOgImage else null
                return@withContext Pair(null, safeOgImage)
            }
        } catch (e: Exception) {
            // Graceful fallback on network or parsing error
        }
        Pair(null, null)
    }
}
