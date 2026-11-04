package com.rewindd.app.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.TextParchment
import kotlinx.coroutines.delay

/**
 * Clean, fluid opening splash animation:
 * Animates only the text "#rewindd" with no captions, in clean geometric Poppins styling.
 */
@Composable
fun BotanicalSplashAnimation(
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    var isExiting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Step 1: Smooth spring reveal of "#rewindd" text
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
                .background(CanvasNoir),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#rewindd",
                fontFamily = FontFamily.SansSerif,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.03).em,
                color = TextParchment,
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value)
            )
        }
    }
}
