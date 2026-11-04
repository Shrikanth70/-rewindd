package com.rewindd.app.feature.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Screen 40 — Pause Tracking Modal Bottom Sheet
 * Refactored into Botanical Editorial Noir luxury sanctuary style.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PauseTrackingBottomSheet(
    onConfirmPause: (durationHours: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedOption by remember { mutableIntStateOf(1) } // 1: 1h, 2: Tomorrow, 3: Indefinitely

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CanvasNoir,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(WarmAmber, CircleShape)
                        )
                        Text(
                            text = "Pause Tracking",
                            style = HeadlineSmall.copy(fontSize = 22.sp),
                            fontFamily = EditorialSerif,
                            color = TextParchment
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "TEMPORARY STANDBY",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextChampagne
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .tactilePress()
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextParchment,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Warm Amber Accent Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SurfaceElevated, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = WarmAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            "Need focused time or privacy?",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = TextParchment
                        )
                        Text(
                            "Pausing suspends foreground dwell time calculations for all monitored apps. No screen-time stats will be recorded.",
                            style = BodyMedium.copy(fontSize = 11.sp, lineHeight = 16.sp),
                            color = TextDewSage
                        )
                    }
                }
            }

            // Reassurance Bento Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceTray, RoundedCornerShape(14.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(14.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "WHAT STAYS ACTIVE WHILE PAUSED",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextLichenMuted
                        )
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "RUNNING",
                                style = BadgeCaps.copy(fontSize = 8.sp),
                                color = DewSage
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "Saving links from Share Sheet & Clipboard",
                            fontSize = 12.sp,
                            color = TextDewSage
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "Daily Rewind Review Flashcards",
                            fontSize = 12.sp,
                            color = TextDewSage
                        )
                    }
                }
            }

            // Duration Options
            Text(
                "SELECT DURATION",
                style = BadgeCaps.copy(fontSize = 10.sp),
                color = TextLichenMuted
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Option 1: 1 Hour
                val isOpt1 = selectedOption == 1
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 8.dp)
                        .background(
                            if (isOpt1) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(14.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(14.dp),
                            topAlpha = if (isOpt1) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedOption = 1 }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(SurfaceTray, CircleShape)
                                    .frostedRimHighlight(shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isOpt1) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(ChampagneGold, CircleShape)
                                    )
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    "Pause for 1 hour",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = if (isOpt1) TextChampagne else TextParchment
                                )
                                Text(
                                    "Resumes automatically in 60m",
                                    fontSize = 11.sp,
                                    color = TextDewSage
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "RECOMMENDED",
                                style = BadgeCaps.copy(fontSize = 8.sp),
                                color = ChampagneGold
                            )
                        }
                    }
                }

                // Option 2: Until Tomorrow
                val isOpt2 = selectedOption == 2
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 8.dp)
                        .background(
                            if (isOpt2) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(14.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(14.dp),
                            topAlpha = if (isOpt2) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedOption = 2 }
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(SurfaceTray, CircleShape)
                                .frostedRimHighlight(shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isOpt2) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(ChampagneGold, CircleShape)
                                    )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Pause until tomorrow morning",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isOpt2) TextChampagne else TextParchment
                            )
                            Text(
                                "Resumes at 06:00 AM tomorrow",
                                fontSize = 11.sp,
                                color = TextDewSage
                            )
                        }
                    }
                }

                // Option 3: Indefinitely
                val isOpt3 = selectedOption == 3
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 8.dp)
                        .background(
                            if (isOpt3) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(14.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(14.dp),
                            topAlpha = if (isOpt3) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedOption = 3 }
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(SurfaceTray, CircleShape)
                                .frostedRimHighlight(shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isOpt3) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(ChampagneGold, CircleShape)
                                    )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Pause indefinitely",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isOpt3) TextChampagne else TextParchment
                            )
                            Text(
                                "Until manually resumed in Settings",
                                fontSize = 11.sp,
                                color = TextDewSage
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Confirm Button
            Button(
                onClick = {
                    val duration = when (selectedOption) {
                        1 -> 1
                        2 -> 12
                        else -> 999
                    }
                    onConfirmPause(duration)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChampagneGold,
                    contentColor = CanvasNoir
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .tactilePress()
            ) {
                Text(
                    "Confirm Pause ⏸",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CanvasNoir
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
