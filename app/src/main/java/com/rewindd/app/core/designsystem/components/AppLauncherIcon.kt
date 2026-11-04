package com.rewindd.app.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.util.AppIconLoader

@Composable
fun AppLauncherIcon(
    packageName: String,
    appName: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    val context = LocalContext.current
    var iconBitmap by remember(packageName) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(packageName) {
        iconBitmap = AppIconLoader.loadIcon(context, packageName)
    }

    Box(
        modifier = modifier
            .size(size)
            .background(SurfaceTray, RoundedCornerShape(10.dp))
            .frostedRimHighlight(shape = RoundedCornerShape(10.dp), topAlpha = 0.2f, bottomAlpha = 0.05f)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        val bitmap = iconBitmap
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = appName,
                modifier = Modifier.size(size)
            )
        } else {
            Text(
                text = AppIconLoader.getFallbackLetter(appName),
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.45).sp,
                color = TextParchment
            )
        }
    }
}
