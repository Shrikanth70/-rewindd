package com.rewindd.app.core.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleContentExtractorTest {
    private val extractor = ArticleContentExtractor()

    @Test
    fun testCleanHtmlArticleExtraction() {
        val html = """
            <!DOCTYPE html>
            <html>
            <head><title>Understanding Kotlin Coroutines</title></head>
            <body>
                <nav><a href="/home">Home</a></nav>
                <article>
                    <h1>Understanding Kotlin Coroutines</h1>
                    <p>Coroutines simplify asynchronous programming by providing sequential code structure across applications.</p>
                    <p>Structured concurrency guarantees that child tasks complete before parent scopes exit safely.</p>
                    <p>In Android development, LifecycleScope and ViewModelScope prevent memory leaks during configuration changes and rotation events.</p>
                </article>
                <script>trackUser();</script>
                <footer>Copyright 2026</footer>
            </body>
            </html>
        """.trimIndent()

        val result = extractor.extractFromHtml(html, "https://example.com/coroutines")
        assertTrue(result is ExtractionResult.Success)
        val success = result as ExtractionResult.Success
        assertEquals("Understanding Kotlin Coroutines", success.title)
        assertTrue(success.cleanedText.contains("Coroutines simplify asynchronous"))
        assertTrue(!success.cleanedText.contains("Home"))
        assertTrue(!success.cleanedText.contains("trackUser"))
        assertTrue(success.readingTimeMinutes >= 1)
        assertTrue(success.sourceHash.isNotBlank())
    }

    @Test
    fun testPaywallAndIneligibleDetection() {
        val paywallHtml = """
            <html>
            <head><title>Premium Article</title></head>
            <body>
                <h1>Exclusive Industry Analysis</h1>
                <div class="paywall-notice">This content is for subscribers only. Please subscribe to read the full article.</div>
            </body>
            </html>
        """.trimIndent()

        val result = extractor.extractFromHtml(paywallHtml, "https://example.com/premium")
        assertTrue(result is ExtractionResult.Ineligible)
    }
}
