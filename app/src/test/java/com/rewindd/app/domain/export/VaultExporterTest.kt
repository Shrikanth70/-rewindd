package com.rewindd.app.domain.export

import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.MetadataStatus
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.model.Tag
import com.rewindd.app.domain.model.UserPreferences
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class VaultExporterTest {

    @Test
    fun exportToJson_createsStructuredValidJsonWithItemsCollectionsTags() {
        val col = Collection(
            id = "col-1",
            name = "Engineering",
            colorToken = "rewind-yellow",
            createdAtUtc = 1000L,
            updatedAtUtc = 1000L,
            isPinned = true,
            sortOrder = 0
        )
        val tag = Tag(
            id = "tag-1",
            normalizedName = "tech",
            displayName = "#tech",
            colorToken = null
        )
        val item = SavedItem(
            id = "item-1",
            url = "https://example.com/article",
            normalizedUrl = "https://example.com/article",
            sourcePackage = "com.twitter.android",
            sourceLabel = "Twitter",
            title = "Test Article",
            thumbnailUri = null,
            capturedAtUtc = 2000L,
            updatedAtUtc = 2000L,
            reviewState = ReviewState.REVIEWED,
            isPinned = true,
            collectionId = "col-1",
            note = "Must read note",
            metadataStatus = MetadataStatus.SUCCESS,
            archivedAtUtc = null,
            contentHash = null
        )
        val detail = SavedItemDetailsDomain(
            item = item,
            collection = col,
            tags = listOf(tag)
        )
        val prefs = UserPreferences(
            trackedApps = setOf("com.twitter.android", "com.instagram.android"),
            retentionMonths = 6,
            remindersEnabled = true
        )

        val jsonString = VaultDataExporter.exportToJson(
            items = listOf(detail),
            collections = listOf(col),
            tags = listOf(tag),
            preferences = prefs,
            exportedAtUtc = 5000L
        )

        val json = JSONObject(jsonString)
        assertEquals(1, json.getInt("schemaVersion"))
        assertEquals("Rewindd", json.getString("appName"))
        assertEquals(5000L, json.getLong("exportedAtUtc"))

        val itemsArray = json.getJSONArray("items")
        assertEquals(1, itemsArray.length())
        val itemObj = itemsArray.getJSONObject(0)
        assertEquals("item-1", itemObj.getString("id"))
        assertEquals("https://example.com/article", itemObj.getString("url"))
        assertEquals("Engineering", itemObj.getString("collectionName"))
        assertEquals("Must read note", itemObj.getString("note"))
        assertEquals("#tech", itemObj.getJSONArray("tags").getString(0))

        val colsArray = json.getJSONArray("collections")
        assertEquals(1, colsArray.length())
        assertEquals("Engineering", colsArray.getJSONObject(0).getString("name"))

        val tagsArray = json.getJSONArray("tags")
        assertEquals(1, tagsArray.length())
        assertEquals("#tech", tagsArray.getJSONObject(0).getString("displayName"))

        val prefsObj = json.getJSONObject("preferences")
        assertEquals(2, prefsObj.getJSONArray("trackedApps").length())
        assertEquals(6, prefsObj.getInt("retentionMonths"))
    }

    @Test
    fun exportToMarkdown_createsMarkdownDocument() {
        val col = Collection("c1", "Design", "blue", 1L, 1L, false, 0)
        val tag = Tag("t1", "ui", "#ui", null)
        val item = SavedItem(
            id = "i1",
            url = "https://design.com",
            normalizedUrl = "https://design.com",
            sourcePackage = null,
            sourceLabel = "Web",
            title = "Design System",
            thumbnailUri = null,
            capturedAtUtc = 1000L,
            updatedAtUtc = 1000L,
            reviewState = ReviewState.UNREVIEWED,
            isPinned = false,
            collectionId = "c1",
            note = "Important typography guidelines",
            metadataStatus = MetadataStatus.SUCCESS,
            archivedAtUtc = null,
            contentHash = null
        )
        val detail = SavedItemDetailsDomain(item, col, listOf(tag))

        val md = VaultDataExporter.exportToMarkdown(
            items = listOf(detail),
            collections = listOf(col),
            tags = listOf(tag)
        )

        assertTrue(md.contains("# Rewindd Vault Export"))
        assertTrue(md.contains("Design System"))
        assertTrue(md.contains("Important typography guidelines"))
        assertTrue(md.contains("#ui"))
    }

    @Test
    fun exportScreenTimeToCsv_sanitizesFormulaInjectionAndQuotes() {
        val maliciousUsage = com.rewindd.app.domain.model.DailyAppUsage(
            dateLocal = "2026-10-16",
            packageName = "=cmd|' /C calc'!A0",
            usageMillis = 120000L,
            sessionCount = 2,
            pickupCount = 2,
            sourceStatus = com.rewindd.app.domain.model.UsageSourceStatus.RELIABLE,
            lastCalculatedAtUtc = 1000L
        )

        val csv = VaultDataExporter.exportScreenTimeToCsv(listOf(maliciousUsage))
        assertTrue(csv.contains("Date,PackageName,DwellMinutes,DwellMillis,Sessions,Pickups,Status"))
        // Proves formula character '=' was safely escaped with single quote
        assertTrue(csv.contains("'=cmd|' /C calc'!A0"))
    }
}
