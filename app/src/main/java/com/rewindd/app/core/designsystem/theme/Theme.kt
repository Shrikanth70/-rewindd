package com.rewindd.app.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

val RewinddDarkColorScheme = darkColorScheme(
    primary = DarkChampagneGold,
    onPrimary = DarkCanvasNoir,
    primaryContainer = DarkCypressGreen,
    onPrimaryContainer = DarkTextChampagne,
    secondary = DarkDewSage,
    onSecondary = DarkCanvasNoir,
    secondaryContainer = DarkCypressGreen,
    tertiary = DarkWarmAmber,
    onTertiary = DarkCanvasNoir,
    tertiaryContainer = Color(0xFF382326),
    onTertiaryContainer = DarkRoseSignal,
    background = DarkCanvasNoir,
    onBackground = DarkTextParchment,
    surface = DarkSurfaceVitrine,
    onSurface = DarkTextParchment,
    surfaceVariant = DarkSurfaceTray,
    onSurfaceVariant = DarkTextDewSage,
    error = DarkRoseSignal,
    onError = DarkCanvasNoir,
    outline = Color(0xFF2B3A30),
    outlineVariant = Color(0xFF1E2822)
)

val RewinddLightColorScheme = lightColorScheme(
    primary = LightChampagneGold,
    onPrimary = LightCanvasNoir,
    primaryContainer = LightCypressGreen,
    onPrimaryContainer = LightTextChampagne,
    secondary = LightDewSage,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = LightCypressGreen,
    tertiary = LightWarmAmber,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFDE8E8),
    onTertiaryContainer = LightRoseSignal,
    background = LightCanvasNoir,
    onBackground = LightTextParchment,
    surface = LightSurfaceVitrine,
    onSurface = LightTextParchment,
    surfaceVariant = LightSurfaceTray,
    onSurfaceVariant = LightTextDewSage,
    error = LightRoseSignal,
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFFD6D0C5),
    outlineVariant = Color(0xFFE5E0D6)
)

val RewinddColorScheme = RewinddLightColorScheme

@Composable
fun RewinddTheme(
    darkTheme: Boolean = false, // User requested: Default is bright theme
    content: @Composable () -> Unit
) {
    SideEffect {
        updateRewinddTheme(darkTheme)
    }

    val colorScheme = if (darkTheme) RewinddDarkColorScheme else RewinddLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RewinddTypography,
        content = content
    )
}
