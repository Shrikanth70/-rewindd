package com.rewindd.app.feature.itemdetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.ContentOnChampagne
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
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.feature.library.components.getColorFromToken
import com.rewindd.app.feature.library.components.media.MediaPresentationPolicy
import com.rewindd.app.feature.library.components.media.SmartMediaPreview
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel,
    onBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onOpenOriginal: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemDetails by viewModel.itemDetails.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var copiedFeedback by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    LaunchedEffect(copiedFeedback) {
        if (copiedFeedback) {
            delay(2500)
            copiedFeedback = false
        }
    }

    if (itemDetails == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(CanvasNoir),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading record...", style = BodyMedium, color = TextDewSage)
        }
        return
    }

    val item = itemDetails!!.item
    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy · HH:mm", Locale.getDefault()) }
    val formattedDate = remember(item.capturedAtUtc) { dateFormat.format(Date(item.capturedAtUtc)) }

    // Delete Confirmation Dialog
    if (showDeleteConfirm) {
        BasicAlertDialog(
            onDismissRequest = { showDeleteConfirm = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 6.dp, radius = 24.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Delete Saved Record?",
                        style = HeadlineSmall.copy(fontSize = 18.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Text(
                        text = "This will permanently remove this record from your offline archive.",
                        style = BodyMedium.copy(fontSize = 13.sp),
                        color = TextDewSage
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(SurfaceTray, RoundedCornerShape(12.dp))
                                .tactilePress()
                                .clickable { showDeleteConfirm = false }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cancel", style = BadgeCaps.copy(fontSize = 11.sp), color = TextDewSage)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(RoseSignal.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                                .tactilePress()
                                .clickable {
                                    showDeleteConfirm = false
                                    viewModel.deleteItem(onDeleted = onBack)
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Delete", style = BadgeCaps.copy(fontSize = 11.sp), color = RoseSignal)
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                    .tactilePress()
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextChampagne,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "Record Details",
                style = TitleMedium.copy(fontSize = 16.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                    .tactilePress()
                    .clickable(onClick = onNavigateToEdit),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = TextChampagne,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Item Details Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                // Badges row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = (item.sourceLabel ?: "WEB").uppercase(),
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = TextChampagne
                            )
                        }

                        // Review state badge
                        val stateLabel = when (item.reviewState) {
                            ReviewState.UNREVIEWED -> "INBOX"
                            ReviewState.REVIEWED -> "REVIEWED"
                            ReviewState.ARCHIVED -> "ARCHIVED"
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = stateLabel,
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = if (item.reviewState == ReviewState.UNREVIEWED) DewSage else TextChampagne
                            )
                        }
                    }

                    Text(
                        text = formattedDate,
                        style = BodyMedium.copy(fontSize = 11.sp),
                        color = TextDewSage
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                val mediaSpec = remember(item.url, item.thumbnailUri) {
                    MediaPresentationPolicy.resolve(item, forFeedCard = false)
                }

                SmartMediaPreview(
                    spec = mediaSpec,
                    contentDescription = item.title,
                    onClick = { onOpenOriginal(item.url) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = item.title,
                    style = HeadlineSmall.copy(fontSize = 20.sp, lineHeight = 26.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Copyable Link Container with Copy Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.url,
                            style = BodyMedium.copy(fontSize = 12.sp),
                            color = TextChampagne,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(8.dp))
                                .tactilePress()
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(item.url))
                                    copiedFeedback = true
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = if (copiedFeedback) DewSage else TextChampagne,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = if (copiedFeedback) "COPIED" else "COPY",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = if (copiedFeedback) DewSage else TextChampagne
                                )
                            }
                        }
                    }
                }

                // Collection
                itemDetails!!.collection?.let { coll ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Collection: ",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextDewSage
                        )
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(coll.name, style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextChampagne)
                        }
                    }
                }

                // Margin Notes Section
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "MARGIN NOTES",
                    style = BadgeCaps.copy(fontSize = 9.5.sp),
                    color = TextLichenMuted
                )

                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (!item.note.isNullOrBlank()) item.note else "No margin notes recorded yet.",
                        style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 19.sp),
                        color = if (!item.note.isNullOrBlank()) TextParchment else TextDewSage
                    )
                }

                // Tags Section
                if (itemDetails!!.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "TAGS",
                        style = BadgeCaps.copy(fontSize = 9.5.sp),
                        color = TextLichenMuted
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        itemDetails!!.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(9999.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag.displayName,
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextChampagne
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Column(
            modifier = Modifier.padding(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Open Original Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(ChampagneGold, RoundedCornerShape(14.dp))
                    .tactilePress()
                    .clickable { onOpenOriginal(item.url) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = ContentOnChampagne, modifier = Modifier.size(18.dp))
                    Text("Open Original Resource", style = BadgeCaps.copy(fontSize = 12.sp), color = ContentOnChampagne)
                }
            }

            // Edit Details Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(SurfaceElevated, RoundedCornerShape(14.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(14.dp))
                    .tactilePress()
                    .clickable(onClick = onNavigateToEdit),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = TextChampagne, modifier = Modifier.size(16.dp))
                    Text("Edit Details & Notes", style = BadgeCaps.copy(fontSize = 11.5.sp), color = TextChampagne)
                }
            }

            // Keep / Archive Toggle Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (item.reviewState != ReviewState.REVIEWED) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(SurfaceElevated, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                            .tactilePress()
                            .clickable { viewModel.keepItem() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = DewSage, modifier = Modifier.size(16.dp))
                            Text("Mark Reviewed", style = BadgeCaps.copy(fontSize = 10.5.sp), color = DewSage)
                        }
                    }
                }

                if (item.reviewState != ReviewState.ARCHIVED) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(SurfaceElevated, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                            .tactilePress()
                            .clickable { viewModel.archiveItem() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Archive, contentDescription = null, tint = TextChampagne, modifier = Modifier.size(16.dp))
                            Text("Archive", style = BadgeCaps.copy(fontSize = 10.5.sp), color = TextChampagne)
                        }
                    }
                }
            }

            // Delete Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(SurfaceTray, RoundedCornerShape(12.dp))
                    .tactilePress()
                    .clickable { showDeleteConfirm = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = RoseSignal.copy(alpha = 0.85f), modifier = Modifier.size(16.dp))
                    Text("Delete from Vault", style = BadgeCaps.copy(fontSize = 10.5.sp), color = RoseSignal.copy(alpha = 0.85f))
                }
            }
        }
    }
}
