package com.rewindd.app.linkpreview.extra

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformPreviewResolverTest {

    @Test
    fun testStripTrackingParameters() {
        val raw = "https://www.pinterest.com/pin/123456/?utm_source=share&utm_medium=android_app&utm_campaign=share_pin&si=abc123xyz"
        val cleaned = NetworkHelper.stripTrackingParameters(raw)
        assertEquals("https://www.pinterest.com/pin/123456/", cleaned)

        val xUrl = "https://x.com/user/status/12345?s=20&t=abcdef&ref_src=twsrc"
        val cleanedX = NetworkHelper.stripTrackingParameters(xUrl)
        // Ensure tracking keys are removed
        assertFalse(cleanedX.contains("ref_src"))
    }

    @Test
    fun testPlatformMatchers() {
        val pinterest = PinterestHandler()
        assertTrue(pinterest.matches("https://www.pinterest.com/pin/123456789/"))
        assertTrue(pinterest.matches("https://pin.it/7a8b9c"))
        assertFalse(pinterest.matches("https://google.com"))

        val twitter = TwitterHandler()
        assertTrue(twitter.matches("https://twitter.com/jack/status/20"))
        assertTrue(twitter.matches("https://x.com/elonmusk/status/98765"))
        assertTrue(twitter.matches("https://t.co/abc123xyz"))
        assertFalse(twitter.matches("https://example.com"))

        val threads = ThreadsHandler()
        assertTrue(threads.matches("https://www.threads.net/@zuck/post/123456"))
        assertFalse(threads.matches("https://instagram.com"))

        val linkedin = LinkedInHandler()
        assertTrue(linkedin.matches("https://www.linkedin.com/posts/activity-123456"))
        assertTrue(linkedin.matches("https://lnkd.in/gAbC123"))
        assertFalse(linkedin.matches("https://github.com"))

        val reddit = RedditHandler()
        assertTrue(reddit.matches("https://www.reddit.com/r/Android/comments/abc/"))
        assertTrue(reddit.matches("https://redd.it/xyz123"))

        val tiktok = TikTokHandler()
        assertTrue(tiktok.matches("https://www.tiktok.com/@creator/video/123456789"))

        val vimeo = VimeoHandler()
        assertTrue(vimeo.matches("https://vimeo.com/12345678"))

        val spotify = SpotifyHandler()
        assertTrue(spotify.matches("https://open.spotify.com/track/4cOdK2wGLETKBW3PvgPWqT"))
        assertTrue(spotify.matches("https://spotify.link/abc123xyz"))

        val facebook = FacebookHandler()
        assertTrue(facebook.matches("https://www.facebook.com/watch/?v=12345"))
        assertTrue(facebook.matches("https://fb.watch/xyz/"))

        val popular = PopularServicesHandler()
        assertTrue(popular.matches("https://github.com/torvalds/linux"))
        assertTrue(popular.matches("https://medium.com/@author/story-title"))
    }

    @Test
    fun testMetaTagExtractionPriorityOrder() {
        // Priority 1: og:image:secure_url over og:image
        val html1 = """
            <html>
                <head>
                    <meta property="og:image:secure_url" content="https://images.example.com/secure.jpg" />
                    <meta property="og:image" content="https://images.example.com/normal.jpg" />
                    <meta name="twitter:image" content="https://images.example.com/twitter.jpg" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://images.example.com/secure.jpg",
            MetaTagExtractor.extractImageUrl(html1, "https://example.com/article")
        )

        // Priority 2: og:image over twitter:image
        val html2 = """
            <html>
                <head>
                    <meta property="og:image" content="https://images.example.com/normal.jpg" />
                    <meta name="twitter:image" content="https://images.example.com/twitter.jpg" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://images.example.com/normal.jpg",
            MetaTagExtractor.extractImageUrl(html2, "https://example.com/article")
        )

        // Priority 3: twitter:image over link rel="image_src"
        val html3 = """
            <html>
                <head>
                    <meta name="twitter:image" content="https://images.example.com/twitter.jpg" />
                    <link rel="image_src" href="https://images.example.com/link_src.jpg" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://images.example.com/twitter.jpg",
            MetaTagExtractor.extractImageUrl(html3, "https://example.com/article")
        )

        // Priority 4: link rel="image_src"
        val html4 = """
            <html>
                <head>
                    <link rel="image_src" href="https://images.example.com/link_src.jpg" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://images.example.com/link_src.jpg",
            MetaTagExtractor.extractImageUrl(html4, "https://example.com/article")
        )
    }

    @Test
    fun testRelativeUrlResolution() {
        val html = """
            <html>
                <head>
                    <meta property="og:image" content="/assets/cover.png" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://cdn.example.org/assets/cover.png",
            MetaTagExtractor.extractImageUrl(html, "https://cdn.example.org/blog/post-1")
        )
    }

    @Test
    fun testLoginWallDetection() {
        val html = """
            <html>
                <head>
                    <title>Log in to LinkedIn | LinkedIn</title>
                    <meta property="og:image" content="https://static.licdn.com/sc/h/al2o9zrvเค3.png" />
                </head>
                <body>
                    <div class="authwall">Please sign in to view this profile</div>
                </body>
            </html>
        """.trimIndent()
        assertTrue(NetworkHelper.isLoginWall(html))
        assertNull(MetaTagExtractor.extractImageUrl(html, "https://www.linkedin.com/in/someone"))
    }

    @Test
    fun testFaviconExtraction() {
        val html = """
            <html>
                <head>
                    <link rel="apple-touch-icon" href="/touch-icon.png" />
                    <link rel="icon" href="/favicon.png" />
                </head>
            </html>
        """.trimIndent()
        assertEquals(
            "https://example.com/touch-icon.png",
            MetaTagExtractor.extractFaviconOrTouchIcon(html, "https://example.com")
        )
    }

    @Test
    fun testSkipsYouTubeAndInstagram() = runBlocking {
        PlatformPreviewResolver.clearCache()
        assertNull(PlatformPreviewResolver.resolve("https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
        assertNull(PlatformPreviewResolver.resolve("https://youtu.be/dQw4w9WgXcQ"))
        assertNull(PlatformPreviewResolver.resolve("https://www.instagram.com/reel/C1234567890/"))
        assertNull(PlatformPreviewResolver.resolve("https://instagr.am/p/12345/"))
    }

    @Test
    fun testNeverThrowsOnInvalidInputs() = runBlocking {
        PlatformPreviewResolver.clearCache()
        assertNull(PlatformPreviewResolver.resolve(""))
        assertNull(PlatformPreviewResolver.resolve("invalid-uri"))
        assertNull(PlatformPreviewResolver.resolve("http://127.0.0.1:8080/exploit"))
    }
}

