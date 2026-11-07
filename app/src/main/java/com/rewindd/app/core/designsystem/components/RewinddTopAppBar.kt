package com.rewindd.app.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rewindd.app.R
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress

private const val WORDMARK_ASPECT_RATIO = 817f / 142f

@Composable
internal fun RewinddWordmark(
    modifier: Modifier = Modifier,
    height: Dp = 18.dp,
    contentDescription: String? = "#rewindd"
) {
    val wordmarkRes = if (isDarkThemeGlobal) {
        R.drawable.rewindd_wordmark_dark
    } else {
        R.drawable.rewindd_wordmark_light
    }
    Image(
        painter = painterResource(id = wordmarkRes),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .height(height)
            .width(height * WORDMARK_ASPECT_RATIO)
    )
}

@Composable
fun RewinddTopAppBar(
    modifier: Modifier = Modifier,
    onStreakClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CanvasNoir)
            .statusBarsPadding()
            .drawBehind {
                // Subtle bottom hairline separator
                drawLine(
                    color = OutlineVariant.copy(alpha = 0.45f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
            .height(56.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Left: Clean, gracefully proportioned #rewindd wordmark
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onStreakClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RewinddWordmark(
                height = 18.dp
            )
        }

        // Right: Settings Action Pill
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(38.dp)
                .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                .background(color = SurfaceElevated, shape = RoundedCornerShape(12.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.28f)
                .tactilePress()
                .clickable { onSettingsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = TextDewSage,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}
