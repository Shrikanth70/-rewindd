# Design Specification: Rewindd Lean Link Vault & On-Demand Recall

**Date:** 2026-11-03  
**Status:** Approved by User  
**Target Architecture:** Offline-first Native Android (Jetpack Compose + Room SQLite)

---

## 1. Overview & Vision
Transform **Rewindd** from a multi-faceted digital wellness tracker into an ultra-lean, responsive **Link Vault and On-Demand Recall Deck**. The app focuses strictly on two jobs:
1. **Instant Capture:** Save links effortlessly via clipboard paste or Android Share Sheet, assigned to custom collections.
2. **Tactile Recall:** Review and triage unreviewed links in an uncompressed, beautifully proportioned flashcard deck (Keep, Archive, Open, or Preview with Prev/Next).

All background app dwell tracking, usage stats monitoring, AI keys, remote API endpoints, and insights charts are permanently stripped.

---

## 2. Information Architecture & Navigation

### 2.1 Bottom Navigation (3 Tabs)
The bottom navigation bar transitions from 4 destinations down to 3:
1. **Home (`"home"`):** Quick capture console, unreviewed status hero, curated collections carousel, recent saves.
2. **Rewind (`"rewind"`):** Focused recall and triage deck for unreviewed links.
3. **Library (`"library"`):** All saved links, collections organizer, search, and archived items.

*Note:* The `INSIGHTS` route and its bottom navigation item are deleted.

### 2.2 Settings Simplification
Remove all configuration relating to:
- Tracked Apps & Screen Time
- Usage Access Permissions
- Groq / AI API Keys & Models

Retain only:
- Data Export / Backup (JSON)
- Browser opening preference (In-App Tab vs External)
- Appearance / Theme (System / Dark / Light)
- About / Version info

---

## 3. Screen Layouts & User Experience

### 3.1 Home Screen (`HomeScreen.kt`)
- **Top Bar:** "REWINDD" header with total link count pill and settings gear.
- **Quick Paste Console (Top-most Card):**
  - Instant paste button with clipboard URL detection.
  - Horizontally scrollable collection chip selector.
  - High-contrast tactile "Save" button.
- **Rewind Hero Status Card:**
  - Displays `"● X links waiting to recall"`.
  - Tap navigates directly to the Rewind deck.
  - If 0 items: Displays clear state `"All caught up — inbox clear"`.
- **Curated Collections:**
  - Horizontally scrollable row showing all collections sorted by pin priority.
  - `"MANAGE ⚙"` shortcut button to manage or re-order.
- **Recent Saves Stream:**
  - Chronological list of saved links for quick access.
- **Removed from Home:**
  - `CalendarDwellStrip` (7-day activity strip)
  - `TodayScreenDwellCard` (screen dwell time card)
  - `UsagePermissionBanner`

### 3.2 Rewind Screen & Card Geometry (`RewindScreen.kt`, `RewindCardDeck.kt`)
Eliminate the card squishing and button compression on long cards:
- **Scaffold:** The main screen Column anchors the Top Header and Bottom Navigation Row, allocating flexible space to the deck using `Modifier.weight(1f)`.
- **Card Body Internal Scroll:** The signature card's internal content is wrapped in `Modifier.fillMaxSize().verticalScroll(rememberScrollState())`.
- **Fixed Action Controls:**
  - **Action Array (52dp height):** `[ ← Archive ]` (White), `[ Open ↗ ]` (Blue), `[ Keep → ]` (Signal Green).
  - **Navigation Row (36dp height):** `[ ← Prev ]` (Pill), `"Preview with Prev / Next • Decide with Keep / Archive"`, `[ Next → ]` (Pill).
  - Buttons remain 100% visible, fully proportioned, and stationary regardless of thumbnail size or text length.
- **Recall Interaction:**
  - `Prev` / `Next`: Cycles through items without changing review state.
  - `Keep`: Marks `isReviewed = true`, keeping it active in Library.
  - `Archive`: Marks `isArchived = true`, moving out of inbox with undo banner.

---

## 4. Metadata Extraction & AI Removal

### 4.1 Complete AI Decommissioning
- Delete `GroqApiClient.kt` and `AiKeyManager.kt`.
- Remove all Groq API endpoints, fallback models, and network requests.
- Zero API connection errors; no external network latency.

### 4.2 Local-First OpenGraph Extractor
- `UrlMetadataExtractor` runs in a background IO coroutine:
  - Fetches OpenGraph tags (`og:title`, `og:description`, `og:image`) and standard HTML `<title>` / `<meta>`.
  - Parses domain name and favicon cleanly.
  - Automatically tags based on domain (e.g., `youtube.com` → `#video`, `github.com` → `#dev`).
  - Finishes within 100–300ms locally without third-party services.

---

## 5. Deletions & Cleanups

### 5.1 Removed Code & Files
- `feature/insights/` package (all files deleted or disconnected from navigation).
- `feature/home/components/CalendarDwellStrip.kt`
- `feature/home/components/TodayScreenDwellCard.kt`
- `feature/home/components/UsagePermissionBanner.kt`
- `feature/settings/TrackedAppsScreen.kt`
- `core/network/direct/GroqApiClient.kt`
- `core/network/AiKeyManager.kt`
- `android.permission.PACKAGE_USAGE_STATS` (clean up AndroidManifest).

### 5.2 Verification Criteria
1. `testDebugUnitTest` passes 100% with all obsolete tests removed or updated.
2. `assembleDebug` builds the APK cleanly without compilation errors or missing symbols.
3. Connected device install succeeds and runs without crashing.

