package com.rewindd.app.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.DisplayLarge
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.feature.library.components.LibraryGridCard
import com.rewindd.app.feature.library.components.TaggedCollectionsRail

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToItemDetail: (String) -> Unit = {},
    onNavigateToCreateCollection: () -> Unit = {},
    onOpenCollectionDetail: (String) -> Unit = {},
    onNavigateToManageCollections: () -> Unit = {},
    onOpenOriginal: (String) -> Unit = {},
    onOpenFilterSheet: () -> Unit = {},
    onNavigateToTags: () -> Unit = {},
    onOpenQuickCapture: (String?) -> Unit = {}
) {
    val items by viewModel.filteredItems.collectAsState()
    val allCollections by viewModel.allCollections.collectAsState()
    val collectionCounts by viewModel.collectionItemCounts.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val selectedCollectionId by viewModel.selectedCollectionId.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()
    val counts by viewModel.counts.collectAsState()

    var sortDropdownExpanded by remember { mutableStateOf(false) }

    val selectedCollection = remember(selectedCollectionId, allCollections) {
        allCollections.find { it.id == selectedCollectionId }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        // Top section: Title, Search, Status Tabs, Tagged Collections, Content Heading & Sort
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // SPAN 2: Sub-Header (Title, Count Badge, "+ COLLECTION" Button)
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Your Library",
                                    style = DisplayLarge.copy(fontSize = 24.sp),
                                    fontFamily = EditorialSerif,
                                    color = TextParchment,
                                    letterSpacing = (-0.5).sp
                                )

                                // Champagne Count Pill Badge: "$total SAVED ITEMS"
                                Box(
                                    modifier = Modifier
                                        .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                                        .frostedRimHighlight(shape = RoundedCornerShape(9999.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${counts.total} SAVED ITEMS",
                                        style = BadgeCaps.copy(fontSize = 9.sp),
                                        softWrap = false,
                                        maxLines = 1,
                                        color = TextChampagne
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Mindful digital archives & synthesis",
                                style = BodyMedium.copy(fontSize = 13.sp),
                                color = TextDewSage
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // "+ COLLECTION" Action Button
                        Box(
                            modifier = Modifier
                                .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                                .background(SurfaceElevated, RoundedCornerShape(12.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                                .tactilePress()
                                .clickable(onClick = onNavigateToCreateCollection)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddBox,
                                    contentDescription = "Add Collection",
                                    tint = TextChampagne,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "COLLECTION",
                                    style = BadgeCaps.copy(fontSize = 11.sp),
                                    color = TextChampagne
                                )
                            }
                        }
                    }
                }
            }

            // SPAN 2: Search and Filter Row
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search Bar
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextParchment
                        ),
                        cursorBrush = SolidColor(ChampagneGold),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(SurfaceTray, RoundedCornerShape(12.dp))
                                    .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.15f)
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TextDewSage,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search titles, notes, tags...",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = TextLichenMuted
                                        )
                                    }
                                    innerTextField()
                                }

                                if (searchQuery.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = TextDewSage,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable { viewModel.setSearchQuery("") }
                                    )
                                }
                            }
                        }
                    )

                    // Filter Button with Active Counter Badge
                    Box(
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SurfaceElevated, RoundedCornerShape(12.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                                .tactilePress()
                                .clickable(onClick = onOpenFilterSheet),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filters",
                                tint = TextChampagne,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Active count badge if filters applied
                        if (counts.activeFiltersCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 3.dp, y = (-3).dp)
                                    .zIndex(10f)
                                    .size(18.dp)
                                    .background(WarmAmber, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${counts.activeFiltersCount}",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = Color(0xFF141D16)
                                )
                            }
                        }
                    }
                }
            }

            // SPAN 2: Segmented Status Filter Tabs (ALL, INBOX, ARCHIVED, PINNED)
            item(span = { GridItemSpan(2) }) {
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ALL Chip
                    StatusFilterPill(
                        title = "ALL",
                        count = counts.all,
                        isSelected = activeTab == LibraryTab.ALL,
                        onClick = { viewModel.setTab(LibraryTab.ALL) }
                    )

                    // INBOX Chip
                    StatusFilterPill(
                        title = "INBOX",
                        count = counts.inbox,
                        isSelected = activeTab == LibraryTab.INBOX,
                        onClick = { viewModel.setTab(LibraryTab.INBOX) }
                    )

                    // ARCHIVED Chip
                    StatusFilterPill(
                        title = "ARCHIVED",
                        count = counts.archived,
                        isSelected = activeTab == LibraryTab.ARCHIVED,
                        onClick = { viewModel.setTab(LibraryTab.ARCHIVED) }
                    )

                    // PINNED Chip
                    StatusFilterPill(
                        title = "PINNED",
                        count = counts.pinned,
                        isSelected = activeTab == LibraryTab.PINNED,
                        hasPinIcon = true,
                        onClick = { viewModel.setTab(LibraryTab.PINNED) }
                    )
                }
            }

            // SPAN 2: Tagged Collections Rail
            item(span = { GridItemSpan(2) }) {
                TaggedCollectionsRail(
                    collections = allCollections,
                    itemCounts = collectionCounts,
                    selectedCollectionId = selectedCollectionId,
                    onCollectionSelected = { viewModel.selectCollection(it) },
                    onManageClick = onNavigateToManageCollections,
                    onAddCollectionClick = onNavigateToCreateCollection,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // SPAN 2: Content Heading & Sort Row
            item(span = { GridItemSpan(2) }) {
                val headingText = when {
                    selectedCollection != null -> "Collection: ${selectedCollection.name}"
                    activeTab == LibraryTab.INBOX -> "Showing Inbox (${items.size})"
                    activeTab == LibraryTab.ARCHIVED -> "Showing Archived (${items.size})"
                    activeTab == LibraryTab.PINNED -> "Showing Pinned (${items.size})"
                    searchQuery.isNotBlank() -> "Results for \"$searchQuery\" (${items.size})"
                    else -> "Showing All Content"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = headingText,
                        style = TitleMedium.copy(fontSize = 15.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )

                    // Sort dropdown button
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(8.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(8.dp), topAlpha = 0.12f)
                                .clickable { sortDropdownExpanded = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Sort: ${sortMode.label}",
                                style = BadgeCaps.copy(fontSize = 10.sp),
                                color = TextChampagne
                            )
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = "Change sort",
                                tint = TextChampagne,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = sortDropdownExpanded,
                            onDismissRequest = { sortDropdownExpanded = false },
                            modifier = Modifier
                                .background(SurfaceElevated)
                                .frostedRimHighlight(shape = RoundedCornerShape(8.dp))
                        ) {
                            LibrarySortMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = mode.label,
                                            fontWeight = if (sortMode == mode) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortMode == mode) TextChampagne else TextDewSage,
                                            fontSize = 13.sp
                                        )
                                    },
                                    onClick = {
                                        viewModel.setSortMode(mode)
                                        sortDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Content Grid Cards or Empty State
            if (items.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                            .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when {
                                    searchQuery.isNotBlank() -> "No Matching Results"
                                    selectedCollection != null -> "Collection Empty"
                                    activeTab == LibraryTab.INBOX -> "Inbox Zero"
                                    activeTab == LibraryTab.ARCHIVED -> "No Archived Items"
                                    activeTab == LibraryTab.PINNED -> "No Pinned Items"
                                    else -> "Your Library is Empty"
                                },
                                style = TitleMedium.copy(fontSize = 16.sp),
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when {
                                    searchQuery.isNotBlank() -> "Try searching for a different keyword or clearing your query."
                                    selectedCollection != null -> "Save links to \"${selectedCollection.name}\" to organize your reading."
                                    activeTab == LibraryTab.INBOX -> "All caught up! Head to Rewind or browse all saved content."
                                    activeTab == LibraryTab.ARCHIVED -> "Items you mark as archived will appear here."
                                    activeTab == LibraryTab.PINNED -> "Pin essential cards to keep them at the top of your library."
                                    else -> "Share links from apps or use quick capture to build your library."
                                },
                                style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 17.sp),
                                color = TextDewSage,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    items = items,
                    key = { it.item.id }
                ) { itemDetails ->
                    LibraryGridCard(
                        itemDetails = itemDetails,
                        onItemClick = { onNavigateToItemDetail(itemDetails.item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusFilterPill(
    title: String,
    count: Int,
    isSelected: Boolean,
    hasPinIcon: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) SurfaceElevated else SurfaceTray,
                shape = RoundedCornerShape(9999.dp)
            )
            .frostedRimHighlight(
                shape = RoundedCornerShape(9999.dp),
                topAlpha = if (isSelected) 0.35f else 0.12f
            )
            .tactilePress()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (hasPinIcon) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    tint = if (isSelected) (if (isDarkThemeGlobal) ChampagneGold else DewSage) else TextLichenMuted,
                    modifier = Modifier.size(13.dp)
                )
            }

            Text(
                text = title,
                style = BadgeCaps.copy(fontSize = 11.sp),
                color = if (isSelected) (if (isDarkThemeGlobal) TextChampagne else DewSage) else TextDewSage
            )

            // Nested Counter Pill
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) SurfaceVitrine else SurfaceElevated,
                        shape = RoundedCornerShape(9999.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "$count",
                    style = BadgeCaps.copy(fontSize = 9.5.sp),
                    color = if (isSelected) TextChampagne else TextLichenMuted
                )
            }
        }
    }
}
