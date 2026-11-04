package com.rewindd.app.feature.capture

import com.rewindd.app.domain.model.SavedMediaType
import com.rewindd.app.domain.usecase.UrlMetadataExtractor
import com.rewindd.app.domain.usecase.UrlNormalizer
import com.rewindd.app.domain.usecase.UrlValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuickCaptureValidationTest {

    @Test
    fun urlNormalizer_extractsAndValidates_validWebLink() {
        val input = "Check this out https://danluu.com/agent-loops-reliability?utm_source=twitter"
        val result = UrlNormalizer.extractAndNormalize(input)

        assertTrue(result is UrlValidationResult.Valid)
        val valid = result as UrlValidationResult.Valid
        assertEquals("danluu.com", valid.domain)
        assertEquals("https://danluu.com/agent-loops-reliability", valid.normalizedUrl)
    }

    @Test
    fun urlNormalizer_identifiesYoutubeMedia() {
        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val mediaType = UrlMetadataExtractor.classifyMediaType(url)
        assertEquals(SavedMediaType.YOUTUBE_VIDEO, mediaType)

        val thumb = UrlMetadataExtractor.extractYouTubeThumbnail(url)
        assertEquals("https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg", thumb)
    }

    @Test
    fun urlNormalizer_handlesInvalidOrBlankInput() {
        val blankResult = UrlNormalizer.extractAndNormalize("   ")
        assertTrue(blankResult is UrlValidationResult.Invalid)

        val invalidResult = UrlNormalizer.extractAndNormalize("not a valid link here")
        assertTrue(invalidResult is UrlValidationResult.Invalid)
    }
}
