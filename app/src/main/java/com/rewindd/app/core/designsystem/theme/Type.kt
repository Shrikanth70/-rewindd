package com.rewindd.app.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// ==============================================================================
// BOTANICAL EDITORIAL NOIR TYPOGRAPHY SYSTEM
// ==============================================================================
val EditorialSerif: FontFamily = FontFamily.Serif
val EditorialSans: FontFamily = FontFamily.SansSerif
val Poppins: FontFamily = FontFamily.SansSerif

// Backwards compatibility aliases
val SpaceGrotesk: FontFamily = EditorialSerif
val DMSans: FontFamily = EditorialSans

val DisplayLarge = TextStyle(
    fontFamily = EditorialSerif,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.02).em,
    color = Color.Unspecified
)

val DisplayMedium = TextStyle(
    fontFamily = EditorialSerif,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.015).em,
    color = Color.Unspecified
)

val HeadlineSmall = TextStyle(
    fontFamily = EditorialSerif,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.01).em,
    color = Color.Unspecified
)

val TitleMedium = TextStyle(
    fontFamily = EditorialSerif,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.005).em,
    color = Color.Unspecified
)

val StatCounter = TextStyle(
    fontFamily = EditorialSans,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    color = Color.Unspecified
)

val BodyLarge = TextStyle(
    fontFamily = EditorialSans,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = Color.Unspecified
)

val BodyMedium = TextStyle(
    fontFamily = EditorialSans,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = Color.Unspecified
)

val LabelSmall = TextStyle(
    fontFamily = EditorialSans,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.02.em,
    color = Color.Unspecified
)

val BadgeCaps = TextStyle(
    fontFamily = EditorialSans,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.08.em,
    color = Color.Unspecified
)

val RewinddTypography = Typography(
    displayLarge = DisplayLarge,
    displayMedium = DisplayMedium,
    headlineSmall = HeadlineSmall,
    titleMedium = TitleMedium,
    bodyLarge = BodyLarge,
    bodyMedium = BodyMedium,
    labelSmall = LabelSmall
)
