package com.rewindd.app.domain.usecase

import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

sealed class UrlValidationResult {
    data class Valid(
        val originalUrl: String,
        val normalizedUrl: String,
        val domain: String
    ) : UrlValidationResult()

    data class Invalid(val reason: String) : UrlValidationResult()
}

object UrlNormalizer {

    private val URL_REGEX = Regex(
        "(https?://[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,})+(?::\\d+)?(?:/[^\\s]*)?)",
        RegexOption.IGNORE_CASE
    )

    private val TRACKING_PARAMS = setOf(
        "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
        "fbclid", "igsh", "si", "gclid", "msclkid", "ref", "ref_src"
    )

    fun extractAndNormalize(input: String?): UrlValidationResult {
        if (input.isNullOrBlank()) {
            return UrlValidationResult.Invalid("URL or text input is empty.")
        }

        val match = URL_REGEX.find(input)
            ?: return UrlValidationResult.Invalid("No valid HTTP or HTTPS URL found in input.")

        val extractedUrl = match.value.trim()

        return try {
            val uri = URI(extractedUrl)
            val scheme = uri.scheme?.lowercase()
            if (scheme != "http" && scheme != "https") {
                return UrlValidationResult.Invalid("Only HTTP and HTTPS URLs are supported.")
            }

            val rawHost = uri.host?.lowercase() ?: return UrlValidationResult.Invalid("Missing host in URL.")
            val canonicalHost = rawHost.removePrefix("www.")

            // Normalize path (ensure lowercase host, remove default ports)
            val path = if (uri.path.isNullOrEmpty()) "/" else uri.path

            // Clean query parameters: filter out tracking params
            val rawQuery = uri.rawQuery
            val cleanedQuery = if (!rawQuery.isNullOrBlank()) {
                val params = rawQuery.split("&")
                    .mapNotNull { pair ->
                        val parts = pair.split("=", limit = 2)
                        val key = parts.getOrNull(0)?.lowercase()
                        if (key != null && key !in TRACKING_PARAMS) {
                            pair
                        } else {
                            null
                        }
                    }
                if (params.isNotEmpty()) {
                    params.sorted().joinToString("&")
                } else null
            } else null

            val normalizedUri = URI(
                scheme,
                uri.userInfo,
                canonicalHost,
                if (uri.port == 80 || uri.port == 443) -1 else uri.port,
                path,
                cleanedQuery,
                null // Strip fragments for canonical identity
            )

            val normalizedString = normalizedUri.toASCIIString().removeSuffix("/")

            UrlValidationResult.Valid(
                originalUrl = extractedUrl,
                normalizedUrl = normalizedString,
                domain = canonicalHost
            )
        } catch (e: Exception) {
            UrlValidationResult.Invalid("Malformed URL: ${e.message}")
        }
    }
}
