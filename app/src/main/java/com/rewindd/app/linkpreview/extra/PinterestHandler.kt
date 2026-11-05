package com.rewindd.app.linkpreview.extra

class PinterestHandler : PlatformHandler {

    override fun matches(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("pinterest.com") || lower.contains("pin.it")
    }

    override suspend fun fetch(url: String): String? {
        val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)
        val html = NetworkHelper.fetchHtmlWithUaRotation(
            resolvedUrl,
            listOf(NetworkHelper.UA_FACEBOOK, NetworkHelper.UA_TWITTER, NetworkHelper.UA_MOBILE_CHROME)
        ) ?: return null

        val candidate = MetaTagExtractor.extractImageUrl(html, resolvedUrl)
        return if (candidate != null && NetworkHelper.verifyHttpsImage(candidate)) {
            candidate
        } else {
            null
        }
    }
}

