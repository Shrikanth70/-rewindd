package com.rewindd.app.core.designsystem.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// ==============================================================================
// BOTANICAL PALETTE SYSTEM (Dual Sanctuary: Bright Ivory Porcelain & Dark Noir)
// ==============================================================================

// ------------------------------------------------------------------------------
// 1. NOCTURNAL BOTANICAL NOIR PALETTE (Dark Theme)
// ------------------------------------------------------------------------------
val DarkCanvasNoir = Color(0xFF0F1511)            // Deep nocturnal olive-charcoal base canvas
val DarkSurfaceVitrine = Color(0xFF18221B)         // Elevated squircle container / cards
val DarkSurfaceTray = Color(0xFF131D16)            // Recessed museum matting tray
val DarkSurfaceElevated = Color(0xFF222E26)        // Floating toolbars, chips, inputs
val DarkSurfaceGlassDock = Color(0xFF161F18)       // Floating bottom capsule navigation dock

val DarkTextParchment = Color(0xFFF4EFEA)          // Primary headlines, titles, active text
val DarkTextChampagne = Color(0xFFE5D5BA)          // Accent headlines, star marks, primary CTAs
val DarkTextDewSage = Color(0xFFC8CEC9)            // Subtitles, excerpts, secondary text
val DarkTextLichenMuted = Color(0xFF819185)        // Auxiliary metadata, timestamps, domain tags

val DarkChampagneGold = Color(0xFFE5D5BA)          // Primary action fill (Open Specimen / Save)
val DarkDewSage = Color(0xFF729E74)                // Verified indicators, kept badges, active tabs
val DarkCypressGreen = Color(0xFF2C3E32)           // Chip fills, secondary pill buttons
val DarkWarmAmber = Color(0xFFDDA15E)              // YouTube duration pills, stars, ambient glow
val DarkRoseSignal = Color(0xFFC97A7E)             // Archive / Destructive gentle signal

// ------------------------------------------------------------------------------
// 2. LUMINOUS BOTANICAL IVORY PALETTE (Bright / Light Theme - Default)
// ------------------------------------------------------------------------------
val LightCanvasNoir = Color(0xFFF9F7F3)           // Warm soft ivory-linen canvas
val LightSurfaceVitrine = Color(0xFFFFFFFF)        // Pure luminous porcelain card
val LightSurfaceTray = Color(0xFFF2ECE4)           // Recessed soft alabaster tray
val LightSurfaceElevated = Color(0xFFE9E2D8)       // Tactile warm stone chips & inputs
val LightSurfaceGlassDock = Color(0xFFFFFFFF)      // Floating clean ivory dock

val LightTextParchment = Color(0xFF141D16)         // Deep nocturnal olive-noir for primary text (14:1 contrast)
val LightTextChampagne = Color(0xFF7A551E)         // Burnished antique brass accent (5.6:1 contrast)
val LightTextDewSage = Color(0xFF2C4031)           // Rich herbal slate for subtitles & secondary copy (8:1 contrast)
val LightTextLichenMuted = Color(0xFF4E6052)       // Rich lichen sage for metadata & timestamps (5.5:1 contrast)

val LightChampagneGold = Color(0xFFEAD8BF)         // Warm champagne sand for primary CTAs / badges
val LightDewSage = Color(0xFF2E5D36)               // Crisp vibrant botanical sage green (5.6:1 contrast)
val LightCypressGreen = Color(0xFFE0E8E1)          // Delicate eucalyptus tint for chips
val LightWarmAmber = Color(0xFFB87222)             // Rich warm amber for stars & media tags
val LightRoseSignal = Color(0xFFBF4850)            // Soft rose red for destructive actions

val ContentOnChampagne = Color(0xFF141D16)         // Permanent AAA charcoal text/icon for ChampagneGold buttons
val IconGold: Color get() = if (isDarkThemeGlobal) DarkChampagneGold else Color(0xFF8A5D24)
val IconBotanical: Color get() = if (isDarkThemeGlobal) DarkDewSage else Color(0xFF2E5D36)
val IconNeutral: Color get() = if (isDarkThemeGlobal) DarkTextLichenMuted else Color(0xFF4E6052)

// ------------------------------------------------------------------------------
// 3. REACTIVE COMPOSE STATE PALETTE (Seamless snapshot updates across all scopes)
// Default initialized to Bright theme as requested by the user
// ------------------------------------------------------------------------------
var CanvasNoir by mutableStateOf(LightCanvasNoir)
var SurfaceVitrine by mutableStateOf(LightSurfaceVitrine)
var SurfaceTray by mutableStateOf(LightSurfaceTray)
var SurfaceElevated by mutableStateOf(LightSurfaceElevated)
var SurfaceGlassDock by mutableStateOf(LightSurfaceGlassDock)

var TextParchment by mutableStateOf(LightTextParchment)
var TextChampagne by mutableStateOf(LightTextChampagne)
var TextDewSage by mutableStateOf(LightTextDewSage)
var TextLichenMuted by mutableStateOf(LightTextLichenMuted)

var ChampagneGold by mutableStateOf(LightChampagneGold)
var DewSage by mutableStateOf(LightDewSage)
var CypressGreen by mutableStateOf(LightCypressGreen)
var WarmAmber by mutableStateOf(LightWarmAmber)
var RoseSignal by mutableStateOf(LightRoseSignal)
var isDarkThemeGlobal by mutableStateOf(false)

fun updateRewinddTheme(darkTheme: Boolean) {
    isDarkThemeGlobal = darkTheme
    if (darkTheme) {
        CanvasNoir = DarkCanvasNoir
        SurfaceVitrine = DarkSurfaceVitrine
        SurfaceTray = DarkSurfaceTray
        SurfaceElevated = DarkSurfaceElevated
        SurfaceGlassDock = DarkSurfaceGlassDock
        TextParchment = DarkTextParchment
        TextChampagne = DarkTextChampagne
        TextDewSage = DarkTextDewSage
        TextLichenMuted = DarkTextLichenMuted
        ChampagneGold = DarkChampagneGold
        DewSage = DarkDewSage
        CypressGreen = DarkCypressGreen
        WarmAmber = DarkWarmAmber
        RoseSignal = DarkRoseSignal
    } else {
        CanvasNoir = LightCanvasNoir
        SurfaceVitrine = LightSurfaceVitrine
        SurfaceTray = LightSurfaceTray
        SurfaceElevated = LightSurfaceElevated
        SurfaceGlassDock = LightSurfaceGlassDock
        TextParchment = LightTextParchment
        TextChampagne = LightTextChampagne
        TextDewSage = LightTextDewSage
        TextLichenMuted = LightTextLichenMuted
        ChampagneGold = LightChampagneGold
        DewSage = LightDewSage
        CypressGreen = LightCypressGreen
        WarmAmber = LightWarmAmber
        RoseSignal = LightRoseSignal
    }
}

// ------------------------------------------------------------------------------
// 4. BACKWARDS COMPATIBILITY ALIASES
// ------------------------------------------------------------------------------
val Paper: Color get() = CanvasNoir
val Ink: Color get() = TextParchment
val Surface: Color get() = SurfaceVitrine
val SurfaceContainerLowest: Color get() = SurfaceTray
val SurfaceContainerLow: Color get() = SurfaceTray
val SurfaceContainer: Color get() = SurfaceVitrine
val SurfaceContainerHigh: Color get() = SurfaceElevated
val SurfaceContainerHighest: Color get() = SurfaceElevated
val OnSurfaceVariant: Color get() = TextDewSage
val Outline = Color(0x3388998C)
val OutlineVariant = Color(0x2B88998C)

val PaperDark = DarkCanvasNoir
val InkDark = DarkTextParchment
val SurfaceDark = DarkSurfaceVitrine
val SurfaceContainerLowestDark = DarkSurfaceTray
val SurfaceContainerLowDark = DarkSurfaceTray
val SurfaceContainerDark = DarkSurfaceVitrine
val SurfaceContainerHighDark = DarkSurfaceElevated
val SurfaceContainerHighestDark = Color(0xFF26332A)
val OnSurfaceVariantDark = DarkTextDewSage
val OutlineDark = Color(0xFF2B3A30)
val OutlineVariantDark = Color(0xFF1E2822)

val RewindYellow: Color get() = ChampagneGold
val PrimaryContainer: Color get() = CypressGreen
val OnPrimaryContainer: Color get() = TextChampagne
val LinkBlue: Color get() = DewSage
val SoftBlue: Color get() = DewSage
val SecondaryContainer: Color get() = CypressGreen
val HotPink: Color get() = RoseSignal
val TertiaryContainer = Color(0xFF382326)
val OnTertiaryContainer: Color get() = RoseSignal
val SignalGreen: Color get() = DewSage
val StreakOrange: Color get() = WarmAmber
val TagLilac: Color get() = DewSage
val Lilac: Color get() = DewSage
val InactiveGrey = Color(0xFF2B3A30)
val NeoMint: Color get() = DewSage
val ElectricButter: Color get() = ChampagneGold
val HotCoral: Color get() = RoseSignal
