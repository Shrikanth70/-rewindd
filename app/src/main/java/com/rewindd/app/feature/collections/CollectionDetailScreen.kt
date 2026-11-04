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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.rewindd.app.feature.library.LibraryViewModel
import com.rewindd.app.feature.library.components.LibraryGridCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    collectionId: String,
    viewModel: LibraryViewModel,
    onBack: () -> Unit,
    onNavigateToItemDetail: (String) -> Unit,
    onEditCollection: (com.rewindd.app.domain.model.Collection) -> Unit,
    modifier: Modifier = Modifier
) {
    val allCollections by viewModel.allCollections.collectAsState()
    val allItems by viewModel.rawItemsWithDetails.collectAsState()

    val collection = allCollections.find { it.id == collectionId }
    val collectionItems = allItems.filter { it.item.collectionId == collectionId }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showAddItemsSheet by remember { mutableStateOf(false) }

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
                        text = "Delete Collection?",
                        style = HeadlineSmall.copy(fontSize = 18.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Text(
                        text = "Records in this collection will NOT be deleted; they will safely return to your unassigned inbox.",
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
                                    viewModel.deleteCollection(collectionId)
                                    onBack()
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

    if (showAddItemsSheet && collection != null) {
        ModalBottomSheet(
            onDismissRequest = { showAddItemsSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = CanvasNoir,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Add Records to \"${collection.name}\"",
                    style = HeadlineSmall.copy(fontSize = 18.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )
                Spacer(modifier = Modifier.height(12.dp))

                val availableItems = allItems.filter { it.item.collectionId != collectionId }
                if (availableItems.isEmpty()) {
                    Text(
                        text = "All saved records in your vault are already in this collection.",
                        style = BodyMedium.copy(fontSize = 13.sp),
                        color = TextDewSage,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(availableItems, key = { it.item.id }) { itemDetails ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                                        .tactilePress()
                                        .clickable {
                                            viewModel.assignItemToCollection(itemDetails.item.id, collectionId)
                                        }
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = itemDetails.item.title,
                                            style = TitleMedium.copy(fontSize = 13.sp),
                                            color = TextParchment,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = itemDetails.item.normalizedUrl,
                                            style = BodyMedium.copy(fontSize = 11.sp),
                                            color = TextDewSage,
                                            maxLines = 1
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("+ ADD", style = BadgeCaps.copy(fontSize = 9.sp), color = TextChampagne)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (collection == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(CanvasNoir),
            contentAlignment = Alignment.Center
        ) {
            Text("Collection not found", style = BodyMedium, color = TextDewSage)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                                    .tactilePress()
                                    .clickable { onEditCollection(collection) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Collection",
                                    tint = TextChampagne,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                                    .tactilePress()
                                    .clickable { showDeleteConfirm = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Collection",
                                    tint = RoseSignal.copy(alpha = 0.85f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Collection Hero Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                            .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(12.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = collection.iconToken, fontSize = 24.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = collection.name,
                                    style = HeadlineSmall.copy(fontSize = 20.sp),
                                    fontFamily = EditorialSerif,
                                    color = TextParchment
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${collectionItems.size} saved records in this vault",
                                    style = BodyMedium.copy(fontSize = 12.sp),
                                    color = TextDewSage
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(ChampagneGold, RoundedCornerShape(10.dp))
                                    .tactilePress()
                                    .clickable { showAddItemsSheet = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = ContentOnChampagne,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Add",
                                        style = BadgeCaps.copy(fontSize = 10.sp),
                                        color = ContentOnChampagne
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (collectionItems.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                            .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Collection is empty",
                                style = TitleMedium.copy(fontSize = 16.sp),
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap 'Add' above to link existing saved records to this collection.",
                                style = BodyMedium.copy(fontSize = 12.sp),
                                color = TextDewSage,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(collectionItems, key = { it.item.id }) { itemDetails ->
                    LibraryGridCard(
                        itemDetails = itemDetails,
                        onItemClick = { onNavigateToItemDetail(itemDetails.item.id) }
                    )
                }
            }
        }
    }
}
