# Rewindd

> *"Save less. Remember more. Scroll with intention."*

Rewindd is a privacy-first, local-first Android mobile application built with native Kotlin and Jetpack Compose. It combats feed fatigue and mindless content hoarding by replacing passive endless bookmarking with intentional capture, a focused review queue ("Rewind deck"), personal offline vault organization, and transparent screen-time telemetry.

---

## Core Guarantees & Privacy Architecture

- **100% On-Device Persistence:** All saved links, margin notes, tags, and collections live exclusively on your device in local SQLite / Room with FTS5 full-text search.
- **Intent Over Surveillance:** Capture occurs strictly via explicit user actions (Android Share Sheet or in-app Paste). Zero background scraping or feed monitoring.
- **Deterministic Screen Time:** Dwell minutes are aggregated locally from Android's `UsageStatsManager` for user-selected apps. Telemetry never leaves the device.
- **Zero Client Secrets (BYOK or Proxy):** Optional AI summaries/tags run via user-provided API keys (BYOK) or an owned HTTPS proxy. No hardcoded API keys exist inside the application binary.

---

## Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Platform** | Android 8.0+ (API 26 min, API 35 target/compile) |
| **Language & Toolchain** | Kotlin 2.0.21, Java 21, Gradle 8.11 Version Catalog |
| **UI Framework** | Jetpack Compose, Material 3 with Neo-Brutalist design tokens |
| **Database** | Android Jetpack Room (v3, SQLite + FTS5 full-text search) |
| **Background Processing** | Android Jetpack WorkManager |
| **Telemetry** | Android `UsageStatsManager` |
| **AI Cloud Proxy** | TypeScript, Express, Zod, Token Bucket Rate Limiting |

---

## Project Structure

```
rewindd/
├── app/                  # Android native application source code
│   ├── src/main/java/    # Clean architecture layers (core, data, domain, feature, navigation)
│   ├── src/main/res/     # Compose resources, mipmaps, vector drawables
│   └── src/test/java/    # Unit tests (Room migrations, use cases, ViewModels)
├── docs/                 # Complete product, architecture, and security specifications
├── server/
│   └── proxy/            # Secure HTTPS assistive AI proxy for Groq API
└── gradle/               # Version catalog (libs.versions.toml) and wrapper
```

---

## Developer Quickstart

### Prerequisites
- **JDK:** Java 21 LTS (Oracle JDK or Eclipse Temurin)
- **Android SDK:** Platform 35, Build-Tools 35.0.0
- **Node.js:** v20+ (for `server/proxy` development)

### Build & Test Commands

```bash
# Run all unit test suites (debug + release)
./gradlew test

# Run Android Lint analysis
./gradlew lintDebug

# Build Debug APK
./gradlew assembleDebug

# Build Release APK (R8 minified)
./gradlew assembleRelease
```

### AI Proxy Microservice

```bash
cd server/proxy
npm install
npm test
npm run dev
```

---

## Documentation Index

- [Project Overview](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/PROJECT_OVERVIEW.md)
- [Architecture & Data Flow](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/ARCHITECTURE.md)
- [Security & Threat Model](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/SECURITY_AND_PRIVACY.md)
- [Data Model & SQLite Schema](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/DATA_MODEL.md)
- [Design System & Primitives](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/DESIGN_SYSTEM.md)
- [Phase 10 Hardening Report](file:///c:/Users/Bhukya%20Shrikanth/OneDrive/Desktop/rewindd/docs/PHASE_10_HARDENING_REPORT.md)

