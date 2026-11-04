package com.rewindd.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlMetadataExtractorTest {

    @Test
    fun extractYouTubeThumbnail_standardWatchUrl_returnsHqDefault() {
        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val thumbnail = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", thumbnail)
    }

    @Test
    fun extractYouTubeThumbnail_shortUrl_returnsHqDefault() {
        val url = "https://youtu.be/dQw4w9WgXcQ?t=42"
        val thumbnail = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", thumbnail)
    }

    @Test
    fun extractYouTubeThumbnail_shortsUrl_returnsHqDefault() {
        val url = "https://www.youtube.com/shorts/dQw4w9WgXcQ"
        val thumbnail = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", thumbnail)
    }

    @Test
    fun extractYouTubeThumbnail_embedUrl_returnsHqDefault() {
        val url = "https://www.youtube.com/embed/dQw4w9WgXcQ"
        val thumbnail = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", thumbnail)
    }

    @Test
    fun extractYouTubeThumbnail_nonYouTubeUrl_returnsNull() {
        val url = "https://example.com/article"
        val thumbnail = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertNull(thumbnail)
    }

    @Test
    fun isSafeUrl_validHttpHttps_returnsTrue() {
        assertTrue(UrlMetadataExtractor.isSafeUrl("https://example.com"))
        assertTrue(UrlMetadataExtractor.isSafeUrl("http://example.com/test"))
    }

    @Test
    fun isSafeUrl_dangerousSchemesAndPrivateHosts_returnsFalse() {
        assertFalse(UrlMetadataExtractor.isSafeUrl("file:///etc/passwd"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("javascript:alert(1)"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://127.0.0.1/admin"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://localhost:8080"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://192.168.1.1/router"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://10.0.0.1/internal"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://169.254.169.254/latest/meta-data"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://0.0.0.0"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://172.16.0.1"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://172.24.1.5"))
        assertFalse(UrlMetadataExtractor.isSafeUrl("http://172.31.255.254"))
        assertTrue(UrlMetadataExtractor.isSafeUrl("http://172.32.0.1")) // Public IP
    }

    @Test
    fun classifyMediaType_youtubeWatchUrl_returnsYouTube() {
        val type = UrlMetadataExtractor.classifyMediaType("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        assertEquals(com.rewindd.app.domain.model.SavedMediaType.YOUTUBE_VIDEO, type)
    }

    @Test
    fun classifyMediaType_instagramReelUrl_returnsInstagram() {
        val type = UrlMetadataExtractor.classifyMediaType("https://www.instagram.com/reel/C1234567890/")
        assertEquals(com.rewindd.app.domain.model.SavedMediaType.INSTAGRAM_REEL, type)
    }

    @Test
    fun classifyMediaType_articleUrl_returnsWebArticle() {
        val type = UrlMetadataExtractor.classifyMediaType("https://danluu.substack.com/p/agent-loops")
        assertEquals(com.rewindd.app.domain.model.SavedMediaType.WEB_ARTICLE, type)
    }

    @Test
    fun classifyMediaType_genericUrl_returnsGeneric() {
        val type = UrlMetadataExtractor.classifyMediaType("https://wikipedia.org/wiki/Kotlin")
        assertEquals(com.rewindd.app.domain.model.SavedMediaType.GENERIC_LINK, type)
    }
}
