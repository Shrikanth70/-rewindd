package com.rewindd.app.linkpreview.extra

import com.rewindd.app.core.database.entity.SavedItemEntity
import com.rewindd.app.domain.model.MetadataStatus
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RichLinkMetadataExtractorTest {

    @Test
    fun testCleanText_decodesEntitiesAndCollapsesWhitespace() {
        val raw = "  Breaking &amp; Exclusive:  &#39;Rewindd&#39;  app   launches &hellip;  "
        val cleaned = RichLinkMetadataExtractor.cleanText(raw)
        assertEquals("Breaking & Exclusive: 'Rewindd' app launches …", cleaned)

        val emojiRaw = "Nobita Nobi &#x1f63c;&#x1f919;&#x1f3fb;"
        val emojiCleaned = RichLinkMetadataExtractor.cleanText(emojiRaw)
        assertNotNull(emojiCleaned)
        assert(emojiCleaned!!.contains("Nobita Nobi"))

        assertNull(RichLinkMetadataExtractor.cleanText(null))
        assertNull(RichLinkMetadataExtractor.cleanText("   \n\t  "))
    }

    @Test
    fun testParseHtmlMetadata_openGraphPriority() {
        val sampleHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Fallback Document Title</title>
                <meta name="description" content="Fallback standard description" />
                <meta property="og:title" content="OpenGraph Headline" />
                <meta property="og:description" content="OpenGraph detailed summary of the article." />
                <meta property="og:site_name" content="The Verge" />
                <meta property="article:author" content="Alex Heath" />
                <meta property="og:image:secure_url" content="https://cdn.example.com/cover_secure.png" />
            </head>
            <body></body>
            </html>
        """.trimIndent()

        val parsed = RichLinkMetadataExtractor.parseHtmlMetadata(sampleHtml, "https://www.theverge.com/2026/test")
        assertEquals("OpenGraph Headline", parsed.title)
        assertEquals("OpenGraph detailed summary of the article.", parsed.description)
        assertEquals("The Verge", parsed.siteName)
        assertEquals("Alex Heath", parsed.authorName)
        assertEquals("https://cdn.example.com/cover_secure.png", parsed.thumbnailUri)
    }

    @Test
    fun testParseHtmlMetadata_twitterCardFallback() {
        val twitterHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Document Title</title>
                <meta name="twitter:title" content="Twitter Card Title" />
                <meta name="twitter:description" content="Twitter thread breakdown of architecture." />
                <meta name="twitter:creator" content="@developer" />
                <meta name="application-name" content="Dev Community" />
                <meta name="twitter:image" content="https://pbs.twimg.com/media/preview.jpg" />
            </head>
            <body></body>
            </html>
        """.trimIndent()

        val parsed = RichLinkMetadataExtractor.parseHtmlMetadata(twitterHtml, "https://dev.to/article")
        assertEquals("Twitter Card Title", parsed.title)
        assertEquals("Twitter thread breakdown of architecture.", parsed.description)
        assertEquals("@developer", parsed.authorName)
        assertEquals("Dev Community", parsed.siteName)
        assertEquals("https://pbs.twimg.com/media/preview.jpg", parsed.thumbnailUri)
    }

    @Test
    fun testParseHtmlMetadata_standardHtmlFallback() {
        val standardHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Personal Blog — Thoughts on Local Vaults</title>
                <meta name="description" content="Why storing data locally is the best privacy defense." />
                <meta name="author" content="Jane Doe" />
            </head>
            <body></body>
            </html>
        """.trimIndent()

        val parsed = RichLinkMetadataExtractor.parseHtmlMetadata(standardHtml, "https://janedoe.me/blog")
        assertEquals("Personal Blog — Thoughts on Local Vaults", parsed.title)
        assertEquals("Why storing data locally is the best privacy defense.", parsed.description)
        assertEquals("Jane Doe", parsed.authorName)
        assertNull(parsed.thumbnailUri)
    }

    @Test
    fun testSavedItemEntity_withNullableMetadataFields() {
        val item = SavedItem(
            id = "test-123",
            url = "https://youtube.com/watch?v=123",
            normalizedUrl = "youtube.com/watch?v=123",
            title = "Awesome Video Title",
            description = "Complete video description detailing the tutorial.",
            siteName = "YouTube",
            authorName = "Tech Creator",
            thumbnailUri = "https://img.youtube.com/vi/123/hqdefault.jpg",
            capturedAtUtc = 1700000000L,
            updatedAtUtc = 1700000000L,
            reviewState = ReviewState.UNREVIEWED,
            metadataStatus = MetadataStatus.SUCCESS
        )

        val entity = SavedItemEntity.fromDomain(item)
        assertEquals("Complete video description detailing the tutorial.", entity.description)
        assertEquals("YouTube", entity.siteName)
        assertEquals("Tech Creator", entity.authorName)

        val reconstituted = entity.toDomain()
        assertEquals(item.id, reconstituted.id)
        assertEquals(item.description, reconstituted.description)
        assertEquals(item.siteName, reconstituted.siteName)
        assertEquals(item.authorName, reconstituted.authorName)
    }
}

