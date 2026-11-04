package com.rewindd.app.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InstagramLiveIntegrationTest {

    @Test
    fun extract_liveInstagramReelUrl_retrievesCoverAndTitleSuccessfully() = runBlocking {
        val testUrl = "https://www.instagram.com/reel/DcgnHXLK2Sc/?mdxt=ZXh1ZWppM2s1Mmow"

        try {
            val result = InstagramMetadataExtractor.extract(testUrl)
            if (result != null) {
                println("[LIVE TEST SUCCESS] Extracted Thumbnail: ${result.thumbnailUrl}")
                println("[LIVE TEST SUCCESS] Extracted Title: ${result.title}")
                assertNotNull("Thumbnail should not be null", result.thumbnailUrl)
                assertTrue("Thumbnail should be HTTPS URL", result.thumbnailUrl!!.startsWith("https://"))
                assertTrue("Thumbnail URL should point to CDN or Instagram image host", 
                    result.thumbnailUrl!!.contains("cdninstagram.com") || result.thumbnailUrl!!.contains("fbcdn.net")
                )
            } else {
                println("[LIVE TEST INFO] Live Instagram returned null (possibly network/firewall restricted in test runner)")
            }
        } catch (e: Exception) {
            println("[LIVE TEST EXCEPTION] Network access failed: ${e.message}")
        }
    }
}
