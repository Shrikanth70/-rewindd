package com.rewindd.app.feature.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
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
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.feature.library.components.media.FallbackCategory
import com.rewindd.app.feature.library.components.media.MediaPresentationPolicy
import com.rewindd.app.feature.library.components.media.VitrineFallbackTile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TagColorPalette = listOf(
    CypressGreen,
    SurfaceElevated,
    Color(0xFF26332A),
    Color(0xFF1E2822)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryGridCard(
    itemDetails: SavedItemDetailsDomain,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = itemDetails.item
    val relativeTime = remember(item.capturedAtUtc) {
        formatRelativeTime(item.capturedAtUtc)
    }

    val spec = remember(item.url, item.thumbnailUri) {
        MediaPresentationPolicy.resolve(item, forFeedCard = true)
    }

    val isVideo = spec.fallbackCategory == FallbackCategory.VIDEO

    val sourceLabel = remember(item.sourceLabel, spec.sourceDomain) {
        item.sourceLabel?.uppercase()
            ?: spec.sourceDomain?.uppercase()
            ?: "WEB"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        // Main Card Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 3.dp, radius = 14.dp)
                .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                .frostedRimHighlight(RoundedCornerShape(18.dp), topAlpha = 0.25f)
                .tactilePress()
                .clickable(onClick = onItemClick)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Media Thumbnail Preview Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceTray)
                        .frostedRimHighlight(RoundedCornerShape(12.dp), topAlpha = 0.20f)
                ) {
                    if (!item.thumbnailUri.isNullOrBlank()) {
                        SubcomposeAsyncImage(
                            model = item.thumbnailUri,
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                VitrineFallbackTile(spec = spec, isCompact = true)
                            },
                            error = {
                                VitrineFallbackTile(spec = spec, isCompact = true)
                            }
                        )
                        // Micro-Tonal Warmth Pass
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(ChampagneGold.copy(alpha = 0.04f))
                        )
                    } else {
                        VitrineFallbackTile(spec = spec, isCompact = true)
                    }

                    // Bottom-left source pill
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .background(CanvasNoir.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                            .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.30f)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = sourceLabel,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDewSage,
                            maxLines = 1,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Video duration pill or indicator
                    if (isVideo) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(6.dp)
                                .background(CanvasNoir.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                                .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.30f)
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Video",
                                    tint = WarmAmber,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "VIDEO",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmAmber
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata Row: Relative time + Status / read time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = relativeTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextLichenMuted
                    )

                    when {
                        isVideo -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Video",
                                    tint = WarmAmber,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "Video",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WarmAmber
                                )
                            }
                        }
                        item.reviewState == ReviewState.UNREVIEWED -> {
                            Text(
                                text = "● New",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ChampagneGold
                            )
                        }
                        item.reviewState == ReviewState.REVIEWED -> {
                            Text(
                                text = "✓ Read",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DewSage
                            )
                        }
                        else -> {
                            Text(
                                text = (itemDetails.collection?.name ?: "").ifBlank { "Saved" },
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextDewSage,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Title
                Text(
                    text = item.title.ifBlank { item.url },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextParchment,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Tags Row & Divider
                if (itemDetails.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(TextLichenMuted.copy(alpha = 0.2f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemDetails.tags.take(3).forEachIndexed { idx, tag ->
                            val chipBg = TagColorPalette[idx % TagColorPalette.size]
                            Box(
                                modifier = Modifier
                                    .background(chipBg, RoundedCornerShape(6.dp))
                                    .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.20f)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "#${tag.displayName.uppercase()}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDewSage,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Pinned Badge (top-right, inset safely)
        if (item.isPinned) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = (-7).dp)
                    .zIndex(10f)
                    .ambientGlow(offsetY = 2.dp, radius = 8.dp)
                    .background(RoseSignal.copy(alpha = 0.25f), RoundedCornerShape(9999.dp))
                    .frostedRimHighlight(RoundedCornerShape(9999.dp), topAlpha = 0.35f)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = RoseSignal,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "PINNED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseSignal,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        } else if (item.reviewState == ReviewState.UNREVIEWED) {
            // Floating Unread Dot (top-left, inset safely)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 8.dp, y = (-5).dp)
                    .zIndex(10f)
                    .size(10.dp)
                    .ambientGlow(color = ChampagneGold.copy(alpha = 0.5f), offsetY = 1.dp, radius = 6.dp)
                    .background(ChampagneGold, CircleShape)
            )
        }
    }
}

private fun formatRelativeTime(capturedAtUtc: Long): String {
    val now = System.currentTimeMillis()
    val diff = (now - capturedAtUtc).coerceAtLeast(0)
    val minutes = diff / (60 * 1000)
    val hours = diff / (60 * 60 * 1000)
    val days = diff / (24 * 60 * 60 * 1000)
    val weeks = days / 7

    return when {
        minutes < 60 -> "${minutes.coerceAtLeast(1)}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        weeks < 4 -> "${weeks}w ago"
        else -> {
            val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
            sdf.format(Date(capturedAtUtc))
        }
    }
}
