package com.rewindd.app.feature.rewind.components

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.LabelSmall
import com.rewindd.app.core.designsystem.theme.RoseSignal
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
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.feature.library.components.media.MediaPresentationPolicy
import com.rewindd.app.feature.library.components.media.SmartMediaPreview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RewindCardDeck(
    item: SavedItem,
    currentIndex: Int,
    totalCount: Int,
    onKeep: () -> Unit,
    onArchive: () -> Unit,
    onOpenOriginal: () -> Unit,
    onPrev: () -> Unit = {},
    onNext: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val formattedDate = remember(item.capturedAtUtc) { dateFormat.format(Date(item.capturedAtUtc)) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Progress Pill Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(999.dp))
                    .frostedRimHighlight(RoundedCornerShape(999.dp), topAlpha = 0.25f)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(DewSage, CircleShape)
                    )
                    Text(
                        text = "$currentIndex of $totalCount waiting",
                        style = TitleMedium.copy(fontSize = 13.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = TextChampagne
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                    .frostedRimHighlight(RoundedCornerShape(8.dp), topAlpha = 0.20f)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${((currentIndex.toFloat() / totalCount.coerceAtLeast(1)) * 100).toInt()}%",
                    style = BadgeCaps,
                    color = DewSage
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { (currentIndex.toFloat() / totalCount.coerceAtLeast(1)).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = ChampagneGold,
            trackColor = SurfaceTray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Vitrine Stack with Subtle Depth Offset (Anti-Neo-Brutalism Depth)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Stack Layer 2 (Deep Background)
            Box(
                modifier = Modifier
                    .fillMaxSize(0.92f)
                    .offset(y = 12.dp)
                    .background(color = SurfaceTray, shape = RoundedCornerShape(26.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(26.dp), topAlpha = 0.12f)
            )

            // Stack Layer 1 (Midground)
            Box(
                modifier = Modifier
                    .fillMaxSize(0.96f)
                    .offset(y = 6.dp)
                    .background(color = SurfaceVitrine.copy(alpha = 0.85f), shape = RoundedCornerShape(26.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(26.dp), topAlpha = 0.20f)
            )

            // Signature Main Card (Top Vitrine Specimen)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .ambientGlow(offsetY = 8.dp, radius = 28.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(26.dp))
                    .frostedRimHighlight(RoundedCornerShape(26.dp), topAlpha = 0.32f)
                    .clip(RoundedCornerShape(26.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Card Top Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(CypressGreen, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = (item.sourceLabel ?: "WEB").uppercase(),
                                style = BadgeCaps,
                                color = DewSage
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = formattedDate,
                                style = LabelSmall,
                                color = TextLichenMuted
                            )
                            if (item.isPinned) {
                                Box(
                                    modifier = Modifier
                                        .background(RoseSignal.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PINNED", style = BadgeCaps.copy(fontSize = 9.sp), color = RoseSignal)
                                }
                            }
                        }
                    }

                    val mediaSpec = remember(item.url, item.thumbnailUri) {
                        MediaPresentationPolicy.resolve(item, forFeedCard = false)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SmartMediaPreview(
                        spec = mediaSpec,
                        contentDescription = item.title
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title
                    Text(
                        text = item.title,
                        style = HeadlineSmall.copy(fontSize = 20.sp, lineHeight = 26.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = TextParchment,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Author / Domain Subtitle
                    Text(
                        text = if (item.sourceLabel != null) "${item.sourceLabel} • Specimen" else item.normalizedUrl,
                        style = LabelSmall,
                        color = TextLichenMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Quote / Notes Snippet Box
                    if (!item.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceTray, RoundedCornerShape(12.dp))
                                .frostedRimHighlight(RoundedCornerShape(12.dp), topAlpha = 0.20f)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "\"${item.note}\"",
                                style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 18.sp),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = TextDewSage
                            )
                        }
                    }

                    // Tag Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("#ARTICLE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextDewSage)
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("#SAVED", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextDewSage)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Floating Capsule 3-Button Action Array
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Archive Button (Left)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .background(SurfaceElevated, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(RoundedCornerShape(18.dp), topAlpha = 0.25f)
                    .tactilePress()
                    .clickable { onArchive() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Archive, null, tint = RoseSignal, modifier = Modifier.size(18.dp))
                    Text("Archive", style = TitleMedium.copy(fontSize = 12.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                }
            }

            // Open Original Action (Center)
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .height(52.dp)
                    .background(SurfaceElevated, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(RoundedCornerShape(18.dp), topAlpha = 0.25f)
                    .tactilePress()
                    .clickable { onOpenOriginal() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = if (isDarkThemeGlobal) ChampagneGold else DewSage, modifier = Modifier.size(18.dp))
                    Text("Open Specimen ↗", style = TitleMedium.copy(fontSize = 12.sp), fontWeight = FontWeight.SemiBold, color = if (isDarkThemeGlobal) TextChampagne else DewSage)
                }
            }

            // Keep Button (Right - Champagne Gold Primary CTA)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(ChampagneGold, RoundedCornerShape(18.dp))
                    .tactilePress()
                    .clickable { onKeep() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF141D16), modifier = Modifier.size(18.dp))
                    Text("Keep ✓", style = TitleMedium.copy(fontSize = 12.sp), fontWeight = FontWeight.Bold, color = Color(0xFF141D16))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Secondary Navigation Row (Prev / Guide / Next)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.20f)
                    .graphicsLayer { alpha = if (currentIndex > 1) 1f else 0.45f }
                    .clickable(enabled = currentIndex > 1) { onPrev() }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text("← Prev", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = TextDewSage)
            }

            Text(
                text = "Keep / Archive to curate",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextLichenMuted
            )

            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.20f)
                    .graphicsLayer { alpha = if (currentIndex < totalCount) 1f else 0.45f }
                    .clickable(enabled = currentIndex < totalCount) { onNext() }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text("Next →", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = TextDewSage)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Archiving marks this item as digested. Accessible anytime in Library.",
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = TextLichenMuted,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
