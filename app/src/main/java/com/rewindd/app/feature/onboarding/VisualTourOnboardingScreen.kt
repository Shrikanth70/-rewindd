package com.rewindd.app.feature.onboarding

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.Poppins
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress
import kotlinx.coroutines.launch

data class TourStepData(
    val stepNumber: String,
    val headline: String,
    val supportText: String
)

private val TOUR_STEPS = listOf(
    TourStepData(
        stepNumber = "01 / 04",
        headline = "Find Any Link & Tap Share",
        supportText = "Encounter something inspiring on YouTube, Instagram, X, Pinterest, or Threads? Tap Share in that app."
    ),
    TourStepData(
        stepNumber = "02 / 04",
        headline = "Select Rewindd in Share Sheet",
        supportText = "Choose Rewindd from Android's share targets. Your link is safely captured in a split second."
    ),
    TourStepData(
        stepNumber = "03 / 04",
        headline = "Rich Cover Art & Full Details",
        supportText = "Cover images, titles, and descriptions are stored locally in your offline vault without cloud surveillance."
    ),
    TourStepData(
        stepNumber = "04 / 04",
        headline = "Organize, Search & Rewind",
        supportText = "Search at lightspeed, group into decks, and mindfully recall what you've saved in your daily Rewind session."
    )
)

@Composable
fun VisualTourOnboardingScreen(
    onCompleteTour: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { TOUR_STEPS.size })

    // Detect system reduce motion setting
    val isReduceMotion = remember {
        try {
            val scale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
            scale == 0f
        } catch (_: Exception) {
            false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // TOP HEADER BAR: Brand Logo & Skip Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "#rewindd",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = if (isDarkThemeGlobal) TextParchment else Color(0xFF141D16)
                )
            }

            if (pagerState.currentPage < TOUR_STEPS.size - 1) {
                Box(
                    modifier = Modifier
                        .tactilePress()
                        .clickable { onCompleteTour() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Skip",
                        style = BodyMedium.copy(fontSize = 14.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = TextLichenMuted
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // PAGER WITH 4 SCENES
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            TourScenePage(
                pageIndex = page,
                step = TOUR_STEPS[page],
                isReduceMotion = isReduceMotion
            )
        }

        // BOTTOM BAR: Page Indicators + Next / Get Started Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Page Indicator Pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                for (index in TOUR_STEPS.indices) {
                    val isSelected = pagerState.currentPage == index
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 28.dp else 8.dp,
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                        label = "dotWidth"
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    if (isDarkThemeGlobal) ChampagneGold else Color(0xFF141D16)
                                } else {
                                    TextLichenMuted.copy(alpha = 0.35f)
                                }
                            )
                    )
                }
            }

            // Primary Action Button
            val isLastPage = pagerState.currentPage == TOUR_STEPS.size - 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 14.dp)
                    .background(
                        color = if (isDarkThemeGlobal) ChampagneGold else Color(0xFF141D16),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .tactilePress()
                    .clickable {
                        if (isLastPage) {
                            onCompleteTour()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isLastPage) "Get Started" else "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkThemeGlobal) Color(0xFF141D16) else Color.White
                    )
                    Icon(
                        imageVector = if (isLastPage) Icons.Default.Bolt else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isDarkThemeGlobal) Color(0xFF141D16) else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TourScenePage(
    pageIndex: Int,
    step: TourStepData,
    isReduceMotion: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step Badge
        Box(
            modifier = Modifier
                .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                .frostedRimHighlight(RoundedCornerShape(9999.dp), topAlpha = 0.25f)
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            Text(
                text = step.stepNumber,
                style = BadgeCaps.copy(fontSize = 11.sp, letterSpacing = 0.15.sp),
                color = DewSage,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Headline
        Text(
            text = step.headline,
            style = HeadlineSmall.copy(fontSize = 24.sp, lineHeight = 30.sp),
            fontWeight = FontWeight.Bold,
            color = TextParchment,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Support copy
        Text(
            text = step.supportText,
            style = BodyMedium.copy(fontSize = 14.sp, lineHeight = 20.sp),
            color = TextLichenMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Motion Graphic Interactive Scene Frame
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            when (pageIndex) {
                0 -> SceneShareInOtherApp(isReduceMotion = isReduceMotion)
                1 -> SceneSelectRewinddInShareSheet(isReduceMotion = isReduceMotion)
                2 -> SceneVaultCardWithRichCover(isReduceMotion = isReduceMotion)
                else -> SceneOrganizeAndRewind(isReduceMotion = isReduceMotion)
            }
        }
    }
}

// SCENE 1: Find post in another app & tap Share
@Composable
private fun SceneShareInOtherApp(isReduceMotion: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse1")
    val pulseScale by if (isReduceMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.82f)
            .background(SurfaceVitrine, RoundedCornerShape(26.dp))
            .border(1.5.dp, if (isDarkThemeGlobal) Color(0xFF283B2E) else Color(0xFFDCD6CA), RoundedCornerShape(26.dp))
            .padding(18.dp)
            .semantics { contentDescription = "Social post preview with Share button being tapped" },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // App Bar inside phone preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFFE50914), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = Color.White, fontSize = 10.sp)
                    }
                    Text(
                        text = "Social Video",
                        style = BadgeCaps.copy(fontSize = 11.sp),
                        color = TextParchment
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(DewSage, CircleShape)
                )
            }

            // Mock Media Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF3B1D54),
                                Color(0xFF24483B),
                                Color(0xFF16251C)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("▶", color = ChampagneGold, fontSize = 18.sp)
                }
            }

            // Post Description Lines
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(10.dp)
                        .background(SurfaceElevated, RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(8.dp)
                        .background(SurfaceElevated.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                )
            }

            // Action Buttons with Highlighted "Share"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("♥  12.4K", style = BodyMedium.copy(fontSize = 12.sp), color = TextLichenMuted)
                Text("💬  842", style = BodyMedium.copy(fontSize = 12.sp), color = TextLichenMuted)

                // Animated Share Button with Tapping Indicator
                Box(
                    modifier = Modifier
                        .background(
                            color = ChampagneGold.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, ChampagneGold, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = ChampagneGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "SHARE",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = ChampagneGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Touch Indicator Halo
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .scale(pulseScale)
                            .border(2.dp, ChampagneGold.copy(alpha = 0.65f), CircleShape)
                    )
                }
            }
        }
    }
}

// SCENE 2: Android Share Sheet sliding up with Rewindd chosen
@Composable
private fun SceneSelectRewinddInShareSheet(isReduceMotion: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "sheetSlide")
    val sheetOffsetY by if (isReduceMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 24f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offsetY"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.82f)
            .background(SurfaceVitrine, RoundedCornerShape(26.dp))
            .border(1.5.dp, if (isDarkThemeGlobal) Color(0xFF283B2E) else Color(0xFFDCD6CA), RoundedCornerShape(26.dp))
            .clip(RoundedCornerShape(26.dp))
            .semantics { contentDescription = "Android share sheet with Rewindd selected" },
        contentAlignment = Alignment.BottomCenter
    ) {
        // Dimmed background screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        // Sliding Share Sheet Tray
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = sheetOffsetY.dp)
                .background(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isDarkThemeGlobal) Color(0xFF2E4234) else Color(0xFFD4CCBC),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Sheet Grabber Bar
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(TextLichenMuted.copy(alpha = 0.5f), RoundedCornerShape(9999.dp))
                )

                Text(
                    text = "Share via",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = TextLichenMuted
                )

                // Share Targets Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ShareTargetMock(name = "Messages", icon = "💬", isSelected = false)
                    // Rewindd Chosen Target with Highlight
                    ShareTargetMock(name = "Rewindd", icon = "⚡", isSelected = true)
                    ShareTargetMock(name = "Copy Link", icon = "🔗", isSelected = false)
                    ShareTargetMock(name = "Drive", icon = "📁", isSelected = false)
                }
            }
        }
    }
}

@Composable
private fun ShareTargetMock(name: String, icon: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isSelected) ChampagneGold else SurfaceTray
                )
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) DewSage else TextLichenMuted.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 22.sp
            )
        }
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) ChampagneGold else TextLichenMuted
        )
    }
}

// SCENE 3: Rewindd card with rich cover art, title, and description
@Composable
private fun SceneVaultCardWithRichCover(isReduceMotion: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "cardFloat")
    val cardFloatY by if (isReduceMotion) {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    } else {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -6f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "cardFloat"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.82f)
            .background(SurfaceVitrine, RoundedCornerShape(26.dp))
            .border(1.5.dp, if (isDarkThemeGlobal) Color(0xFF283B2E) else Color(0xFFDCD6CA), RoundedCornerShape(26.dp))
            .padding(16.dp)
            .semantics { contentDescription = "Vault card showing rich title, description, cover image, and offline badge" },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = cardFloatY.dp)
                .background(SurfaceTray, RoundedCornerShape(20.dp))
                .border(1.5.dp, DewSage.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column {
                // Cover Art Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF2F4F4F),
                                    Color(0xFF1E3A2F),
                                    Color(0xFF0D140E)
                                )
                            )
                        ),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("YOUTUBE.COM", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DewSage)
                    }
                }

                // Metadata Details
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Why Offline First Is The Future of Apps",
                        style = BodyMedium.copy(fontSize = 14.sp, lineHeight = 18.sp),
                        fontWeight = FontWeight.Bold,
                        color = TextParchment,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "An in-depth look at local SQLite architecture and zero cloud leakage.",
                        style = BodyMedium.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = TextLichenMuted,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(DewSage.copy(alpha = 0.15f), RoundedCornerShape(9999.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("● ENCRYPTED & VAULTED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DewSage)
                        }

                        Text("SAVED JUST NOW", fontSize = 9.sp, color = TextLichenMuted)
                    }
                }
            }
        }
    }
}

// SCENE 4: Organize, Search & Daily Rewind
@Composable
private fun SceneOrganizeAndRewind(isReduceMotion: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.82f)
            .background(SurfaceVitrine, RoundedCornerShape(26.dp))
            .border(1.5.dp, if (isDarkThemeGlobal) Color(0xFF283B2E) else Color(0xFFDCD6CA), RoundedCornerShape(26.dp))
            .padding(16.dp)
            .semantics { contentDescription = "Search bar, collection tags, and rewind queue deck" },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mock Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(SurfaceElevated, RoundedCornerShape(12.dp))
                    .border(1.dp, ChampagneGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ChampagneGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Search offline vault...", fontSize = 12.sp, color = TextLichenMuted)
                }
            }

            // Collection Tag Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagChipMock(label = "📁 Inspiration", isSelected = true)
                TagChipMock(label = "⚡ Read Later", isSelected = false)
                TagChipMock(label = "🎥 Watchlist", isSelected = false)
            }

            // Fanned 3D Deck Mockup
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp),
                contentAlignment = Alignment.Center
            ) {
                // Back card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .height(90.dp)
                        .offset(y = (-10).dp)
                        .background(SurfaceElevated.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .border(1.dp, TextLichenMuted.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                )
                // Mid card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(95.dp)
                        .offset(y = (-5).dp)
                        .background(SurfaceElevated, RoundedCornerShape(14.dp))
                        .border(1.dp, TextLichenMuted.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                )
                // Front card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(100.dp)
                        .background(SurfaceTray, RoundedCornerShape(16.dp))
                        .border(1.5.dp, ChampagneGold.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("DAILY REWIND", style = BadgeCaps.copy(fontSize = 9.sp), color = ChampagneGold)
                            Text("1 of 5", fontSize = 10.sp, color = TextLichenMuted)
                        }
                        Text(
                            text = "Swipe to recall, archive, or keep",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("← Archive", fontSize = 10.sp, color = TextLichenMuted)
                            Text("Keep →", fontSize = 10.sp, color = DewSage)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TagChipMock(label: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) DewSage.copy(alpha = 0.2f) else SurfaceElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) DewSage else TextLichenMuted.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) DewSage else TextLichenMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
