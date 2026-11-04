package com.rewindd.app.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AppIconLoaderTest {

    @Test
    fun getFallbackLetter_validAppName_returnsFirstCharUppercase() {
        assertEquals("Y", AppIconLoader.getFallbackLetter("YouTube"))
        assertEquals("I", AppIconLoader.getFallbackLetter("Instagram"))
        assertEquals("C", AppIconLoader.getFallbackLetter("Chrome"))
        assertEquals("T", AppIconLoader.getFallbackLetter("tiktok"))
    }

    @Test
    fun getFallbackLetter_emptyOrSpecial_returnsQuestionMark() {
        assertEquals("?", AppIconLoader.getFallbackLetter(""))
        assertEquals("?", AppIconLoader.getFallbackLetter("   "))
        assertEquals("?", AppIconLoader.getFallbackLetter("---"))
    }
}
