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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Screen 03 — Onboarding Privacy Explanation
 * Establishes the 100% local-first sanctuary contract:
 * - Zero app tracking / surveillance
 * - 100% offline local SQLite
 * - No accounts or cloud sync
 * - Full data export & wipe
 */
@Composable
fun PrivacyExplanationScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(CanvasNoir)
                .drawBehind {
                    drawLine(
                        color = OutlineVariant.copy(alpha = 0.5f),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .tactilePress()
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextParchment,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Privacy Sanctuary",
                    style = TitleMedium,
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )

                Box(
                    modifier = Modifier
                        .background(SurfaceElevated, CircleShape)
                        .frostedRimHighlight(shape = CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "STEP 2 OF 2",
                        style = BadgeCaps.copy(fontSize = 9.sp),
                        color = TextChampagne
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceTray, RoundedCornerShape(16.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = DewSage,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Local-first by design.\nNo cloud. Zero tracking.",
                    style = DisplayLarge.copy(fontSize = 24.sp, lineHeight = 30.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )

                Text(
                    text = "Your bookmarks, reading notes, and decks belong strictly to you. Rewindd runs 100% on-device with zero app surveillance and zero remote servers.",
                    style = BodyMedium.copy(fontSize = 13.5.sp, lineHeight = 19.sp),
                    color = TextDewSage
                )
            }

            // Guarantees Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    listOf(
                        "Zero App Surveillance" to "We do not monitor your device usage, background apps, or screen time. No intrusive permissions required.",
                        "100% Local SQLite Vault" to "Every link, note, and custom deck lives inside your phone's private app storage.",
                        "No Account or Cloud Sign-in" to "Open the app and use it immediately. No email, passwords, phone numbers, or analytics SDKs.",
                        "Complete Data Export & Purge" to "Export your entire vault to JSON or wipe the database clean with one tap from Settings at any time."
                    ).forEach { (title, desc) ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextParchment
                                )
                                Text(
                                    text = desc,
                                    style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 17.sp),
                                    color = TextDewSage
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Primary CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(color = ChampagneGold, shape = RoundedCornerShape(16.dp))
                    .tactilePress()
                    .clickable { onContinueClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "I Understand & Agree",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = ContentOnChampagne
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = ContentOnChampagne,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Reassurance Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = TextLichenMuted,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Your private sanctuary is ready.",
                    style = BodyMedium.copy(fontSize = 11.5.sp),
                    color = TextLichenMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
