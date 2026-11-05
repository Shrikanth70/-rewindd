package com.rewindd.app.linkpreview.extra

class LinkedInHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("linkedin.com") || lower.contains("lnkd.in")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)

        val botUa = "LinkedInBot/1.0 (compatible; Mozilla/5.0; Apache-HttpClient +http://www.linkedin.com)"
        val uas = listOf(botUa, NetworkHelper.UA_FACEBOOK, NetworkHelper.UA_TWITTER)

        val html = NetworkHelper.fetchHtmlWithUaRotation(resolvedUrl, uas) ?: return null

        // If a login wall or authwall appeared, move on gracefully
        if (NetworkHelper.isLoginWall(html) || html.contains("authwall", ignoreCase = true)) {
            return null
        }

        val candidate = MetaTagExtractor.extractImageUrl(html, resolvedUrl)
        return if (candidate != null && NetworkHelper.verifyHttpsImage(candidate)) {
            candidate
        } else {
            null
        }
    }
}

