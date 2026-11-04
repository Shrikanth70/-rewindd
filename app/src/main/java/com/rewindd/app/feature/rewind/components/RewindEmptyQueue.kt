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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.RoseSignal
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress

@Composable
fun RewindEmptyQueue(
    totalReviewedToday: Int = 0,
    onNavigateToLibrary: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Empty State Vitrine Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 6.dp, radius = 24.dp)
                .background(SurfaceVitrine, RoundedCornerShape(26.dp))
                .frostedRimHighlight(RoundedCornerShape(26.dp), topAlpha = 0.28f)
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cassette Sanctuary Illustration Frame
                Box(
                    modifier = Modifier
                        .size(width = 240.dp, height = 140.dp)
                        .padding(8.dp)
                ) {
                    // Cleared Badge
                    Box(
                        modifier = Modifier
                            .offset(x = (-4).dp, y = (-4).dp)
                            .rotate(-6f)
                            .background(RoseSignal.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                            .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.35f)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = RoseSignal,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("CLEARED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RoseSignal)
                        }
                    }

                    // Cassette Body
                    Box(
                        modifier = Modifier
                            .size(width = 200.dp, height = 118.dp)
                            .align(Alignment.Center)
                            .background(SurfaceElevated, RoundedCornerShape(14.dp))
                            .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.30f)
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Cassette Top Label
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SurfaceTray, RoundedCornerShape(6.dp))
                                    .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.20f)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "REWIND TAPE • A-SIDE",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextChampagne
                                    )
                                    Text(
                                        text = "$totalReviewedToday/$totalReviewedToday",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DewSage
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Reel center window
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .background(SurfaceTray, RoundedCornerShape(8.dp))
                                    .frostedRimHighlight(RoundedCornerShape(8.dp), topAlpha = 0.20f)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Left Spool
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceElevated)
                                            .frostedRimHighlight(CircleShape, topAlpha = 0.30f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(ChampagneGold)
                                        )
                                    }

                                    // Tape strip
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .padding(horizontal = 6.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(DewSage.copy(alpha = 0.4f))
                                    )

                                    // Right Spool
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceElevated)
                                            .frostedRimHighlight(CircleShape, topAlpha = 0.30f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(ChampagneGold)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Bottom Notch
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(DewSage))
                                Text("VAULT HARMONIZED", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = TextLichenMuted)
                                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(DewSage))
                            }
                        }
                    }

                    // Green Checkmark Circular Badge
                    Box(
                        modifier = Modifier
                            .offset(x = 6.dp, y = 6.dp)
                            .size(34.dp)
                            .align(Alignment.BottomEnd)
                            .background(DewSage, CircleShape)
                            .frostedRimHighlight(CircleShape, topAlpha = 0.35f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Cleared",
                            tint = CanvasNoir,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "You’re all caught up.",
                    style = HeadlineSmall.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = TextParchment,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (totalReviewedToday > 0) {
                        "$totalReviewedToday specimens reviewed. Your sanctuary is peaceful."
                    } else {
                        "Your inbox has no pending specimens to review. Save new links anytime."
                    },
                    fontSize = 13.5.sp,
                    color = TextDewSage,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )
            }
        }

        // Today's Rewind Sprint Bento Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceVitrine, RoundedCornerShape(22.dp))
                .frostedRimHighlight(RoundedCornerShape(22.dp), topAlpha = 0.25f)
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today’s Rewind Sprint",
                        style = HeadlineSmall.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = TextParchment
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.20f)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("BATCH #1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextChampagne)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Retention Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(SurfaceTray, RoundedCornerShape(14.dp))
                            .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.20f)
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("RETENTION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextLichenMuted)
                                Icon(Icons.Default.Bookmark, contentDescription = null, tint = if (isDarkThemeGlobal) ChampagneGold else WarmAmber, modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalReviewedToday",
                                style = HeadlineSmall.copy(fontSize = 20.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = TextChampagne
                            )
                            Text("Kept in Vault", fontSize = 10.5.sp, color = TextDewSage)
                        }
                    }

                    // Archive Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(SurfaceTray, RoundedCornerShape(14.dp))
                            .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.20f)
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("ARCHIVED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextLichenMuted)
                                Icon(Icons.Default.Delete, contentDescription = null, tint = DewSage, modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Safe",
                                style = HeadlineSmall.copy(fontSize = 18.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = DewSage
                            )
                            Text("Out of queue", fontSize = 10.5.sp, color = TextDewSage)
                        }
                    }
                }
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(ChampagneGold, RoundedCornerShape(16.dp))
                    .tactilePress()
                    .clickable(onClick = onNavigateToLibrary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Explore Library Vault 📚",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF141D16)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(SurfaceElevated, RoundedCornerShape(16.dp))
                    .frostedRimHighlight(RoundedCornerShape(16.dp), topAlpha = 0.25f)
                    .tactilePress()
                    .clickable(onClick = onNavigateToHome),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Back to Home 🏠",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextParchment
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
