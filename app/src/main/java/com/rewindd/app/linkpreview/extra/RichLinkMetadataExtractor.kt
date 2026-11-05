package com.rewindd.app.linkpreview.extra

import com.rewindd.app.domain.usecase.InstagramMetadataExtractor
import com.rewindd.app.domain.usecase.UrlMetadataExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URI
import java.net.URLEncoder

data class RichMetadata(
    val title: String? = null,
    val description: String? = null,
    val siteName: String? = null,
    val authorName: String? = null,
    val thumbnailUri: String? = null
)

object RichLinkMetadataExtractor {

    /**
     * Extracts rich metadata (title, description, site name, author/channel, thumbnail)
     * using the exact hierarchy specified:
     * 1. Public oEmbed endpoints where available (YouTube, Vimeo, Reddit, TikTok, X).
     * 2. OpenGraph / Twitter meta tags / HTML title & description.
     * 3. Fallback to domain name.
     * Guaranteed to NEVER throw an exception.
     */
    suspend fun extract(urlStr: String): RichMetadata = withContext(Dispatchers.IO) {
        if (urlStr.isBlank()) return@withContext RichMetadata()

        val lower = urlStr.lowercase()
        val cleanedUrl = NetworkHelper.resolveRedirectsAndClean(urlStr)
        val domain = extractDomain(cleanedUrl)

        try {
            // 1. YouTube specialized extraction
            if (lower.contains("youtube.com") || lower.contains("youtu.be")) {
                val ytThumb = UrlMetadataExtractor.extractYouTubeThumbnail(urlStr)
                val oembed = fetchOEmbed("https://www.youtube.com/oembed?url=" + URLEncoder.encode(cleanedUrl, "UTF-8") + "&format=json")
                if (oembed != null) {
                    val title = cleanText(oembed.optString("title"))
                    val author = cleanText(oembed.optString("author_name"))
                    val thumb = oembed.optString("thumbnail_url").takeIf { it.startsWith("https://") } ?: ytThumb
                    return@withContext RichMetadata(
                        title = title ?: domain,
                        description = if (author != null) "Video on YouTube by $author" else "Video on YouTube",
                        siteName = "YouTube",
                        authorName = author,
                        thumbnailUri = thumb
                    )
                }
                return@withContext RichMetadata(
                    title = domain,
                    description = "YouTube video",
                    siteName = "YouTube",
                    thumbnailUri = ytThumb
                )
            }

            // 2. Instagram specialized extraction
            if (InstagramMetadataExtractor.isInstagramUrl(urlStr)) {
                val insta = InstagramMetadataExtractor.extract(urlStr)
                if (insta != null) {
                    val title = cleanText(insta.title)
                    val desc = cleanText(insta.rawCaption)
                    return@withContext RichMetadata(
                        title = title ?: domain,
                        description = desc,
                        siteName = "Instagram",
                        authorName = null,
                        thumbnailUri = insta.thumbnailUrl
                    )
                }
            }

            // 3. Reddit oEmbed
            if (lower.contains("reddit.com") || lower.contains("redd.it")) {
                val oembed = fetchOEmbed("https://www.reddit.com/oembed?url=" + URLEncoder.encode(cleanedUrl, "UTF-8"))
                if (oembed != null) {
                    val title = cleanText(oembed.optString("title"))
                    val author = cleanText(oembed.optString("author_name"))
                    val thumb = oembed.optString("thumbnail_url").takeIf { it.startsWith("https://") }
                    if (!title.isNullOrBlank()) {
                        return@withContext RichMetadata(
                            title = title,
                            description = if (author != null) "Post on Reddit by $author" else "Post on Reddit",
                            siteName = "Reddit",
                            authorName = author,
                            thumbnailUri = thumb ?: PlatformPreviewResolver.resolve(urlStr)
                        )
                    }
                }
            }

            // 4. TikTok oEmbed
            if (lower.contains("tiktok.com")) {
                val oembed = fetchOEmbed("https://www.tiktok.com/oembed?url=" + URLEncoder.encode(cleanedUrl, "UTF-8"))
                if (oembed != null) {
                    val title = cleanText(oembed.optString("title"))
                    val author = cleanText(oembed.optString("author_name"))
                    val thumb = oembed.optString("thumbnail_url").takeIf { it.startsWith("https://") }
                    if (!title.isNullOrBlank()) {
                        return@withContext RichMetadata(
                            title = title,
                            description = if (author != null) "TikTok by $author" else "TikTok video",
                            siteName = "TikTok",
                            authorName = author,
                            thumbnailUri = thumb ?: PlatformPreviewResolver.resolve(urlStr)
                        )
                    }
                }
            }

            // 5. Vimeo oEmbed
            if (lower.contains("vimeo.com")) {
                val oembed = fetchOEmbed("https://vimeo.com/api/oembed.json?url=" + URLEncoder.encode(cleanedUrl, "UTF-8"))
                if (oembed != null) {
                    val title = cleanText(oembed.optString("title"))
                    val author = cleanText(oembed.optString("author_name"))
                    val desc = cleanText(oembed.optString("description"))
                    val thumb = oembed.optString("thumbnail_url").takeIf { it.startsWith("https://") }
                    return@withContext RichMetadata(
                        title = title ?: domain,
                        description = desc ?: if (author != null) "Video on Vimeo by $author" else "Video on Vimeo",
                        siteName = "Vimeo",
                        authorName = author,
                        thumbnailUri = thumb ?: PlatformPreviewResolver.resolve(urlStr)
                    )
                }
            }

            // 6. Generic HTML extraction with fallback chain
            val html = NetworkHelper.fetchHtmlWithUaRotation(cleanedUrl)
            if (!html.isNullOrBlank()) {
                val parsed = parseHtmlMetadata(html, cleanedUrl)
                val thumb = parsed.thumbnailUri ?: PlatformPreviewResolver.resolve(urlStr)
                return@withContext parsed.copy(
                    title = parsed.title ?: domain,
                    siteName = parsed.siteName ?: domain,
                    thumbnailUri = thumb
                )
            }

            // 7. Ultimate Fallback: Domain name
            val fallbackThumb = PlatformPreviewResolver.resolve(urlStr)
            RichMetadata(
                title = domain,
                siteName = domain,
                thumbnailUri = fallbackThumb
            )
        } catch (_: Exception) {
            RichMetadata(
                title = domain,
                siteName = domain
            )
        }
    }

    /**
     * Parses HTML using Jsoup following the strict priority order:
     * Title: og:title -> twitter:title -> <title>
     * Description: og:description -> twitter:description -> meta[name=description]
     * Site name: og:site_name -> meta[name=application-name]
     * Author: meta[name=author] -> meta[property=article:author] -> meta[name=twitter:creator]
     */
    fun parseHtmlMetadata(html: String, baseUrl: String): RichMetadata {
        val doc = try {
            Jsoup.parse(html, baseUrl)
        } catch (_: Exception) {
            return RichMetadata()
        }

        // Title
        val ogTitle = doc.select("meta[property=og:title]").attr("content").takeIf { it.isNotBlank() }
        val twTitle = doc.select("meta[name=twitter:title]").attr("content").takeIf { it.isNotBlank() }
        val docTitle = doc.title().takeIf { it.isNotBlank() }
        val rawTitle = ogTitle ?: twTitle ?: docTitle
        val title = cleanText(rawTitle)

        // Description
        val ogDesc = doc.select("meta[property=og:description]").attr("content").takeIf { it.isNotBlank() }
        val twDesc = doc.select("meta[name=twitter:description]").attr("content").takeIf { it.isNotBlank() }
        val metaDesc = doc.select("meta[name=description]").attr("content").takeIf { it.isNotBlank() }
        val rawDesc = ogDesc ?: twDesc ?: metaDesc
        val description = cleanText(rawDesc)

        // Site Name
        val ogSite = doc.select("meta[property=og:site_name]").attr("content").takeIf { it.isNotBlank() }
        val appName = doc.select("meta[name=application-name]").attr("content").takeIf { it.isNotBlank() }
        val siteName = cleanText(ogSite ?: appName)

        // Author / Channel
        val authorMeta = doc.select("meta[name=author]").attr("content").takeIf { it.isNotBlank() }
        val articleAuthor = doc.select("meta[property=article:author]").attr("content").takeIf { it.isNotBlank() }
        val twCreator = doc.select("meta[name=twitter:creator]").attr("content").takeIf { it.isNotBlank() }
        val authorName = cleanText(authorMeta ?: articleAuthor ?: twCreator)

        // Thumbnail from meta tags
        val thumb = MetaTagExtractor.extractImageUrl(html, baseUrl)

        return RichMetadata(
            title = title,
            description = description,
            siteName = siteName,
            authorName = authorName,
            thumbnailUri = thumb
        )
    }

    /**
     * Decodes HTML entities and collapses whitespace.
     */
    fun cleanText(raw: String?): String? {
        if (raw.isNullOrBlank()) return null
        val unescaped = org.jsoup.parser.Parser.unescapeEntities(raw, false)
        val collapsed = unescaped.replace(Regex("\\s+"), " ").trim()
        return if (collapsed.isNotBlank()) collapsed else null
    }

    private suspend fun fetchOEmbed(endpoint: String): JSONObject? {
        val jsonStr = NetworkHelper.fetchJson(endpoint) ?: return null
        return try {
            JSONObject(jsonStr)
        } catch (_: Exception) {
            null
        }
    }

    private fun extractDomain(urlStr: String): String {
        return try {
            val uri = URI(urlStr)
            val host = uri.host ?: urlStr
            if (host.startsWith("www.")) host.removePrefix("www.") else host
        } catch (_: Exception) {
            urlStr
        }
    }
}

