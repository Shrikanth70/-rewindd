package com.rewindd.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

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
                    color = OutlineVariant.copy(alpha = 0.6f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
            .height(58.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Left: Clean #rewindd Wordmark in Poppins / Modern Geometric Font (matches Image 3)
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onStreakClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#rewindd",
                fontFamily = FontFamily.SansSerif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.03).em,
                color = TextParchment
            )
        }

        // Right: Settings Action Pill
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(38.dp)
                .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                .background(color = SurfaceElevated, shape = RoundedCornerShape(12.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.25f)
                .tactilePress()
                .clickable { onSettingsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = TextDewSage,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
