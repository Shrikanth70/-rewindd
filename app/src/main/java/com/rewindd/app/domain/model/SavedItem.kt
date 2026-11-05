package com.rewindd.app.domain.model

enum class ReviewState {
    UNREVIEWED,
    REVIEWED,
    ARCHIVED
}

enum class MetadataStatus {
    PENDING,
    SUCCESS,
    FAILED,
    OFFLINE
}

fun cleanHtmlEntities(text: String): String {
    if (!text.contains('&')) return text
    return try {
        val hexPattern = java.util.regex.Pattern.compile("&#x([0-9a-fA-F]+);", java.util.regex.Pattern.CASE_INSENSITIVE)
        val decPattern = java.util.regex.Pattern.compile("&#([0-9]+);")
        var result = text
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#x2022;", "•")
            .replace("&#064;", "@")
            .replace("&#39;", "'")
            .replace("&apos;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")

        val hexMatcher = hexPattern.matcher(result)
        val sbHex = StringBuffer()
        while (hexMatcher.find()) {
            val codePoint = hexMatcher.group(1)?.toIntOrNull(16)
            if (codePoint != null && Character.isValidCodePoint(codePoint)) {
                hexMatcher.appendReplacement(sbHex, java.util.regex.Matcher.quoteReplacement(String(Character.toChars(codePoint))))
            }
        }
        hexMatcher.appendTail(sbHex)
        result = sbHex.toString()

        val decMatcher = decPattern.matcher(result)
        val sbDec = StringBuffer()
        while (decMatcher.find()) {
            val codePoint = decMatcher.group(1)?.toIntOrNull(10)
            if (codePoint != null && Character.isValidCodePoint(codePoint)) {
                decMatcher.appendReplacement(sbDec, java.util.regex.Matcher.quoteReplacement(String(Character.toChars(codePoint))))
            }
        }
        decMatcher.appendTail(sbDec)
        sbDec.toString()
    } catch (_: Exception) {
        text
    }
}

data class SavedItem(
    val id: String,
    val url: String,
    val normalizedUrl: String,
    val sourcePackage: String? = null,
    val sourceLabel: String? = null,
    val title: String,
    val thumbnailUri: String? = null,
    val capturedAtUtc: Long,
    val updatedAtUtc: Long,
    val reviewState: ReviewState = ReviewState.UNREVIEWED,
    val isPinned: Boolean = false,
    val collectionId: String? = null,
    val note: String? = null,
    val metadataStatus: MetadataStatus = MetadataStatus.PENDING,
    val archivedAtUtc: Long? = null,
    val contentHash: String? = null,
    val description: String? = null,
    val siteName: String? = null,
    val authorName: String? = null
)
