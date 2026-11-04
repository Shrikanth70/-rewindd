# Rewindd: Design System (v0.1)

Oct 13, 2026 · @Shri

## Platform check

Rewindd is a brutalist app that sits as a floating button over other apps, saves reels and links for later, and tracks time per app. The floating-bubble concept works fully on Android; iOS needs a share-sheet and shortcut equivalent.

| Feature | Android | iOS |
| --- | --- | --- |
| Floating bubble over other apps | Yes, via the overlay permission (`SYSTEM_ALERT_WINDOW`) | Not possible |
| Per-app screen time | Yes, via `UsageStatsManager` (user grants Usage Access) | Limited: Screen Time / FamilyControls API, no raw access |
| Saving a reel | Share sheet to Rewindd, or paste a link | Share extension to Rewindd, or paste a link |
| Auto-capturing what you watched | Only through an Accessibility Service, which Play Store policy treats as risky | Not possible |

Build Android first, or design the bubble as one capture entry point with a platform-specific implementation.

## Product model

Rewindd does four things: capture content, review it later, track time per app, and let the user control what is watched.

- **Capture**: bubble, share sheet, or paste a link. The item lands in Inbox with its source app, thumbnail, title, and timestamp.
- **Rewind**: a swipe-through review mode (like the "1 of 4" flow in the reference video), so saved reels get watched instead of piling up.
- **Track**: per-app time, sessions, and pickups, shown on the calendar strip.
- **Permissions**: a first-run "Add apps" screen. Each app is a row with a toggle, so the user controls what is tracked.

## Design tokens

The language is neo-brutalist: thick black outlines, hard offset shadows, flat saturated color, and chunky grotesk type, with a warm paper background instead of pure white.

### Color

| Token | Value | Use |
| --- | --- | --- |
| `paper` | `#F4EFE6` | App background |
| `ink` | `#111111` | All borders, text, shadows |
| `surface` | `#FFFFFF` | Cards |
| `rewind-yellow` | `#FFC53D` | Primary action, "now" |
| `signal-green` | `#2EC27E` | Success, inbox notice |
| `hot-pink` | `#FF4D8D` | Pinned, destructive-adjacent |
| `blue` | `#3B6CFF` | Info, links |
| `orange` | `#FF7A1A` | Streaks, highlights |
| `lilac` | `#B58CFF` | Tags |

Each tracked app also gets a collection color from this palette.

### Typography

Headings use a wide grotesk such as Space Grotesk, Archivo Black, or Bricolage Grotesque. Body uses Inter or DM Sans.

| Token | Size / weight |
| --- | --- |
| `display` | 32 / 800 |
| `title` | 20 / 700 |
| `body` | 14 / 500 |
| `label` | 11 / 700, uppercase, +4% tracking |
| `numeral` | 28 / 800, tabular figures (stats) |

### Borders, shadows, spacing, motion

- Border width: 2px standard, 3px for emphasis.
- Radius: 8px small, 12px medium, used consistently.
- `shadow-hard`: `4px 4px 0 ink` for cards and buttons. `shadow-pressed`: `1px 1px 0 ink` with the element moved 3px.
- Spacing scale: 4, 8, 12, 16, 24, 32.
- Motion: press 80ms linear (the shadow collapses), card swipe 220ms ease-out, bubble expand 180ms with slight overshoot. Respect the reduced-motion setting.

## Components

The floating bubble is the new core piece. The rest adapt patterns from the reference app (folder cards, link cards, swipe deck) and add a calendar strip and app-permission rows.

### Floating bubble

| Variant | Use when |
| --- | --- |
| Idle | Collapsed 48px circle, yellow fill, ink border, hard shadow. Snaps to the screen edge and turns semi-transparent after 3s. |
| Expanded | Radial or vertical menu: Save this, Open Rewindd, Timer, Hide. |
| Saved | Brief checkmark flash plus haptic feedback, then back to idle. |
| Paused | Grey fill, shown when tracking is off for the current app. |

The bubble never covers the keyboard, video controls, or a reel's action rail. The user picks left or right edge and size, and can always dismiss it.

### Calendar strip (top of Home)

- A 7-day row of cells. Each cell has a day letter and a fill intensity (4 levels of yellow) showing screen time or saves.
- Tap a day to open a bottom sheet with that day's saves and per-app breakdown.
- Today has a 3px border. Streak days get a small orange corner flag.
- Each cell carries a text value such as "3h 20m", so color is never the only signal.

### App-permission row

App icon, name, a "tracks time" toggle, a "can save from here" toggle, and an optional daily limit. States: off, on, needs permission (red outline plus a Grant button), limit reached.

### Collection folder card

The tab color is the collection color. It shows the link count and a NEW badge. Variants: default, pinned (pink tab), empty (dashed outline).

### Link card

Thumbnail, source badge (Instagram, X, TikTok), tag chip, title, and a state badge (NEW, WATCHED, PINNED). Long-press opens the share, edit, and delete radial menu.

### Rewind deck

A segmented progress bar, a stacked card, and a back / open-original / next control row with an Exit button. Swipe right to keep, swipe left to archive, tap the yellow arrow to open the original in its app.

### Stat tile

A numeral with a label and a comparison, for example "Instagram, 2h 14m, up 12% vs last week". The Library summary block is built from four of these.

### Buttons

| Variant | Use |
| --- | --- |
| Primary | Yellow, the main action on a screen |
| Secondary | White, supporting actions |
| Destructive | Pink, delete and remove |
| Icon | Square 40px, toolbar actions |

Every button has default, pressed, disabled, and loading states, with a 44px minimum touch target.

### Inputs and feedback

Paste-link field with a clipboard button, tag input, search with cancel, a toast (ink background, yellow text), and a confirm dialog with hard-shadow buttons.

## Screens

Six screens cover the full loop from setup to review.

1. **Onboarding**: grant overlay permission, grant usage access, choose apps.
2. **Home**: calendar strip, today's stats, the inbox notice (for example "12 new saves waiting"), and collections.
3. **Library**: search, filter by app, tag, or date, and a link grid.
4. **Insights**: per-app time list, weekly trend, and most-saved sources.
5. **Rewind deck**: swipe review of saved items.
6. **Settings**: bubble behavior, app list, data export, and privacy.

## Accessibility

Ink on paper gives roughly 16:1 contrast, but yellow-on-white pairings fail, so always put ink text on yellow.

- Every bubble action needs a TalkBack or VoiceOver label.
- Never communicate state by color alone; add an icon or text.
- Swipe gestures in the Rewind deck need button alternatives (back and next).
- Keep a 44px minimum touch target on all controls.

## Trust and privacy

An app that watches other apps has to feel safe, so Rewindd keeps all data on the device by default.

- Show a "what Rewindd can see" screen during onboarding.
- Add a one-tap "pause everything" control inside the bubble.
- Let the user export or delete all data from Settings.

## Open questions

- [ ] Android first, or both platforms?
- [ ] Is auto-capturing reels (without the user pressing save) a real requirement? It carries the most policy risk.
- [ ] Should usage tracking drive limits and wellbeing nudges, or just reporting?
- [ ] Keep the name "Rewindd" with the double d? Check that the handle and domain are available.
- [ ] Next deliverable: a clickable HTML prototype (Home, bubble, Rewind deck) or a token file (CSS variables or a Flutter/Compose theme)?
