package com.rewindd.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.URI

class InstagramMetadataExtractorTest {

    @Test
    fun isInstagramUrl_validReelAndPostUrls_returnsTrue() {
        assertTrue(InstagramMetadataExtractor.isInstagramUrl("https://www.instagram.com/reel/DcgnHXLK2Sc/?mdxt=ZXh1ZWppM2s1Mmow"))
        assertTrue(InstagramMetadataExtractor.isInstagramUrl("https://instagram.com/reel/C7abc123/"))
        assertTrue(InstagramMetadataExtractor.isInstagramUrl("https://www.instagram.com/p/DF12345/"))
        assertTrue(InstagramMetadataExtractor.isInstagramUrl("https://instagr.am/reel/xyz987"))
    }

    @Test
    fun isInstagramUrl_invalidHostsOrPaths_returnsFalse() {
        assertFalse(InstagramMetadataExtractor.isInstagramUrl("https://youtube.com/watch?v=12345678901"))
        assertFalse(InstagramMetadataExtractor.isInstagramUrl("https://malicious-instagram.com/reel/123"))
        assertFalse(InstagramMetadataExtractor.isInstagramUrl("https://instagram.com/explore/"))
        assertFalse(InstagramMetadataExtractor.isInstagramUrl("not a url"))
    }

    @Test
    fun isSafeDestination_blocksInternalAndLoopbackAddresses() {
        assertFalse(InstagramMetadataExtractor.isSafeDestination(URI("http://127.0.0.1/reel/123")))
        assertFalse(InstagramMetadataExtractor.isSafeDestination(URI("http://localhost/reel/123")))
        assertFalse(InstagramMetadataExtractor.isSafeDestination(URI("http://10.0.0.1/reel/123")))
        assertFalse(InstagramMetadataExtractor.isSafeDestination(URI("http://192.168.1.1/reel/123")))
        assertFalse(InstagramMetadataExtractor.isSafeDestination(URI("http://169.254.169.254/latest/meta-data/")))
    }

    @Test
    fun isSafeDestination_allowsValidPublicInstagramHosts() {
        assertTrue(InstagramMetadataExtractor.isSafeDestination(URI("https://www.instagram.com/reel/DcgnHXLK2Sc/")))
        assertTrue(InstagramMetadataExtractor.isSafeDestination(URI("https://instagram.com/p/12345/")))
    }

    @Test
    fun unescapeHtmlEntities_convertsCommonEntities() {
        val raw = "https://scontent.cdninstagram.com/v/t51.jpg?a=1&amp;b=2&amp;c=3&quot;test&quot;&#x2022;"
        val unescaped = InstagramMetadataExtractor.unescapeHtmlEntities(raw)
        assertEquals("https://scontent.cdninstagram.com/v/t51.jpg?a=1&b=2&c=3\"test\"•", unescaped)
    }

    @Test
    fun parseHtmlMetadata_extractsOgImageAndTitleCorrectly() {
        val mockHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta property="og:title" content="Sam John on Instagram: &quot;Nahhh I need to chill out&quot;" />
                <meta property="og:image" content="https://scontent.cdninstagram.com/v/sample.jpg?stp=cmp1&amp;oh=123" />
                <meta property="og:description" content="151K likes, 526 comments - sam.john.creates" />
            </head>
            <body></body>
            </html>
        """.trimIndent()

        val metadata = InstagramMetadataExtractor.parseHtmlMetadata(mockHtml)
        assertNotNull(metadata)
        assertEquals("https://scontent.cdninstagram.com/v/sample.jpg?stp=cmp1&oh=123", metadata.thumbnailUrl)
        assertEquals("Sam John on Instagram: \"Nahhh I need to chill out\"", metadata.title)
    }

    @Test
    fun parseHtmlMetadata_missingOgImage_returnsNullThumbnail() {
        val mockHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Instagram Login</title>
            </head>
            <body>Please log in to continue.</body>
            </html>
        """.trimIndent()

        val metadata = InstagramMetadataExtractor.parseHtmlMetadata(mockHtml)
        assertNull(metadata.thumbnailUrl)
    }
}
