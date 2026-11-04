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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
 * Screen 01 — Onboarding Welcome & Value Overview
 * High-contrast, authentic botanical luxury layout introducing the core Rewindd concept.
 */
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    onSettingsClick: () -> Unit,
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
                .statusBarsPadding()
                .drawBehind {
                    drawLine(
                        color = OutlineVariant.copy(alpha = 0.5f),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
                .height(58.dp)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, CircleShape)
                            .frostedRimHighlight(shape = CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "V1.0",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextChampagne
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                            .background(SurfaceElevated, RoundedCornerShape(10.dp))
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
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Pill
            Box(
                modifier = Modifier
                    .background(SurfaceElevated, CircleShape)
                    .frostedRimHighlight(shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(DewSage, CircleShape)
                    )
                    Text(
                        text = "LOCAL MEMORY SANCTUARY",
                        style = BadgeCaps.copy(fontSize = 10.sp),
                        color = TextChampagne
                    )
                }
            }

            // Hero Headline & Narrative
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Stop hoarding tabs.\nStart intentional recall.",
                    style = DisplayLarge.copy(fontSize = 28.sp, lineHeight = 34.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )
                Text(
                    text = "Save links, videos, and articles from any app in one tap. Revisit them mindfully when you have time, in a 100% private, on-device vault.",
                    style = BodyMedium.copy(fontSize = 14.sp, lineHeight = 21.sp),
                    color = TextDewSage
                )
            }

            // Feature Card 1: Capture Anywhere
            OnboardingHighlightCard(
                icon = Icons.Filled.Share,
                badge = "INPUT",
                title = "Save from Any App in 1 Tap",
                description = "Browse Instagram, YouTube, X, Reddit, or Chrome. Tap the Android Share sheet ➔ Rewindd to capture links with instant rich thumbnails.",
                footerSnippet = "Instagram • YouTube • Substack • Web Articles"
            )

            // Feature Card 2: Mindful Recall Deck
            OnboardingHighlightCard(
                icon = Icons.Filled.Style,
                badge = "RECALL",
                title = "Daily Rewind Card Deck",
                description = "Review your queue card-by-card in a relaxed, distraction-free sweep. Swipe Right to Keep into your permanent collections; Swipe Left to Archive.",
                footerSnippet = "Swipe Right to Keep ✓ • Swipe Left to Archive ✕"
            )

            // Feature Card 3: 100% Local-First
            OnboardingHighlightCard(
                icon = Icons.Filled.Shield,
                badge = "PRIVACY",
                title = "100% On-Device Sanctuary",
                description = "Zero app tracking, zero background surveillance, and zero ad networks. Your content is stored exclusively on your phone's encrypted SQLite database.",
                footerSnippet = "No Accounts • Fully Offline • Export Anytime"
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Primary CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(color = ChampagneGold, shape = RoundedCornerShape(16.dp))
                    .tactilePress()
                    .clickable { onGetStartedClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "See How It Works",
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
                    text = "100% local-first • Your content stays on your device.",
                    style = BodyMedium.copy(fontSize = 11.5.sp),
                    color = TextLichenMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OnboardingHighlightCard(
    icon: ImageVector,
    badge: String,
    title: String,
    description: String,
    footerSnippet: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .ambientGlow(offsetY = 4.dp, radius = 16.dp)
            .background(SurfaceVitrine, RoundedCornerShape(20.dp))
            .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = DewSage,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SurfaceElevated, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badge,
                        style = BadgeCaps.copy(fontSize = 9.sp),
                        color = TextChampagne
                    )
                }
            }

            Text(
                text = title,
                style = HeadlineSmall.copy(fontSize = 17.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Text(
                text = description,
                style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                color = TextDewSage
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = footerSnippet,
                    style = BadgeCaps.copy(fontSize = 9.sp),
                    color = TextLichenMuted
                )
            }
        }
    }
}
