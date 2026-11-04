package com.rewindd.app.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Botanical3DAppLogo:
 * An interactive, kinetic 3D Botanical Sanctuary Medallion for Rewindd.
 *
 * Visual Craft & Physics:
 * - 3D Perspective Projection via graphicsLayer with cameraDistance.
 * - Breathing idle precession: subtle continuous organic rotation in X & Y axes.
 * - Interactive Tilt & Drag: responds dynamically to touch dragging with inertia.
 * - Restorative Spring Physics: returns smoothly to resting float position on release.
 * - Layered Multi-Plane Parallax:
 *   1. Atmospheric Emerald/Amber backlight glow halo.
 *   2. Deep nocturnal obsidian glass slab with beveled edges.
 *   3. Frosted crystalline rim with specular sheen reflection.
 *   4. Floating sculpted Gilded Botanical Flame emblem suspended in 3D space.
 */
@Composable
fun Botanical3DAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showWordmark: Boolean = true,
    interactive: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()

    // Interactive Drag Offsets
    val dragRotationX = remember { Animatable(0f) }
    val dragRotationY = remember { Animatable(0f) }
    val dragElevation = remember { Animatable(1f) }
    var isDragging by remember { mutableStateOf(false) }

    // Idle Breathing Orbital Animation
    val infiniteTransition = rememberInfiniteTransition(label = "medallionOrbit")
    val idlePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2831855f, // 2 * PI
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitPhase"
    )

    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "specularShimmer"
    )

    // Calculate dynamic rotation angles
    val idleRotX = if (!isDragging) sin(idlePhase) * 6.5f else 0f
    val idleRotY = if (!isDragging) cos(idlePhase) * 8.5f else 0f
    val currentRotX = (dragRotationX.value + idleRotX).coerceIn(-28f, 28f)
    val currentRotY = (dragRotationY.value + idleRotY).coerceIn(-30f, 30f)
    val currentScale = dragElevation.value

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 3D Medallion Box
        Box(
            modifier = Modifier
                .size(size)
                .ambientGlow(
                    color = DewSage.copy(alpha = 0.22f),
                    offsetY = 8.dp,
                    radius = (size.value * 0.28f).dp
                )
                .graphicsLayer {
                    rotationX = currentRotX
                    rotationY = currentRotY
                    scaleX = currentScale
                    scaleY = currentScale
                    cameraDistance = 18f * density
                    shadowElevation = 16f
                }
                .then(
                    if (interactive) {
                        Modifier.pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    isDragging = true
                                    coroutineScope.launch {
                                        dragElevation.animateTo(1.08f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                                    }
                                },
                                onDragEnd = {
                                    isDragging = false
                                    coroutineScope.launch {
                                        launch { dragRotationX.animateTo(0f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) }
                                        launch { dragRotationY.animateTo(0f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)) }
                                        launch { dragElevation.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
                                    }
                                },
                                onDragCancel = {
                                    isDragging = false
                                    coroutineScope.launch {
                                        launch { dragRotationX.animateTo(0f) }
                                        launch { dragRotationY.animateTo(0f) }
                                        launch { dragElevation.animateTo(1f) }
                                    }
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    coroutineScope.launch {
                                        dragRotationX.snapTo((dragRotationX.value - dragAmount.y * 0.22f).coerceIn(-24f, 24f))
                                        dragRotationY.snapTo((dragRotationY.value + dragAmount.x * 0.22f).coerceIn(-26f, 26f))
                                    }
                                }
                            )
                        }
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(size * 0.28f))
            ) {
                drawBotanicalMedallion(
                    shimmerProgress = shimmerProgress,
                    tiltX = currentRotX,
                    tiltY = currentRotY
                )
            }
        }

        // Optional Atmospheric Wordmark
        if (showWordmark) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "REWINDD",
                    style = HeadlineSmall.copy(
                        fontSize = 22.sp,
                        letterSpacing = 2.sp
                    ),
                    fontFamily = EditorialSerif,
                    fontWeight = FontWeight.Bold,
                    color = TextParchment
                )

                Text(
                    text = "INTENTIONAL ATTENTION SANCTUARY",
                    style = BadgeCaps.copy(
                        fontSize = 9.sp,
                        letterSpacing = 1.2.sp
                    ),
                    color = TextChampagne
                )
            }
        }
    }
}

/**
 * Custom DrawScope rendering of the Botanical Sanctuary Medallion:
 * - Multi-stop rich obsidian/forest vitrine base
 * - Frosted beveled rim with angle-dependent chromatic refraction
 * - Sculpted Gilded Flame & Droplet crest with 3D parallax offset
 * - Moving specular light sweep
 */
private fun DrawScope.drawBotanicalMedallion(
    shimmerProgress: Float,
    tiltX: Float,
    tiltY: Float
) {
    val w = size.width
    val h = size.height
    val cornerRadius = w * 0.28f

    // 1. Base Obsidian Vitrine Body
    val baseBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF1E2C22), // Deep nocturnal forest olive
            Color(0xFF131C16), // Dark obsidian matrix
            Color(0xFF0C120E)  // Rich nocturnal charcoal base
        ),
        center = Offset(w * (0.4f + tiltY * 0.005f), h * (0.35f + tiltX * 0.005f)),
        radius = w * 0.9f
    )

    drawRoundRect(
        brush = baseBrush,
        size = Size(w, h),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
    )

    // 2. Beveled Frosted Rim Highlight
    val rimBrush = Brush.linearGradient(
        colors = listOf(
            ChampagneGold.copy(alpha = (0.50f + tiltX * 0.01f).coerceIn(0.2f, 0.85f)),
            DewSage.copy(alpha = 0.25f),
            Color(0xFF1E2C22).copy(alpha = 0.1f),
            ChampagneGold.copy(alpha = (0.35f - tiltX * 0.01f).coerceIn(0.1f, 0.6f))
        ),
        start = Offset(0f, 0f),
        end = Offset(w, h)
    )

    drawRoundRect(
        brush = rimBrush,
        size = Size(w, h),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        style = Stroke(width = w * 0.022f)
    )

    // 3. Specular Light Shimmer Sweep
    val shimmerStartX = w * (shimmerProgress - 0.3f)
    val shimmerEndX = w * (shimmerProgress + 0.3f)
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            ChampagneGold.copy(alpha = 0.14f),
            Color.White.copy(alpha = 0.22f),
            ChampagneGold.copy(alpha = 0.10f),
            Color.Transparent
        ),
        start = Offset(shimmerStartX, 0f),
        end = Offset(shimmerEndX, h)
    )

    drawRoundRect(
        brush = shimmerBrush,
        size = Size(w, h),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        blendMode = BlendMode.Screen
    )

    // 4. Parallax Offset for Floating Gilded Emblem (Higher layer = moves more with tilt)
    val emblemOffsetX = tiltY * 0.35f
    val emblemOffsetY = -tiltX * 0.35f

    // 5. Ambient Drop Shadow behind Floating Crest
    drawFlameCrest(
        centerX = w * 0.5f + emblemOffsetX * 0.4f,
        centerY = h * 0.5f + emblemOffsetY * 0.4f + w * 0.03f,
        scale = w / 108f,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.55f),
                Color.Black.copy(alpha = 0.25f)
            )
        )
    )

    // 6. Sculpted Gilded Flame Crest (Foreground)
    val gildedBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF2DC), // Specular highlight tip
            ChampagneGold,     // Luminous champagne body
            WarmAmber,         // Warm radiant amber midtone
            Color(0xFF8E6B3A)  // Antique gilded shadow
        ),
        startY = h * 0.25f + emblemOffsetY,
        endY = h * 0.75f + emblemOffsetY
    )

    drawFlameCrest(
        centerX = w * 0.5f + emblemOffsetX,
        centerY = h * 0.5f + emblemOffsetY,
        scale = w / 108f,
        brush = gildedBrush
    )

    // 7. Inner Delicate Crystalline Core Droplet
    val dropletBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            ChampagneGold,
            DewSage
        ),
        startY = h * 0.52f + emblemOffsetY,
        endY = h * 0.72f + emblemOffsetY
    )

    drawFlameDroplet(
        centerX = w * 0.5f + emblemOffsetX * 1.15f,
        centerY = h * 0.5f + emblemOffsetY * 1.15f,
        scale = w / 108f,
        brush = dropletBrush
    )
}

/**
 * Draws the botanical/flame outer silhouette with smooth bezier curves.
 */
private fun DrawScope.drawFlameCrest(
    centerX: Float,
    centerY: Float,
    scale: Float,
    brush: Brush
) {
    val path = Path()
    // Original coordinate center is around (54, 54) on 108x108 grid
    fun x(v: Float) = centerX + (v - 54f) * scale
    fun y(v: Float) = centerY + (v - 54f) * scale

    path.moveTo(x(54.0f), y(26.0f))
    // Right wing outer curve
    path.cubicTo(
        x(58.0f), y(32.0f),
        x(66.5f), y(40.0f),
        x(69.5f), y(50.0f)
    )
    path.cubicTo(
        x(73.5f), y(60.0f),
        x(69.0f), y(73.0f),
        x(57.0f), y(79.0f)
    )
    path.cubicTo(
        x(51.0f), y(82.0f),
        x(44.0f), y(80.0f),
        x(39.0f), y(75.0f)
    )
    // Left wing sweep
    path.cubicTo(
        x(32.0f), y(67.0f),
        x(33.0f), y(53.0f),
        x(41.0f), y(43.0f)
    )
    path.cubicTo(
        x(47.0f), y(35.0f),
        x(52.5f), y(30.0f),
        x(54.0f), y(26.0f)
    )
    path.close()

    drawPath(path = path, brush = brush, style = Fill)
}

/**
 * Draws the inner droplet/seed of focused mindful reflection.
 */
private fun DrawScope.drawFlameDroplet(
    centerX: Float,
    centerY: Float,
    scale: Float,
    brush: Brush
) {
    val path = Path()
    fun x(v: Float) = centerX + (v - 54f) * scale
    fun y(v: Float) = centerY + (v - 54f) * scale

    path.moveTo(x(54.0f), y(52.0f))
    path.cubicTo(
        x(58.5f), y(58.0f),
        x(60.5f), y(64.0f),
        x(59.0f), y(70.0f)
    )
    path.cubicTo(
        x(57.5f), y(75.0f),
        x(50.5f), y(75.0f),
        x(49.0f), y(70.0f)
    )
    path.cubicTo(
        x(47.5f), y(64.0f),
        x(49.5f), y(58.0f),
        x(54.0f), y(52.0f)
    )
    path.close()

    drawPath(path = path, brush = brush, style = Fill)
}

