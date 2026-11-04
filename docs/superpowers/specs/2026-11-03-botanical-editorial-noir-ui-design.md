# UI Specification: Botanical Editorial Noir

**Date:** 2026-11-03  
**Status:** Approved by User  
**Target Architecture:** Offline-first Native Android (Jetpack Compose + Material 3)  
**Style Movement:** Luxury Botanical Editorial Noir (100% Anti-Neo-Brutalist)

---

## 1. Executive Summary & Aesthetic Floor

Transform Rewindd from a comic-styled neo-brutalist interface into a **private, luxury curatorial sanctuary**. The visual language pairs the atmospheric calm of a nocturnal botanical garden with the typographic restraint of an archival art publication.

### Hard Floor (Zero Neo-Brutalism):
1. **No Hard Black Borders:** Completely eliminate `border(2.dp, Ink)` and heavy outline strokes.
2. **No Flat Offset Drop-Shadows:** Completely eliminate `hardShadow(offset = 2.dp, color = Ink)`.
3. **No Garish Pop Colors:** Eliminate primary yellow / hot pink / raw black comic contrasts.
4. **No Boxy Angular Geometry:** Replaced with smooth organic squircles (`24.dp` to `28.dp` radius) and continuous capsule pills (`999.dp`).

---

## 2. Color Tokens & Palette System

### 2.1 Core Palette
```kotlin
// Botanical Editorial Noir Canvas & Surfaces
val CanvasNoir = Color(0xFF0F1511)            // Deep nocturnal olive-charcoal base
val SurfaceVitrine = Color(0xFF18221B)         // Elevated squircle container
val SurfaceTray = Color(0xFF131D16)            // Recessed museum matting tray
val SurfaceElevated = Color(0xFF222E26)        // Floating toolbars, chips, inputs
val SurfaceGlassDock = Color(0xFF161F18)       // Floating bottom capsule navigation dock

// Text & Typography Hierarchy
val TextParchment = Color(0xFFF4EFEA)          // Primary headlines, titles, active text
val TextChampagne = Color(0xFFE5D5BA)          // Accent headlines, star marks, primary CTAs
val TextDewSage = Color(0xFFC8CEC9)            // Subtitles, excerpts, secondary text
val TextLichenMuted = Color(0xFF819185)        // Auxiliary metadata, timestamps, domain tags

// Accent Accents & Chromatic Signals
val ChampagneGold = Color(0xFFE5D5BA)          // Primary action fill (Open Specimen / Save)
val DewSage = Color(0xFF729E74)                // Verified indicators, kept badges, active tabs
val CypressGreen = Color(0xFF2C3E32)           // Chip fills, secondary pill buttons
val WarmAmber = Color(0xFFDDA15E)              // YouTube duration pills, stars, ambient glow
val RoseSignal = Color(0xFFC97A7E)             // Archive / Destructive gentle signal
```

---

## 3. Depth, Lighting & Vitrine Framing

### 3.1 Atmospheric Ambient Lighting
Replaces hard shadows with soft, multi-layered diffuse lighting:
```kotlin
fun Modifier.ambientGlow(
    color: Color = Color(0xFF4E6855).copy(alpha = 0.16f),
    radius: Dp = 24.dp,
    offsetY: Dp = 8.dp
): Modifier
```

### 3.2 Frosted Vitrine Rim Highlight
Replaces solid black borders with an ethereal top-lit rim highlight:
```kotlin
fun Modifier.frostedRimHighlight(
    shape: Shape = RoundedCornerShape(26.dp)
): Modifier // 1px stroke transitioning from rgba(229, 213, 186, 0.22) down to rgba(78, 104, 85, 0.06)
```

### 3.3 The 4 Media Harmonization Treatments
Ensures multi-colored YouTube covers, Instagram reels, and web article thumbnails blend seamlessly:
1. **Vitrine Matte Framing:** Image covers sit inside a `SurfaceTray` with a 6dp inset and 18dp squircle curvature.
2. **Deep Gradient Scrim:** Lower 45% of cover images transitions via vertical gradient (`Transparent -> SurfaceVitrine`) so serif titles sit in 100% contrast.
3. **Chromatic Ambient Glow:** Cards project a diffused 12% ambient glow matching their dominant tone (e.g. warm amber for video, sage for articles).
4. **Micro-Tonal Warmth Pass:** Subtle 4% champagne-gold overlay (`Color(0xFFE5D5BA).copy(alpha = 0.04f)`) over thumbnails to soften harsh web clipping.

---

## 4. Typography System

- **Display & Titles:** `FontFamily.Serif` (Editorial Serif style), letter-spacing `-0.015em`, line-height `1.15`.
- **Body & Excerpts:** `FontFamily.SansSerif`, letter-spacing `0em`, line-height `1.5`.
- **Labels, Tags & Chips:** `FontFamily.SansSerif`, uppercase with `0.06em` to `0.08em` optical tracking.
- **Numbers & Readouts:** Tabular numerals with mono markers for durations (`▶ 18:42`).

---

## 5. Screen Components & Layouts

### 5.1 Floating Obsidian Capsule Dock (`BottomNavBar.kt`)
- Detached, centered capsule floating `16.dp` above screen bottom.
- Surface: `SurfaceGlassDock.copy(alpha = 0.90f)` with `28.dp` corner radius and frosted rim highlight.
- Active item encased in a `DewSage.copy(alpha = 0.25f)` squircle with `TextChampagne` icon and label.

### 5.2 Home Screen (`HomeScreen.kt`)
- **Header:** Date in tracked uppercase (`WEDNESDAY, OCT 24`), serif wordmark (`Rewindd`), quick capture button.
- **Hero Rewind Card:** 4:5 vertical squircle card with extracted cover image, gradient scrim, serif title, and floating capsule action row (`Archive`, `Open Specimen ↗`, `Keep`).
- **Curated Decks Rail:** Soft horizontal scrollable squircle tiles with custom emojis and live count badges.
- **Recent Captures Feed:** Editorial cards for YouTube videos (16:9 with duration badge), X threads, and Instagram reels.

### 5.3 Rewind Card Deck (`RewindScreen.kt`, `RewindCardDeck.kt`)
- Focused, distraction-free recall stage.
- Elastic spring physics on drag with 3D perspective tilt (`rotationZ` and `rotationY`).
- Pinned bottom action bar with frosted capsule buttons.

### 5.4 Library (`LibraryScreen.kt`, `TaggedCollectionsRail.kt`)
- Curated collection rails with personal folder emojis.
- Staggered editorial media cards showing extracted cover imagery.

---

## 6. Verification & Quality Gates
1. `compileDebugKotlin` — Clean compilation.
2. `testDebugUnitTest` — 100% test pass.
3. `assembleDebug` — APK assembly.
4. Device testing on `10BDCK1H54000CR`.

