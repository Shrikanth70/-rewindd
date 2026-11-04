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
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
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
 * Screen 02 — Onboarding How Rewindd Works
 * Practical 3-step educational walkthrough conveying the core usage loop:
 * 1. Capture via Share Sheet / Paste
 * 2. Daily Rewind swipe deck
 * 3. Organize in decks & search offline
 */
@Composable
fun HowRewinddWorksScreen(
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
                    text = "How It Works",
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
                        text = "STEP 1 OF 2",
                        style = BadgeCaps.copy(fontSize = 9.sp),
                        color = TextChampagne
                    )
                }
            }
        }

        // Scrollable Walkthrough
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Intro
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .background(SurfaceElevated, CircleShape)
                        .frostedRimHighlight(shape = CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(DewSage, CircleShape)
                        )
                        Text(
                            text = "THREE STEPS TO CLARITY",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextChampagne
                        )
                    }
                }

                Text(
                    text = "How to use Rewindd.",
                    style = DisplayLarge.copy(fontSize = 26.sp, lineHeight = 32.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )

                Text(
                    text = "A tranquil workflow designed to replace infinite open tabs with intentional review and lasting memory.",
                    style = BodyMedium.copy(fontSize = 13.5.sp, lineHeight = 19.sp),
                    color = TextDewSage
                )
            }

            // Step 1: Capture Anywhere in 1 Tap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "01",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DewSage
                                )
                            }
                            Text(
                                text = "CAPTURE",
                                style = BadgeCaps.copy(fontSize = 10.sp),
                                color = TextChampagne
                            )
                        }

                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Save from Any App in 1 Tap",
                            style = HeadlineSmall.copy(fontSize = 18.sp),
                            fontFamily = EditorialSerif,
                            color = TextParchment
                        )
                        Text(
                            text = "Whenever you stumble upon an article, video, or thread in Instagram, YouTube, X, Reddit, or Chrome: tap Share ➔ choose Rewindd.",
                            style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                            color = TextDewSage
                        )
                    }

                    // Interactive Graphic: Visual Workflow
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Any App",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextLichenMuted
                                )
                                Text(
                                    text = "Tap Share",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.5.sp,
                                    color = TextParchment
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(14.dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Android Sheet",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextLichenMuted
                                )
                                Text(
                                    text = "Select Rewindd",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.5.sp,
                                    color = TextParchment
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(14.dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Vault",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextChampagne
                                )
                                Text(
                                    text = "Preserved ✓",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.5.sp,
                                    color = ChampagneGold
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentPaste,
                            contentDescription = null,
                            tint = TextLichenMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Tip: You can also paste links directly on the Home screen.",
                            style = BodyMedium.copy(fontSize = 11.5.sp),
                            color = TextLichenMuted
                        )
                    }
                }
            }

            // Step 2: Daily Rewind Card Deck
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "02",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ChampagneGold
                                )
                            }
                            Text(
                                text = "RECALL & SWIPE",
                                style = BadgeCaps.copy(fontSize = 10.sp),
                                color = TextChampagne
                            )
                        }

                        Icon(
                            imageVector = Icons.Filled.Style,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Rewind Daily in Tactile Cards",
                            style = HeadlineSmall.copy(fontSize = 18.sp),
                            fontFamily = EditorialSerif,
                            color = TextParchment
                        )
                        Text(
                            text = "Unread bookmarks don't rot in your tabs. Open Rewind to review your queue one card at a time with clear, mindful decisions.",
                            style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                            color = TextDewSage
                        )
                    }

                    // Interactive Graphic: Swipe Left / Right Visual
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Swipe Left
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(SurfaceElevated, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Archive,
                                        contentDescription = null,
                                        tint = TextLichenMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "SWIPE LEFT",
                                        style = BadgeCaps.copy(fontSize = 8.sp),
                                        color = TextLichenMuted
                                    )
                                    Text(
                                        text = "Archive",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = TextParchment
                                    )
                                }
                            }

                            // Center Divider Card Icon
                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "1 CARD / DAY",
                                    style = BadgeCaps.copy(fontSize = 8.5.sp),
                                    color = TextChampagne
                                )
                            }

                            // Swipe Right
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "SWIPE RIGHT",
                                        style = BadgeCaps.copy(fontSize = 8.sp),
                                        color = DewSage
                                    )
                                    Text(
                                        text = "Keep in Vault",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = TextParchment
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(SurfaceElevated, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = DewSage,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Curate, Tag & Search Offline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "03",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DewSage
                                )
                            }
                            Text(
                                text = "ORGANIZE & SEARCH",
                                style = BadgeCaps.copy(fontSize = 10.sp),
                                color = TextChampagne
                            )
                        }

                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Curate Decks & Search Offline",
                            style = HeadlineSmall.copy(fontSize = 18.sp),
                            fontFamily = EditorialSerif,
                            color = TextParchment
                        )
                        Text(
                            text = "Group items into custom decks, attach notes, and filter by tags. Instant full-text search finds whatever you need, 100% offline.",
                            style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                            color = TextDewSage
                        )
                    }

                    // Interactive Graphic: Library & Tags Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "📁 Curated Decks",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = TextParchment
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "# Custom Tags",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = TextParchment
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = DewSage,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Full-text search across titles, URLs, and notes",
                                    style = BodyMedium.copy(fontSize = 11.sp),
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
                        text = "Privacy Guarantees",
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
