# Platform Preview Resolvers (`linkpreview.extra`)

This package provides strictly additive cover-image resolution for platforms where standard OpenGraph extraction fails or requires specialized handling (e.g., bot User-Agents, redirect unrolling, or public oEmbed endpoints).

## Architecture

- `PlatformHandler`: Interface specifying `matches(url: String): Boolean` and `suspend fun fetch(url: String): String?`.
- `PlatformPreviewResolver`: Dispatches incoming URLs to matching platform handlers, maintains an in-memory cache, and never throws.
- `NetworkHelper`: Manages OkHttp client with 8-second timeouts, up to 2 retries with exponential backoff, redirect unrolling, tracking parameter stripping, and HTTPS image validation.
- `MetaTagExtractor`: Parses HTML using Jsoup to extract `og:image:secure_url`, `og:image`, `twitter:image`, `twitter:image:src`, and `link[rel=image_src]`, converting relative URLs to absolute.

## How to Add a New Platform Handler

1. **Create your handler class** implementing `PlatformHandler`:
   ```kotlin
   package com.rewindd.app.linkpreview.extra

   class ExamplePlatformHandler : PlatformHandler {
       override fun matches(url: String): Boolean {
           return url.lowercase().contains("example.com")
       }

       override suspend fun fetch(url: String): String? {
           val resolvedUrl = NetworkHelper.resolveRedirectsAndClean(url)
           val html = NetworkHelper.fetchHtmlWithUaRotation(resolvedUrl) ?: return null
           val imageUrl = MetaTagExtractor.extractImageUrl(html, resolvedUrl)
           return if (imageUrl != null && NetworkHelper.verifyHttpsImage(imageUrl)) imageUrl else null
       }
   }
   ```

2. **Register the handler** in `PlatformPreviewResolver.kt`:
   Add an instance of `ExamplePlatformHandler()` to the `handlers` list.

3. **Add unit tests** in `PlatformPreviewResolverTest.kt` verifying URL matching and HTML extraction.

