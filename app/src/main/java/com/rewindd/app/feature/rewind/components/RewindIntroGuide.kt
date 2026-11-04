package com.rewindd.app.feature.rewind.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.ContentOnChampagne
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

@Composable
fun RewindIntroGuide(
    onStartDeck: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .ambientGlow(offsetY = 4.dp, radius = 16.dp)
            .background(SurfaceVitrine, RoundedCornerShape(20.dp))
            .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            // Pill header
            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(6.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "1-MIN GUIDE",
                        style = BadgeCaps.copy(fontSize = 9.5.sp),
                        color = TextChampagne
                    )
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = TextChampagne,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Rewind with Intention",
                style = HeadlineSmall.copy(fontSize = 22.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Review your saved links one by one. Keep what matters, archive what you're done with.",
                style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 19.sp),
                color = TextDewSage
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gesture rules
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Rule 1: Keep
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "KEEP →",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = DewSage
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Swipe right or tap Keep to mark reviewed.",
                        style = BodyMedium.copy(fontSize = 12.sp),
                        color = TextParchment
                    )
                }

                // Rule 2: Archive
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "← ARCHIVE",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = TextChampagne
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Swipe left to move out of daily inbox.",
                        style = BodyMedium.copy(fontSize = 12.sp),
                        color = TextParchment
                    )
                }

                // Rule 3: Open
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "OPEN ↗",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = TextChampagne
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Tap card or button to read in original app.",
                        style = BodyMedium.copy(fontSize = 12.sp),
                        color = TextParchment
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(ChampagneGold, RoundedCornerShape(14.dp))
                    .tactilePress()
                    .clickable { onStartDeck() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start Rewind Deck",
                    style = BadgeCaps.copy(fontSize = 13.sp),
                    color = ContentOnChampagne
                )
            }
        }
    }
}
