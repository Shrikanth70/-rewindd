package com.rewindd.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.ContentOnChampagne
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.DisplayLarge
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Screen 06 — Onboarding Complete Screen
 * Refactored into Botanical Editorial Noir luxury sanctuary style.
 */
@Composable
fun OnboardingCompleteScreen(
    onEnterRewinddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Celebratory Checkmark Icon
        Box(
            modifier = Modifier
                .size(76.dp)
                .ambientGlow(offsetY = 6.dp, radius = 24.dp)
                .background(color = SurfaceVitrine, shape = RoundedCornerShape(22.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Success",
                tint = DewSage,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "You're all set!",
            style = DisplayLarge.copy(fontSize = 30.sp),
            fontFamily = EditorialSerif,
            color = TextParchment
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Welcome to your intentional library. Save from any app with the Share button, and review your daily Rewind deck each evening.",
            style = BodyMedium.copy(fontSize = 14.sp, lineHeight = 22.sp),
            color = TextDewSage,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Recap Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                .background(color = SurfaceVitrine, shape = RoundedCornerShape(20.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "READY FOR ARCHIVING",
                        style = BadgeCaps.copy(fontSize = 10.sp),
                        color = TextChampagne
                    )
                }
                Text(
                    text = "• Share any article, video, or post directly to Rewindd\n• Organize into tagged collections\n• 100% offline local storage, zero tracking",
                    style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 22.sp),
                    color = TextDewSage
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Enter Rewindd Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                .background(color = ChampagneGold, shape = RoundedCornerShape(16.dp))
                .tactilePress()
                .clickable { onEnterRewinddClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Enter Rewindd",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = ContentOnChampagne
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = ContentOnChampagne,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
