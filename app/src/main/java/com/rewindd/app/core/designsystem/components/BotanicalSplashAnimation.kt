package com.rewindd.app.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rewindd.app.R
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import kotlinx.coroutines.delay

private val LightSplashCanvasColor = Color(0xFFF9F7F3)     // Off-white ivory canvas
private val DarkSplashCanvasColor = Color(0xFF0F1511)      // Deep nocturnal olive-noir canvas

/**
 * Custom opening splash animation:
 * Centered "#rewindd" transparent wordmark PNG (theme-aware without background)
 * with fluid spring reveal and graceful dissolve exit.
 */
@Composable
fun BotanicalSplashAnimation(
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    var isExiting by remember { mutableStateOf(false) }

    val isDark = isDarkThemeGlobal || isSystemInDarkTheme()
    val splashBackground = if (isDark) DarkSplashCanvasColor else LightSplashCanvasColor
    val wordmarkRes = if (isDark) R.drawable.rewindd_wordmark_dark else R.drawable.rewindd_wordmark_light

    LaunchedEffect(Unit) {
        // Step 1: Smooth spring reveal of the "#rewindd" wordmark
        alpha.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        // Step 2: Hold poise (total ~1000ms)
        delay(700)

        // Step 3: Graceful exit dissolve
        isExiting = true
        delay(250)
        onAnimationComplete()
    }

    AnimatedVisibility(
        visible = !isExiting,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(250, easing = FastOutSlowInEasing))
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(splashBackground),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = wordmarkRes),
                contentDescription = "#rewindd",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(260.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
            )
        }
    }
}
