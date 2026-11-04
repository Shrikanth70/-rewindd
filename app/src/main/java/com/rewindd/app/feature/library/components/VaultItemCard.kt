package com.rewindd.app.feature.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.ContentOnChampagne
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.RoseSignal
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.feature.library.components.media.MediaPresentationPolicy
import com.rewindd.app.feature.library.components.media.PresentationMode
import com.rewindd.app.feature.library.components.media.SmartMediaPreview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VaultItemCard(
    itemDetails: SavedItemDetailsDomain,
    onItemClick: () -> Unit,
    onOpenOriginal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = itemDetails.item
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val formattedDate = remember(item.capturedAtUtc) { dateFormat.format(Date(item.capturedAtUtc)) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .ambientGlow(offsetY = 4.dp, radius = 16.dp)
            .background(SurfaceVitrine, RoundedCornerShape(20.dp))
            .frostedRimHighlight(RoundedCornerShape(20.dp), topAlpha = 0.25f)
            .tactilePress()
            .clickable(onClick = onItemClick)
            .padding(18.dp)
    ) {
        Column {
            // Header: Source app badge + Pinned status + Date
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
                            .background(CypressGreen, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = (item.sourceLabel ?: "WEB").uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DewSage
                        )
                    }

                    if (item.isPinned) {
                        Box(
                            modifier = Modifier
                                .background(RoseSignal.copy(alpha = 0.20f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "PINNED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoseSignal
                                )
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = null,
                                    tint = RoseSignal,
                                    modifier = Modifier.size(9.dp)
                                )
                            }
                        }
                    }

                    itemDetails.collection?.let { coll ->
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = coll.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextChampagne
                            )
                        }
                    }
                }

                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = TextLichenMuted
                )
            }

            val spec = remember(item.url, item.thumbnailUri) {
                MediaPresentationPolicy.resolve(item, forFeedCard = true)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (spec.presentationMode == PresentationMode.COMPACT_SQUARE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = HeadlineSmall.copy(fontSize = 16.sp, lineHeight = 22.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.normalizedUrl,
                            fontSize = 12.sp,
                            color = TextLichenMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    SmartMediaPreview(
                        spec = spec,
                        contentDescription = item.title
                    )
                }
            } else {
                SmartMediaPreview(
                    spec = spec,
                    contentDescription = item.title
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.title,
                    style = HeadlineSmall.copy(fontSize = 17.sp, lineHeight = 23.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = TextParchment,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.normalizedUrl,
                    fontSize = 12.sp,
                    color = TextLichenMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Margin Note Snippet (if available)
            if (!item.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(10.dp))
                        .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.20f)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "💬 ${item.note}",
                        fontSize = 12.sp,
                        color = TextDewSage,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Tags Row
            if (itemDetails.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemDetails.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.20f)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tag.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDewSage
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(SurfaceElevated, RoundedCornerShape(14.dp))
                        .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.25f)
                        .tactilePress()
                        .clickable(onClick = onItemClick)
                        .padding(horizontal = 16.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextChampagne
                    )
                }

                Box(
                    modifier = Modifier
                        .background(ChampagneGold, RoundedCornerShape(14.dp))
                        .tactilePress()
                        .clickable(onClick = onOpenOriginal)
                        .padding(horizontal = 16.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open Specimen ↗",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ContentOnChampagne
                    )
                }
            }
        }
    }
}
