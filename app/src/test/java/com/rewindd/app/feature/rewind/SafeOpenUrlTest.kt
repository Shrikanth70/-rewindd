package com.rewindd.app.feature.rewind

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rewindd.app.feature.rewind.components.safeOpenUrl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SafeOpenUrlTest {

    @Test
    fun testValidHttpUrlDoesNotTriggerFallback() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var fallbackCalled = false
        safeOpenUrl(context, "https://example.com/article") {
            fallbackCalled = true
        }
        assertFalse(fallbackCalled)
    }

    @Test
    fun testNonHttpUrlTriggersFallback() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var fallbackCalled = false
        safeOpenUrl(context, "javascript:alert(1)") {
            fallbackCalled = true
        }
        assertTrue(fallbackCalled)
    }

    @Test
    fun testFileSchemeTriggersFallback() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var fallbackCalled = false
        safeOpenUrl(context, "file:///sdcard/malicious.apk") {
            fallbackCalled = true
        }
        assertTrue(fallbackCalled)
    }
}
