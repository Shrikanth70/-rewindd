package com.rewindd.app.core.network

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.security.MessageDigest
import kotlin.math.max

sealed class ExtractionResult {
    data class Success(
        val title: String,
        val cleanedText: String,
        val wordCount: Int,
        val readingTimeMinutes: Int,
        val sourceHash: String
    ) : ExtractionResult()

    data class Ineligible(val reason: String) : ExtractionResult()
}

class ArticleContentExtractor {

    private val paywallSignatures = listOf(
        "subscriber only", "subscribers only", "subscription required",
        "paywall", "exclusive access", "sign in to read", "create an account to continue"
    )

    fun extractFromHtml(html: String, url: String): ExtractionResult {
        if (html.isBlank()) {
            return ExtractionResult.Ineligible("Empty HTML response")
        }

        val doc: Document = Jsoup.parse(html, url)
        val rawLower = doc.text().lowercase()
        for (sig in paywallSignatures) {
            if (rawLower.contains(sig) && rawLower.length < 500) {
                return ExtractionResult.Ineligible("Article is behind a paywall or login requirement")
            }
        }

        // Clean up unwanted noise tags
        doc.select("script, style, nav, header, footer, aside, noscript, svg, form, .ad, .ads, .social-share, .newsletter").remove()

        val title = doc.title().ifBlank { doc.select("h1").text() }.trim()

        val articleElement = doc.select("article, main, .post-content, .article-body, .entry-content").first()
        val text = (articleElement?.text() ?: doc.body()?.text() ?: "").trim()

        if (text.length < 120) {
            return ExtractionResult.Ineligible("Insufficient accessible article text extracted")
        }

        val truncatedText = if (text.length > 16000) text.substring(0, 16000) else text
        val words = truncatedText.split(Regex("\\s+")).filter { it.isNotBlank() }
        val wordCount = words.size
        val readingTime = max(1, (wordCount / 200))

        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(truncatedText.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }

        return ExtractionResult.Success(
            title = title.ifBlank { "Untitled Article" },
            cleanedText = truncatedText,
            wordCount = wordCount,
            readingTimeMinutes = readingTime,
            sourceHash = hash
        )
    }
}
