package com.rewindd.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlNormalizerTest {

    @Test
    fun extractAndNormalize_cleanStandardUrl_returnsValid() {
        val input = "https://danluu.com/agent-loops-reliability"
        val result = UrlNormalizer.extractAndNormalize(input)

        assertTrue(result is UrlValidationResult.Valid)
        val valid = result as UrlValidationResult.Valid
        assertEquals("https://danluu.com/agent-loops-reliability", valid.normalizedUrl)
        assertEquals("danluu.com", valid.domain)
    }

    @Test
    fun extractAndNormalize_urlWithTrackingParameters_stripsTrackingParams() {
        val input = "https://instagram.com/reel/C7abc123/?igsh=xyz987&utm_source=ig_web_copy_link"
        val result = UrlNormalizer.extractAndNormalize(input)

        assertTrue(result is UrlValidationResult.Valid)
        val valid = result as UrlValidationResult.Valid
        assertEquals("https://instagram.com/reel/C7abc123", valid.normalizedUrl)
        assertEquals("instagram.com", valid.domain)
    }

    @Test
    fun extractAndNormalize_urlEmbeddedInSocialShareText_extractsAndNormalizes() {
        val input = "Check out this breakdown of AI agents! https://substack.com/@danluu/post/1082?utm_campaign=post&utm_medium=web"
        val result = UrlNormalizer.extractAndNormalize(input)

        assertTrue(result is UrlValidationResult.Valid)
        val valid = result as UrlValidationResult.Valid
        assertEquals("https://substack.com/@danluu/post/1082", valid.normalizedUrl)
        assertEquals("substack.com", valid.domain)
    }

    @Test
    fun extractAndNormalize_preservesContentQueryParameters() {
        val input = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&si=track123"
        val result = UrlNormalizer.extractAndNormalize(input)

        assertTrue(result is UrlValidationResult.Valid)
        val valid = result as UrlValidationResult.Valid
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", valid.normalizedUrl)
    }

    @Test
    fun extractAndNormalize_emptyOrNullInput_returnsInvalid() {
        val emptyResult = UrlNormalizer.extractAndNormalize("")
        val nullResult = UrlNormalizer.extractAndNormalize(null)

        assertTrue(emptyResult is UrlValidationResult.Invalid)
        assertTrue(nullResult is UrlValidationResult.Invalid)
    }

    @Test
    fun extractAndNormalize_nonHttpScheme_returnsInvalid() {
        val fileResult = UrlNormalizer.extractAndNormalize("file:///data/local/tmp/test.txt")
        val contentResult = UrlNormalizer.extractAndNormalize("content://media/external/images")

        assertTrue(fileResult is UrlValidationResult.Invalid)
        assertTrue(contentResult is UrlValidationResult.Invalid)
    }
}
