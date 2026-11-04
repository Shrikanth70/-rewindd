package com.rewindd.app.feature.library.media

import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.feature.library.components.media.FallbackCategory
import com.rewindd.app.feature.library.components.media.MediaPresentationPolicy
import com.rewindd.app.feature.library.components.media.PresentationMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MediaPresentationPolicyTest {

    private fun createItem(
        url: String,
        thumbnailUri: String? = null
    ): SavedItem {
        return SavedItem(
            id = "test-1",
            url = url,
            normalizedUrl = url,
            title = "Test Item",
            thumbnailUri = thumbnailUri,
            capturedAtUtc = System.currentTimeMillis(),
            updatedAtUtc = System.currentTimeMillis()
        )
    }

    @Test
    fun youtubeVideoResolvesToSixteenByNineHero() {
        val item = createItem(url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = true)

        assertEquals(PresentationMode.HERO_VIDEO_LANDSCAPE, spec.presentationMode)
        assertEquals(16f / 9f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.VIDEO, spec.fallbackCategory)
        assertEquals("16:9 YOUTUBE", spec.badgeLabel)
        assertEquals("img.youtube.com", spec.primaryImageUrl?.let { java.net.URI(it).host })
    }

    @Test
    fun youtubeShortsResolvesToNineBySixteenHero() {
        val item = createItem(url = "https://www.youtube.com/shorts/dQw4w9WgXcQ")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = true)

        assertEquals(PresentationMode.HERO_VIDEO_PORTRAIT, spec.presentationMode)
        assertEquals(9f / 16f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.VIDEO, spec.fallbackCategory)
        assertEquals("9:16 SHORTS", spec.badgeLabel)
    }

    @Test
    fun instagramReelResolvesToOneByOneSquareHero() {
        val item = createItem(url = "https://www.instagram.com/reel/C3123456789/")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = false)

        assertEquals(PresentationMode.HERO_VIDEO_SQUARE, spec.presentationMode)
        assertEquals(1f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.VIDEO, spec.fallbackCategory)
        assertEquals("1:1 REEL", spec.badgeLabel)
    }

    @Test
    fun instagramPostResolvesToOneByOneSquareHero() {
        val item = createItem(url = "https://www.instagram.com/p/DB123456789/")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = false)

        assertEquals(PresentationMode.HERO_VIDEO_SQUARE, spec.presentationMode)
        assertEquals(1f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.VIDEO, spec.fallbackCategory)
        assertEquals("1:1 REEL", spec.badgeLabel)
    }

    @Test
    fun articleResolvesToOneByOneCompactSquareInFeed() {
        val item = createItem(url = "https://newsletter.substack.com/p/my-post")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = true)

        assertEquals(PresentationMode.COMPACT_SQUARE, spec.presentationMode)
        assertEquals(1f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.ARTICLE, spec.fallbackCategory)
        assertEquals("newsletter.substack.com", spec.sourceDomain)
        assertNotNull(spec.faviconUrl)
    }

    @Test
    fun paperResolvesToOneByOneSquare() {
        val item = createItem(url = "https://arxiv.org/abs/2301.00001")
        val spec = MediaPresentationPolicy.resolve(item, forFeedCard = false)

        assertEquals(PresentationMode.FULL_SQUARE_COVER, spec.presentationMode)
        assertEquals(1f, spec.aspectRatio, 0.01f)
        assertEquals(FallbackCategory.PAPER, spec.fallbackCategory)
        assertEquals("1:1 PAPER", spec.badgeLabel)
    }

    @Test
    fun prefersCustomThumbnailIfPresent() {
        val customThumb = "https://cdn.example.com/custom.jpg"
        val item = createItem(
            url = "https://example.com/article",
            thumbnailUri = customThumb
        )
        val spec = MediaPresentationPolicy.resolve(item)
        assertEquals(customThumb, spec.primaryImageUrl)
    }
}
