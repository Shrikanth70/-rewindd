# Lean Link Vault & Recall Deck Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans (native inline execution) to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Transform Rewindd into an offline-first link saving and recall vault by stripping all app tracking, AI/Groq APIs, and Insights screen, elevating the "Paste a link" input to the top of Home, and fixing the Rewind card geometry with internal scrolling so action and navigation buttons never compress.

**Architecture:** 3-tab Jetpack Compose navigation (`Home`, `Rewind`, `Library`). Fast local OpenGraph metadata extraction (`UrlMetadataExtractor`) without remote LLM dependencies. Flexible-weight card deck container with scrollable card body and permanently anchored bottom control bars.

**Tech Stack:** Kotlin, Jetpack Compose, Room SQLite, OkHttp (Local OpenGraph extraction), StateFlow, Coroutines.

---

### Task 1: Decommission AI Network Layer & Settings

**Files:**
- Delete: `app/src/main/java/com/rewindd/app/core/network/direct/GroqApiClient.kt`
- Delete: `app/src/main/java/com/rewindd/app/core/network/AiKeyManager.kt`
- Modify: `app/src/main/java/com/rewindd/app/feature/settings/SettingsScreen.kt`
- Modify: `app/src/main/java/com/rewindd/app/feature/ai/AiViewModel.kt`

- [ ] **Step 1: Delete `GroqApiClient.kt` and `AiKeyManager.kt`**
Remove remote AI calls and API key management from the project.

- [ ] **Step 2: Clean up `SettingsScreen.kt`**
Remove AI model selector, "Enter Groq API Key" input, and "Test Connection" button from the Settings UI. Retain only Data Export (JSON), Browser Open preference, Appearance, and About.

- [ ] **Step 3: Simplify `AiViewModel.kt`**
Ensure link extraction relies solely on local metadata parsing (`UrlMetadataExtractor`) with executive briefing fallback and local tag assignment, removing all calls to remote Groq endpoints.

- [ ] **Step 4: Verify Compilation**
Run: `.\gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL (no missing symbols from AI removal).

- [ ] **Step 5: Commit (check auto_commit)**
Check `.agent/config.yml` for `auto_commit` setting. If `false`, skip commit and staging.

---

### Task 2: Strip App Tracking & Screen Dwell Components

**Files:**
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/main/java/com/rewindd/app/feature/home/HomeViewModel.kt`
- Modify: `app/src/main/java/com/rewindd/app/feature/settings/SettingsScreen.kt`
- Delete: `app/src/main/java/com/rewindd/app/feature/home/components/CalendarDwellStrip.kt`
- Delete: `app/src/main/java/com/rewindd/app/feature/home/components/TodayScreenDwellCard.kt`
- Delete: `app/src/main/java/com/rewindd/app/feature/home/components/UsagePermissionBanner.kt`
- Delete: `app/src/main/java/com/rewindd/app/feature/settings/TrackedAppsScreen.kt`

- [ ] **Step 1: Remove tracking permissions and components**
Remove `android.permission.PACKAGE_USAGE_STATS` from `AndroidManifest.xml`. Delete `CalendarDwellStrip.kt`, `TodayScreenDwellCard.kt`, `UsagePermissionBanner.kt`, and `TrackedAppsScreen.kt`.

- [ ] **Step 2: Clean up `HomeViewModel.kt`**
Remove `TodayDwellModel`, `CalendarDayUiModel`, and background usage stats query logic. Focus `HomeViewModel` strictly on unreviewed count, pinned collections, and recent saves.

- [ ] **Step 3: Remove Tracked Apps item from `SettingsScreen.kt`**
Remove navigation to Tracked Apps screen.

- [ ] **Step 4: Verify Compilation**
Run: `.\gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit (check auto_commit)**
Check `.agent/config.yml` for `auto_commit` setting. If `false`, skip commit and staging.

---

### Task 3: Remove Insights Screen & Collapse to 3 Bottom Tabs

**Files:**
- Modify: `app/src/main/java/com/rewindd/app/navigation/BottomNavBar.kt`
- Modify: `app/src/main/java/com/rewindd/app/navigation/RewinddNavGraph.kt`
- Delete directory: `app/src/main/java/com/rewindd/app/feature/insights/`

- [ ] **Step 1: Update `BottomNavBar.kt`**
Remove `INSIGHTS("insights", "Insights", Icons.Filled.Insights)` from `TopLevelDestination`. Ensure the bottom bar renders only 3 items: `HOME`, `REWIND`, and `LIBRARY`.

- [ ] **Step 2: Update `RewinddNavGraph.kt`**
Remove the `composable("insights")` route and all associated lambdas.

- [ ] **Step 3: Delete `feature/insights/`**
Remove `InsightsScreen.kt`, `InsightsViewModel.kt`, and all components inside `app/src/main/java/com/rewindd/app/feature/insights/`.

- [ ] **Step 4: Verify Compilation**
Run: `.\gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit (check auto_commit)**
Check `.agent/config.yml` for `auto_commit` setting. If `false`, skip commit and staging.

---

### Task 4: Restructure Home Screen (Elevate Paste a Link)

**Files:**
- Modify: `app/src/main/java/com/rewindd/app/feature/home/HomeScreen.kt`

- [ ] **Step 1: Reorganize LazyColumn item order in `HomeScreen.kt`**
Reorder items to place:
1. Top bar with "REWINDD" header and settings gear.
2. Section 1 (Top-most): **Quick Capture / Paste a Link Card** (auto-detect clipboard, collection selector, Paste & Save buttons).
3. Section 2: **Rewind Status Hero Card** (displays unreviewed count and "Start Rewind →" button).
4. Section 3: **Curated Collections Row** (horizontal scroll with "Manage ⚙" button).
5. Section 4: **Recent Saves Stream** (chronological list of saved items).

- [ ] **Step 2: Remove references to dwell cards and calendar strip**
Remove all unused imports and code previously rendering `CalendarDwellStrip`, `TodayScreenDwellCard`, and `UsagePermissionBanner`.

- [ ] **Step 3: Verify Compilation**
Run: `.\gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit (check auto_commit)**
Check `.agent/config.yml` for `auto_commit` setting. If `false`, skip commit and staging.

---

### Task 5: Fix Rewind Screen & Card Deck Geometry (Anti-Compression)

**Files:**
- Modify: `app/src/main/java/com/rewindd/app/feature/rewind/RewindScreen.kt`
- Modify: `app/src/main/java/com/rewindd/app/feature/rewind/components/RewindCardDeck.kt`

- [ ] **Step 1: Update `RewindScreen.kt` Layout Bounds**
Ensure the outer container gives flexible space (`Modifier.weight(1f)`) to the active card deck while anchoring top header and bottom controls.

- [ ] **Step 2: Add Internal Scrolling to `RewindCardDeck.kt`**
Wrap the card body in `Modifier.verticalScroll(rememberScrollState())`. Fix the height of the 3-button action array (`Archive`, `Open ↗`, `Keep`) to `52.dp` and the secondary navigation row (`← Prev`, guide text, `Next →`) to `36.dp` with permanent anchoring at the bottom.

- [ ] **Step 3: Verify Prev/Next Cycling Behavior**
Confirm that tapping `← Prev` or `Next →` cycles `currentIndex` without altering `isReviewed` or `isArchived`.

- [ ] **Step 4: Verify Compilation**
Run: `.\gradlew.bat compileDebugKotlin`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 5: Commit (check auto_commit)**
Check `.agent/config.yml` for `auto_commit` setting. If `false`, skip commit and staging.

---

### Task 6: Verification & Test Suite Pass

**Files:**
- Modify: `app/src/test/java/...` (Update or remove obsolete tests referencing Insights or AI)

- [ ] **Step 1: Run unit tests**
Run: `.\gradlew.bat testDebugUnitTest`
Expected: BUILD SUCCESSFUL, all unit tests pass.

- [ ] **Step 2: Assemble debug APK**
Run: `.\gradlew.bat assembleDebug`
Expected: BUILD SUCCESSFUL, `app-debug.apk` generated.

- [ ] **Step 3: Deploy to connected device**
Run: `adb -s 10BDCK1H54000CR install -r app\build\outputs\apk\debug\app-debug.apk`
Expected: Success. Launch app and confirm:
1. Home screen opens with "Paste a link" immediately at the top.
2. Bottom bar has exactly 3 tabs: Home, Rewind, Library.
3. Rewind card has smooth internal scrolling and buttons never compress.
4. No AI errors or app tracking permissions requested.

