package com.rewindd.app.domain.export

import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.Tag
import com.rewindd.app.domain.model.UserPreferences
import org.json.JSONArray
import org.json.JSONObject

object VaultDataExporter {

    fun exportToJson(
        items: List<SavedItemDetailsDomain>,
        collections: List<Collection>,
        tags: List<Tag>,
        preferences: UserPreferences? = null,
        exportedAtUtc: Long = System.currentTimeMillis()
    ): String {
        val root = JSONObject()
        root.put("schemaVersion", 1)
        root.put("appName", "Rewindd")
        root.put("exportedAtUtc", exportedAtUtc)

        val collectionsArray = JSONArray()
        collections.forEach { col ->
            val obj = JSONObject()
            obj.put("id", col.id)
            obj.put("name", col.name)
            obj.put("colorToken", col.colorToken)
            obj.put("isPinned", col.isPinned)
            obj.put("sortOrder", col.sortOrder)
            collectionsArray.put(obj)
        }
        root.put("collections", collectionsArray)

        val tagsArray = JSONArray()
        tags.forEach { tag ->
            val obj = JSONObject()
            obj.put("id", tag.id)
            obj.put("displayName", tag.displayName)
            obj.put("normalizedName", tag.normalizedName)
            obj.put("colorToken", tag.colorToken ?: "")
            tagsArray.put(obj)
        }
        root.put("tags", tagsArray)

        val itemsArray = JSONArray()
        items.forEach { detail ->
            val item = detail.item
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("url", item.url)
            obj.put("normalizedUrl", item.normalizedUrl)
            obj.put("title", item.title ?: "")
            obj.put("sourcePackage", item.sourcePackage ?: "")
            obj.put("sourceLabel", item.sourceLabel ?: "")
            obj.put("capturedAtUtc", item.capturedAtUtc)
            obj.put("reviewState", item.reviewState.name)
            obj.put("isPinned", item.isPinned)
            obj.put("note", item.note ?: "")
            obj.put("collectionId", item.collectionId ?: "")
            obj.put("collectionName", detail.collection?.name ?: "")

            val itemTags = JSONArray()
            detail.tags.forEach { t -> itemTags.put(t.displayName) }
            obj.put("tags", itemTags)

            itemsArray.put(obj)
        }
        root.put("items", itemsArray)

        if (preferences != null) {
            val prefObj = JSONObject()
            prefObj.put("trackedApps", JSONArray(preferences.trackedApps.toList()))
            prefObj.put("retentionMonths", preferences.retentionMonths)
            prefObj.put("remindersEnabled", preferences.remindersEnabled)
            root.put("preferences", prefObj)
        }

        return root.toString(2)
    }

    fun exportToMarkdown(
        items: List<SavedItemDetailsDomain>,
        collections: List<Collection>,
        tags: List<Tag>
    ): String {
        val sb = StringBuilder()
        sb.append("# Rewindd Vault Export\n\n")
        sb.append("Total Items: ${items.size} | Collections: ${collections.size} | Tags: ${tags.size}\n\n")

        sb.append("## Collections\n")
        if (collections.isEmpty()) {
            sb.append("_No collections created._\n\n")
        } else {
            collections.forEach { col ->
                sb.append("- **${col.name}**\n")
            }
            sb.append("\n")
        }

        sb.append("## Saved Items\n\n")
        if (items.isEmpty()) {
            sb.append("_No saved items found._\n")
        } else {
            items.forEach { detail ->
                val item = detail.item
                sb.append("### [${item.title ?: item.url}](${item.url})\n")
                if (!item.note.isNullOrBlank()) {
                    sb.append("> ${item.note}\n\n")
                }
                sb.append("- **Status:** ${item.reviewState.name}\n")
                if (detail.collection != null) {
                    sb.append("- **Collection:** ${detail.collection.name}\n")
                }
                if (detail.tags.isNotEmpty()) {
                    sb.append("- **Tags:** ${detail.tags.joinToString(" ") { it.displayName }}\n")
                }
                sb.append("- **Captured:** ${java.time.Instant.ofEpochMilli(item.capturedAtUtc)}\n\n---\n\n")
            }
        }
        return sb.toString()
    }

    private fun sanitizeCsvField(field: String): String {
        var clean = field
        if (clean.startsWith("=") || clean.startsWith("+") || clean.startsWith("-") || clean.startsWith("@") || clean.startsWith("\t") || clean.startsWith("\r")) {
            clean = "'$clean"
        }
        if (clean.contains(",") || clean.contains("\"") || clean.contains("\n") || clean.contains("\r")) {
            clean = "\"${clean.replace("\"", "\"\"")}\""
        }
        return clean
    }

    fun exportScreenTimeToCsv(
        usageList: List<com.rewindd.app.domain.model.DailyAppUsage>
    ): String {
        val sb = StringBuilder()
        sb.append("Date,PackageName,DwellMinutes,DwellMillis,Sessions,Pickups,Status\n")
        usageList.forEach { u ->
            val mins = u.usageMillis / 60000.0
            val formattedMins = String.format(java.util.Locale.US, "%.1f", mins)
            val date = sanitizeCsvField(u.dateLocal)
            val pkg = sanitizeCsvField(u.packageName)
            sb.append("$date,$pkg,$formattedMins,${u.usageMillis},${u.sessionCount ?: 0},${u.pickupCount ?: 0},${u.sourceStatus.name}\n")
        }
        return sb.toString()
    }
}
