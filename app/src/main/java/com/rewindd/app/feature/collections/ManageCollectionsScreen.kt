package com.rewindd.app.feature.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.feature.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCollectionsScreen(
    viewModel: LibraryViewModel,
    onBack: () -> Unit,
    onOpenCollectionDetail: (String) -> Unit,
    onCreateCollection: () -> Unit,
    onEditCollection: (Collection) -> Unit,
    modifier: Modifier = Modifier
) {
    val collections by viewModel.allCollections.collectAsState()
    val itemCounts by viewModel.collectionItemCounts.collectAsState()

    var selectedActionCollection by remember { mutableStateOf<Collection?>(null) }
    var collectionToDelete by remember { mutableStateOf<Collection?>(null) }

    // Bottom Action Sheet for selected collection
    if (selectedActionCollection != null) {
        val target = selectedActionCollection!!
        val count = itemCounts[target.id] ?: 0
        CollectionActionBottomSheet(
            collection = target,
            itemCount = count,
            onDismiss = { selectedActionCollection = null },
            onBrowseItems = {
                val id = target.id
                selectedActionCollection = null
                onOpenCollectionDetail(id)
            },
            onEdit = {
                selectedActionCollection = null
                onEditCollection(target)
            },
            onTogglePin = {
                viewModel.updateCollection(target.copy(isPinned = !target.isPinned))
                selectedActionCollection = null
            },
            onDelete = {
                selectedActionCollection = null
                collectionToDelete = target
            }
        )
    }

    // Delete Confirmation Dialog
    if (collectionToDelete != null) {
        val target = collectionToDelete!!
        BasicAlertDialog(
            onDismissRequest = { collectionToDelete = null }
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
                        text = "Delete \"${target.name}\"?",
                        style = HeadlineSmall.copy(fontSize = 18.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Text(
                        text = "Items in this collection will NOT be deleted; they will safely return to your unassigned inbox.",
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
                                .clickable { collectionToDelete = null }
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
                                    viewModel.deleteCollection(target.id)
                                    collectionToDelete = null
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
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                text = "Collections",
                style = HeadlineSmall.copy(fontSize = 18.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Box(
                modifier = Modifier
                    .background(ChampagneGold, RoundedCornerShape(10.dp))
                    .tactilePress()
                    .clickable(onClick = onCreateCollection)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Collection",
                        tint = ContentOnChampagne,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "New",
                        style = BadgeCaps.copy(fontSize = 10.5.sp),
                        color = ContentOnChampagne
                    )
                }
            }
        }

        // Body Content
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(collections, key = { it.id }) { col ->
                val count = itemCounts[col.id] ?: 0
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                        .tactilePress()
                        .clickable { selectedActionCollection = col }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = col.iconToken, fontSize = 20.sp)
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = col.name,
                                        style = TitleMedium.copy(fontSize = 15.sp),
                                        fontFamily = EditorialSerif,
                                        color = TextParchment
                                    )
                                    if (col.isPinned) {
                                        Icon(
                                            imageVector = Icons.Default.PushPin,
                                            contentDescription = "Pinned",
                                            tint = ChampagneGold,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "$count ${if (count == 1) "record" else "records"}",
                                    style = BodyMedium.copy(fontSize = 12.sp),
                                    color = TextDewSage
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextDewSage,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionActionBottomSheet(
    collection: Collection,
    itemCount: Int,
    onDismiss: () -> Unit,
    onBrowseItems: () -> Unit,
    onEdit: () -> Unit,
    onTogglePin: () -> Unit,
    onDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CanvasNoir,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = collection.iconToken, fontSize = 22.sp)
                }

                Column {
                    Text(
                        text = collection.name,
                        style = HeadlineSmall.copy(fontSize = 18.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Text(
                        text = "$itemCount saved records in this vault",
                        style = BodyMedium.copy(fontSize = 12.sp),
                        color = TextDewSage
                    )
                }
            }

            // Action 1: Browse Items
            ActionRowItem(
                title = "Browse Saved Records",
                subtitle = "View and manage links in this collection",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                isHighlight = true,
                onClick = onBrowseItems
            )

            // Action 2: Edit Name & Theme
            ActionRowItem(
                title = "Edit Collection",
                subtitle = "Rename icon or theme color",
                icon = Icons.Default.Edit,
                onClick = onEdit
            )

            // Action 3: Pin / Unpin
            ActionRowItem(
                title = if (collection.isPinned) "Unpin from Top Rail" else "Pin to Top Rail",
                subtitle = if (collection.isPinned) "Move back to regular position" else "Show first in Library rail",
                icon = Icons.Default.PushPin,
                onClick = onTogglePin
            )

            // Action 4: Delete Collection
            ActionRowItem(
                title = "Delete Collection",
                subtitle = "Records return safely to your inbox",
                icon = Icons.Default.Delete,
                isDestructive = true,
                onClick = onDelete
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ActionRowItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isHighlight: Boolean = false,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isHighlight) SurfaceElevated else SurfaceTray,
                RoundedCornerShape(14.dp)
            )
            .frostedRimHighlight(
                shape = RoundedCornerShape(14.dp),
                topAlpha = if (isHighlight) 0.30f else 0.10f
            )
            .tactilePress()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = when {
                            isDestructive -> RoseSignal
                            isHighlight -> ChampagneGold
                            else -> TextChampagne
                        },
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        style = TitleMedium.copy(fontSize = 14.sp),
                        color = if (isDestructive) RoseSignal else TextParchment
                    )
                    Text(
                        text = subtitle,
                        style = BodyMedium.copy(fontSize = 11.5.sp),
                        color = TextDewSage
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = TextDewSage,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
