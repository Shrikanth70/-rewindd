package com.rewindd.app.feature.library.components.media

import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.model.SavedMediaType
import com.rewindd.app.domain.usecase.UrlMetadataExtractor
import java.net.URI

enum class PresentationMode {
    HERO_VIDEO_LANDSCAPE,
    HERO_VIDEO_PORTRAIT,
    HERO_VIDEO_SQUARE,
    COMPACT_SQUARE,
    FULL_SQUARE_COVER
}

enum class FallbackCategory {
    ARTICLE,
    PAPER,
    VIDEO,
    WEBSITE,
    NOTE
}

data class MediaPresentationSpec(
    val presentationMode: PresentationMode,
    val aspectRatio: Float,
    val primaryImageUrl: String?,
    val faviconUrl: String?,
    val fallbackCategory: FallbackCategory,
    val sourceDomain: String?,
    val badgeLabel: String?
)

object MediaPresentationPolicy {

    fun resolve(
        item: SavedItem,
        forFeedCard: Boolean = false
    ): MediaPresentationSpec {
        return resolve(
            url = item.url,
            thumbnailUri = item.thumbnailUri,
            forFeedCard = forFeedCard
        )
    }

    fun resolve(
        url: String,
        thumbnailUri: String? = null,
        forFeedCard: Boolean = false
    ): MediaPresentationSpec {
        val mediaType = UrlMetadataExtractor.classifyMediaType(url)
        val lowerUrl = url.lowercase()
        val isShorts = lowerUrl.contains("youtube.com/shorts") || lowerUrl.contains("youtu.be/shorts")
        val isInstagram = mediaType == SavedMediaType.INSTAGRAM_REEL || lowerUrl.contains("instagram.com")
        val isTikTok = lowerUrl.contains("tiktok.com")

        val presentationMode = when {
            isInstagram -> PresentationMode.HERO_VIDEO_SQUARE
            isShorts || isTikTok -> PresentationMode.HERO_VIDEO_PORTRAIT
            mediaType == SavedMediaType.YOUTUBE_VIDEO -> PresentationMode.HERO_VIDEO_LANDSCAPE
            forFeedCard -> PresentationMode.COMPACT_SQUARE
            else -> PresentationMode.FULL_SQUARE_COVER
        }

        val aspectRatio = when (presentationMode) {
            PresentationMode.HERO_VIDEO_LANDSCAPE -> 16f / 9f
            PresentationMode.HERO_VIDEO_PORTRAIT -> 9f / 16f
            PresentationMode.HERO_VIDEO_SQUARE -> 1f
            PresentationMode.COMPACT_SQUARE, PresentationMode.FULL_SQUARE_COVER -> 1f
        }

        val fallbackCategory = when {
            isShorts || isTikTok || mediaType == SavedMediaType.YOUTUBE_VIDEO || isInstagram -> FallbackCategory.VIDEO
            lowerUrl.contains("arxiv.org") || lowerUrl.contains(".pdf") -> FallbackCategory.PAPER
            mediaType == SavedMediaType.WEB_ARTICLE -> FallbackCategory.ARTICLE
            else -> FallbackCategory.WEBSITE
        }

        val badgeLabel = when {
            isShorts -> "9:16 SHORTS"
            isInstagram -> "1:1 REEL"
            isTikTok -> "9:16 TIKTOK"
            mediaType == SavedMediaType.YOUTUBE_VIDEO -> "16:9 YOUTUBE"
            fallbackCategory == FallbackCategory.PAPER -> "1:1 PAPER"
            fallbackCategory == FallbackCategory.ARTICLE -> "1:1 ARTICLE"
            else -> null
        }

        val sourceDomain = try {
            val uri = URI(url)
            uri.host?.removePrefix("www.")
        } catch (e: Exception) {
            null
        }

        val faviconUrl = if (!sourceDomain.isNullOrBlank()) {
            "https://www.google.com/s2/favicons?domain=$sourceDomain&sz=128"
        } else null

        val primaryImageUrl = thumbnailUri ?: if (mediaType == SavedMediaType.YOUTUBE_VIDEO) {
            UrlMetadataExtractor.extractYouTubeThumbnail(url)
        } else null

        return MediaPresentationSpec(
            presentationMode = presentationMode,
            aspectRatio = aspectRatio,
            primaryImageUrl = primaryImageUrl,
            faviconUrl = faviconUrl,
            fallbackCategory = fallbackCategory,
            sourceDomain = sourceDomain,
            badgeLabel = badgeLabel
        )
    }
}
