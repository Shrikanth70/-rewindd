package com.rewindd.app.feature.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.LinkBlue
import com.rewindd.app.core.designsystem.theme.OnSurfaceVariant
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.SignalGreen
import com.rewindd.app.core.designsystem.theme.SpaceGrotesk
import com.rewindd.app.core.designsystem.theme.SurfaceContainer
import com.rewindd.app.core.designsystem.theme.SurfaceContainerLowest
import com.rewindd.app.core.designsystem.theme.TagLilac
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.core.designsystem.theme.brutalPress
import com.rewindd.app.core.designsystem.theme.hardShadow
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.SavedItem

/**
 * Stitch Screen 12 Save Confirmation Sheet.
 * Celebratory state shown immediately after an item is successfully vaulted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveConfirmationCard(
    savedItem: SavedItem,
    collectionName: String?,
    unreviewedCount: Int,
    onStartRewind: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onUndo: (SavedItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Paper,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Bar
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
                            .size(28.dp)
                            .hardShadow(offset = 2.dp, radius = 6.dp)
                            .background(RewindYellow, RoundedCornerShape(6.dp))
                            .border(1.5.dp, Ink, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Ink,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "REWINDD",
                        fontFamily = SpaceGrotesk,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Ink
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .hardShadow(offset = 2.dp, radius = 8.dp)
                        .background(SurfaceContainerLowest, RoundedCornerShape(8.dp))
                        .border(2.dp, Ink, RoundedCornerShape(8.dp))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close confirmation",
                        tint = Ink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Celebratory Success Hero Card (Screen 12)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(offset = 4.dp, radius = 16.dp)
                    .background(SurfaceContainerLowest, RoundedCornerShape(16.dp))
                    .border(2.dp, Ink, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Big Green Checkmark
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .hardShadow(offset = 3.dp, radius = 9999.dp)
                            .background(SignalGreen, CircleShape)
                            .border(2.dp, Ink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Ink,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Saved. Future you says thanks.",
                        fontFamily = SpaceGrotesk,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Safely indexed into your local offline vault. No cloud dependencies.",
                        style = BodyMedium,
                        color = OnSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Encrypted & Vaulted Pill
                    Box(
                        modifier = Modifier
                            .background(SurfaceContainer, RoundedCornerShape(9999.dp))
                            .border(1.5.dp, Ink, RoundedCornerShape(9999.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(SignalGreen, CircleShape)
                                    .border(1.dp, Ink, CircleShape)
                            )
                            Text(
                                text = "ENCRYPTED & VAULTED",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Saved Item Summary Card Bento
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(offset = 3.dp, radius = 12.dp)
                    .background(SurfaceContainerLowest, RoundedCornerShape(12.dp))
                    .border(2.dp, Ink, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .hardShadow(offset = 2.dp, radius = 9999.dp)
                                .background(LinkBlue, RoundedCornerShape(9999.dp))
                                .border(1.5.dp, Ink, RoundedCornerShape(9999.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = (savedItem.sourceLabel ?: "WEB").uppercase(),
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                fontWeight = FontWeight.Bold,
                                color = Paper
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "SAVED JUST NOW",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 68.dp, height = 54.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(RewindYellow)
                                .border(1.5.dp, Ink, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!savedItem.thumbnailUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = savedItem.thumbnailUri,
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 68.dp, height = 54.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Ink,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = savedItem.title,
                                fontFamily = SpaceGrotesk,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Ink,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (!collectionName.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(SurfaceContainer, RoundedCornerShape(4.dp))
                                        .border(1.dp, Ink, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "#${collectionName.uppercase()}",
                                        style = BadgeCaps.copy(fontSize = 9.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Ink
                                    )
                                }
                            }
                        }
                    }

                    // Stored Metrics Strip
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Paper, RoundedCornerShape(8.dp))
                            .border(1.5.dp, Ink, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderZip,
                                    contentDescription = null,
                                    tint = Ink,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("38 KB cached", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Ink)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Inbox,
                                    contentDescription = null,
                                    tint = Ink,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Inbox ($unreviewedCount items)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Interactive Next Actions
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Primary Start Rewind Button (Refined Luxury Sanctuary)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 10.dp)
                        .background(ChampagneGold, RoundedCornerShape(14.dp))
                        .tactilePress()
                        .clickable { onStartRewind() }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
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
                                    .size(32.dp)
                                    .background(Color(0x18000000), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color(0xFF141D16),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Start Rewind",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF141D16)
                                )
                                Text(
                                    text = "Review your queue now",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF141D16).copy(alpha = 0.75f)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF141D16),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Secondary View Library Button (Refined Luxury Sanctuary)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(SurfaceElevated, RoundedCornerShape(14.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(14.dp), topAlpha = 0.25f)
                        .tactilePress()
                        .clickable { onNavigateToLibrary() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = "View Library",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment
                        )
                    }
                }

                // Tertiary Dismiss Action
                Text(
                    text = "Done (Return to Home)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDewSage,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onDismiss() }
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Quick Undo Toast Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(offset = 2.dp, radius = 10.dp)
                    .background(Ink, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdded,
                            contentDescription = null,
                            tint = SignalGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (!collectionName.isNullOrBlank()) "Added to $collectionName" else "Added to Vault",
                            color = Paper,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Paper, RoundedCornerShape(6.dp))
                            .clickable { onUndo(savedItem) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text("Tap to undo", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Ink)
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                tint = Ink,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
