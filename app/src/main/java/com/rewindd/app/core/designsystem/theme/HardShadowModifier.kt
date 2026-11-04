package com.rewindd.app.core.designsystem.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Atmospheric soft ambient lighting that replaces harsh neo-brutalist drop shadows.
 * Adapts automatically to Bright or Dark sanctuary theme.
 */
fun Modifier.ambientGlow(
    color: Color? = null,
    offsetY: Dp = 6.dp,
    radius: Dp = 24.dp
): Modifier = composed {
    val isDark = isDarkThemeGlobal
    val defaultColor = if (isDark) Color(0xFF000000).copy(alpha = 0.28f) else Color(0xFF000000).copy(alpha = 0.06f)
    val effectiveColor = color ?: defaultColor
    this.drawBehind {
        val radiusPx = radius.toPx()
        val offsetPx = offsetY.toPx()
        drawRoundRect(
            color = effectiveColor,
            topLeft = Offset(0f, offsetPx),
            size = Size(size.width, size.height),
            cornerRadius = CornerRadius(radiusPx, radiusPx)
        )
    }
}

/**
 * Ethereal frosted top-lit rim highlight that replaces harsh 2px black borders.
 * Adapts to delicate subtle border in bright mode, and frosted champagne glow in dark mode.
 */
fun Modifier.frostedRimHighlight(
    shape: Shape = RoundedCornerShape(26.dp),
    topAlpha: Float = 0.22f,
    bottomAlpha: Float = 0.05f
): Modifier = composed {
    val isDark = isDarkThemeGlobal
    val topColor = if (isDark) {
        DarkChampagneGold.copy(alpha = topAlpha)
    } else {
        Color(0xFF000000).copy(alpha = topAlpha * 0.35f)
    }
    val bottomColor = if (isDark) {
        DarkDewSage.copy(alpha = bottomAlpha)
    } else {
        Color(0xFF000000).copy(alpha = bottomAlpha * 0.20f)
    }
    this.border(
        width = 1.dp,
        brush = Brush.verticalGradient(
            listOf(topColor, bottomColor)
        ),
        shape = shape
    )
}

/**
 * Backward compatibility alias: transforms legacy hardShadow calls into soft atmospheric glow.
 */
fun Modifier.hardShadow(
    offset: Dp = 4.dp,
    color: Color? = null,
    radius: Dp = 16.dp
): Modifier = composed {
    val isDark = isDarkThemeGlobal
    val defColor = if (isDark) Color(0xFF000000).copy(alpha = 0.25f) else Color(0xFF000000).copy(alpha = 0.06f)
    this.ambientGlow(
        color = color ?: defColor,
        offsetY = (offset.value * 0.75f).dp,
        radius = (radius.value * 1.2f).dp
    )
}

/**
 * Tactile micro-interaction press: smooth scale & subtle alpha dampening.
 */
fun Modifier.tactilePress(
    interactionSource: MutableInteractionSource? = null,
    targetScale: Float = 0.97f
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) targetScale else 1f,
        label = "tactilePressScale"
    )

    this.graphicsLayer {
        scaleX = animatedScale
        scaleY = animatedScale
    }
}

/**
 * Backward compatibility alias: transforms legacy brutalPress calls into smooth tactilePress.
 */
fun Modifier.brutalPress(
    interactionSource: MutableInteractionSource? = null,
    pressOffset: Dp = 2.dp
): Modifier = this.tactilePress(interactionSource = interactionSource)
