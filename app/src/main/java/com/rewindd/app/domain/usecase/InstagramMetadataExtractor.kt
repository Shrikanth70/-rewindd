package com.rewindd.app.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.util.regex.Pattern

data class InstagramMetadata(
    val thumbnailUrl: String?,
    val title: String?,
    val rawCaption: String?
)

object InstagramMetadataExtractor {

    private const val CRAWLER_USER_AGENT = "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)"
    private const val CONNECT_TIMEOUT_MS = 3500
    private const val READ_TIMEOUT_MS = 4000
    private const val MAX_HTML_CHARS = 131072 // 128 KB
    private const val MAX_REDIRECT_HOPS = 3

    private val OG_IMAGE_PATTERN = Pattern.compile(
        """<meta[^>]+(?:property|name)=["'](?:og:image|twitter:image)["'][^>]+content=["']([^"']+)["']""",
        Pattern.CASE_INSENSITIVE
    )

    private val OG_IMAGE_CONTENT_FIRST_PATTERN = Pattern.compile(
        """<meta[^>]+content=["']([^"']+)["'][^>]+(?:property|name)=["'](?:og:image|twitter:image)["']""",
        Pattern.CASE_INSENSITIVE
    )

    private val OG_TITLE_PATTERN = Pattern.compile(
        """<meta[^>]+(?:property|name)=["'](?:og:title|twitter:title)["'][^>]+content=["']([^"']+)["']""",
        Pattern.CASE_INSENSITIVE
    )

    private val OG_TITLE_CONTENT_FIRST_PATTERN = Pattern.compile(
        """<meta[^>]+content=["']([^"']+)["'][^>]+(?:property|name)=["'](?:og:title|twitter:title)["']""",
        Pattern.CASE_INSENSITIVE
    )

    private val OG_DESC_PATTERN = Pattern.compile(
        """<meta[^>]+(?:property|name)=["'](?:og:description|description)["'][^>]+content=["']([^"']+)["']""",
        Pattern.CASE_INSENSITIVE
    )

    fun isInstagramUrl(urlStr: String): Boolean {
        return try {
            val uri = URI(urlStr)
            val host = uri.host?.lowercase() ?: return false
            val isHostValid = host == "instagram.com" ||
                    host.endsWith(".instagram.com") ||
                    host == "instagr.am" ||
                    host.endsWith(".instagr.am")
            if (!isHostValid) return false

            val path = uri.path?.lowercase() ?: ""
            path.contains("/reel/") || path.contains("/p/") || path.contains("/tv/")
        } catch (e: Exception) {
            false
        }
    }

    fun isSafeDestination(uri: URI): Boolean {
        return try {
            val scheme = uri.scheme?.lowercase() ?: return false
            if (scheme != "http" && scheme != "https") return false

            val host = uri.host?.lowercase() ?: return false
            val isHostAllowed = host == "instagram.com" ||
                    host.endsWith(".instagram.com") ||
                    host == "instagr.am" ||
                    host.endsWith(".instagr.am") ||
                    host.endsWith(".cdninstagram.com")
            if (!isHostAllowed) return false

            val addresses = InetAddress.getAllByName(host)
            for (addr in addresses) {
                if (addr.isLoopbackAddress ||
                    addr.isAnyLocalAddress ||
                    addr.isLinkLocalAddress ||
                    addr.isSiteLocalAddress ||
                    addr.isMulticastAddress
                ) {
                    return false
                }
                val hostAddr = addr.hostAddress ?: ""
                if (hostAddr.startsWith("127.") ||
                    hostAddr.startsWith("10.") ||
                    hostAddr.startsWith("192.168.") ||
                    hostAddr.startsWith("169.254.") ||
                    hostAddr == "::1" ||
                    hostAddr.startsWith("fe80:")
                ) {
                    return false
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun unescapeHtmlEntities(text: String): String {
        return com.rewindd.app.domain.model.cleanHtmlEntities(text)
    }

    fun parseHtmlMetadata(html: String): InstagramMetadata {
        var rawThumb: String? = null
        val imgMatcher = OG_IMAGE_PATTERN.matcher(html)
        if (imgMatcher.find()) {
            rawThumb = imgMatcher.group(1)
        } else {
            val imgRevMatcher = OG_IMAGE_CONTENT_FIRST_PATTERN.matcher(html)
            if (imgRevMatcher.find()) {
                rawThumb = imgRevMatcher.group(1)
            }
        }

        var rawTitle: String? = null
        val titleMatcher = OG_TITLE_PATTERN.matcher(html)
        if (titleMatcher.find()) {
            rawTitle = titleMatcher.group(1)
        } else {
            val titleRevMatcher = OG_TITLE_CONTENT_FIRST_PATTERN.matcher(html)
            if (titleRevMatcher.find()) {
                rawTitle = titleRevMatcher.group(1)
            }
        }

        var rawDesc: String? = null
        val descMatcher = OG_DESC_PATTERN.matcher(html)
        if (descMatcher.find()) {
            rawDesc = descMatcher.group(1)
        }

        val unescapedThumb = rawThumb?.let { unescapeHtmlEntities(it) }
        val unescapedTitle = rawTitle?.let { unescapeHtmlEntities(it) }
        val unescapedDesc = rawDesc?.let { unescapeHtmlEntities(it) }

        return InstagramMetadata(
            thumbnailUrl = unescapedThumb,
            title = unescapedTitle,
            rawCaption = unescapedDesc
        )
    }

    // Visible for testing / mock injection
    internal var metadataFetcher: (suspend (String) -> InstagramMetadata?)? = null

    suspend fun extract(urlStr: String): InstagramMetadata? = withContext(Dispatchers.IO) {
        val customFetcher = metadataFetcher
        if (customFetcher != null) {
            return@withContext customFetcher(urlStr)
        }

        if (!isInstagramUrl(urlStr)) return@withContext null

        var currentUrl = urlStr
        var hops = 0

        while (hops < MAX_REDIRECT_HOPS) {
            val uri = try {
                URI(currentUrl)
            } catch (e: Exception) {
                return@withContext null
            }

            if (!isSafeDestination(uri)) {
                return@withContext null
            }

            var connection: HttpURLConnection? = null
            try {
                val url = URL(currentUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = CONNECT_TIMEOUT_MS
                connection.readTimeout = READ_TIMEOUT_MS
                connection.instanceFollowRedirects = false
                connection.setRequestProperty("User-Agent", CRAWLER_USER_AGENT)
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")

                val code = connection.responseCode
                if (code in 300..399) {
                    val location = connection.getHeaderField("Location") ?: return@withContext null
                    currentUrl = if (location.startsWith("http://") || location.startsWith("https://")) {
                        location
                    } else {
                        uri.resolve(location).toString()
                    }
                    hops++
                    continue
                }

                if (code in 200..299) {
                    val reader = connection.inputStream.bufferedReader()
                    val sb = StringBuilder()
                    val buffer = CharArray(1024)
                    var bytesRead = 0
                    while (reader.read(buffer).also { bytesRead = it } != -1 && sb.length < MAX_HTML_CHARS) {
                        sb.append(buffer, 0, bytesRead)
                    }
                    reader.close()
                    val html = sb.toString()
                    return@withContext parseHtmlMetadata(html)
                }

                return@withContext null
            } catch (e: Exception) {
                return@withContext null
            } finally {
                connection?.disconnect()
            }
        }

        null
    }
}
