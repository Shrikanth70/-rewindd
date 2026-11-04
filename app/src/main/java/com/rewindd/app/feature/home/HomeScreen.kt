package com.rewindd.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.HotPink
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.LabelSmall
import com.rewindd.app.core.designsystem.theme.LinkBlue
import com.rewindd.app.core.designsystem.theme.OnSurfaceVariant
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.SignalGreen
import com.rewindd.app.core.designsystem.theme.SurfaceContainer
import com.rewindd.app.core.designsystem.theme.SurfaceContainerLowest
import com.rewindd.app.core.designsystem.theme.TagLilac
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.brutalPress
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
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
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.feature.capture.DuplicateLinkBottomSheet
import com.rewindd.app.feature.library.SaveFeedbackEvent
import com.rewindd.app.feature.library.components.getColorFromToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    feedbackEvents: SharedFlow<SaveFeedbackEvent>,
    collections: List<Collection> = emptyList(),
    savedItems: List<SavedItemDetailsDomain> = emptyList(),
    onOpenCollection: (String) -> Unit = {},
    onNavigateToItemDetail: (String) -> Unit = {},
    onStartRewind: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToManageCollections: () -> Unit = {},
    onOpenSettings: () -> Unit,
    onQuickSaveUrl: (String) -> Unit = {},
    onOpenQuickCapture: (String?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val unreviewedCount by homeViewModel.unreviewedCount.collectAsState()
    val topUnreviewedItem by homeViewModel.topUnreviewedItem.collectAsState()

    var quickUrlInput by remember { mutableStateOf("") }
    var bannerMessage by remember { mutableStateOf<String?>(null) }
    var bannerColor by remember { mutableStateOf(SignalGreen) }
    var duplicateDetectedItem by remember { mutableStateOf<SavedItem?>(null) }
    val lazyListState = rememberLazyListState()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    var isInputFocused by remember { mutableStateOf(false) }

    val isImeVisible = WindowInsets.isImeVisible
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(isImeVisible, imeBottom, isInputFocused) {
        if (isInputFocused && (isImeVisible || imeBottom > 0)) {
            delay(150)
            bringIntoViewRequester.bringIntoView()
        }
    }

    LaunchedEffect(Unit) {
        feedbackEvents.collect { event ->
            when (event) {
                is SaveFeedbackEvent.Saved -> {
                    bannerMessage = "✓ Saved to Library: ${event.item.title}"
                    bannerColor = SignalGreen
                    quickUrlInput = ""
                }
                is SaveFeedbackEvent.DuplicateDetected -> {
                    duplicateDetectedItem = event.existingItem
                }
                is SaveFeedbackEvent.InvalidUrl -> {
                    bannerMessage = "✕ Invalid URL: ${event.reason}"
                    bannerColor = HotPink
                }
                is SaveFeedbackEvent.Error -> {
                    bannerMessage = "✕ Error: ${event.message}"
                    bannerColor = HotPink
                }
            }
            delay(4000)
            bannerMessage = null
        }
    }

    // Screen 13: Duplicate Link Collision Guard Bottom Sheet
    duplicateDetectedItem?.let { item ->
        DuplicateLinkBottomSheet(
            existingItem = item,
            onViewInVault = { itemId ->
                duplicateDetectedItem = null
                onNavigateToItemDetail(itemId)
            },
            onUpdateNotes = { itemId ->
                duplicateDetectedItem = null
                onNavigateToItemDetail(itemId)
            },
            onDismiss = { duplicateDetectedItem = null }
        )
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(bottom = 96.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Animated Save Feedback Banner (DEF-02)
            bannerMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .ambientGlow(offsetY = 4.dp, radius = 12.dp)
                        .background(SurfaceElevated, RoundedCornerShape(16.dp))
                        .frostedRimHighlight(RoundedCornerShape(16.dp), topAlpha = 0.35f)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = msg,
                        style = LabelSmall.copy(fontSize = 13.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = bannerColor
                    )
                }
            }

            // DATE SUBHEAD
            val currentDateFormatted = remember {
                SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date()).uppercase()
            }
            Text(
                text = currentDateFormatted,
                style = BadgeCaps.copy(fontSize = 11.sp, letterSpacing = 0.12.sp),
                color = TextLichenMuted,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // SECTION 1: QUICK CAPTURE / PRESERVE A LINK
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .ambientGlow(offsetY = 4.dp, radius = 18.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(22.dp))
                    .frostedRimHighlight(RoundedCornerShape(22.dp), topAlpha = 0.28f)
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                .frostedRimHighlight(RoundedCornerShape(8.dp), topAlpha = 0.25f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentPaste,
                                contentDescription = null,
                                tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Preserve Specimen",
                            style = TitleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = quickUrlInput,
                            onValueChange = { quickUrlInput = it },
                            placeholder = { Text("https://...", style = BodyMedium, color = TextLichenMuted) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { focusState ->
                                    isInputFocused = focusState.isFocused
                                    if (focusState.isFocused) {
                                        coroutineScope.launch {
                                            delay(150)
                                            bringIntoViewRequester.bringIntoView()
                                        }
                                    }
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChampagneGold.copy(alpha = 0.6f),
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = SurfaceTray,
                                unfocusedContainerColor = SurfaceTray,
                                focusedTextColor = TextParchment,
                                unfocusedTextColor = TextParchment
                            )
                        )

                        Box(
                            modifier = Modifier
                                .height(52.dp)
                                .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                                .background(ChampagneGold, RoundedCornerShape(14.dp))
                                .tactilePress()
                                .clickable {
                                    onOpenQuickCapture(quickUrlInput.ifBlank { null })
                                    quickUrlInput = ""
                                }
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Save",
                                    style = TitleMedium.copy(fontSize = 14.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF141D16)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = Color(0xFF141D16),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null,
                            tint = TextLichenMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Or share directly to Rewindd from any app via Android share sheet.",
                            style = LabelSmall.copy(fontSize = 11.sp),
                            color = TextLichenMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 2: DAILY REWIND / INBOX QUEUE HERO CARD
            val topItem = topUnreviewedItem
            if (topItem != null && unreviewedCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 6.dp, radius = 24.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(26.dp))
                        .frostedRimHighlight(RoundedCornerShape(26.dp), topAlpha = 0.28f)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (isDarkThemeGlobal) ChampagneGold else WarmAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "INBOX QUEUE",
                                    style = BadgeCaps,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkThemeGlobal) TextChampagne else WarmAmber
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(DewSage.copy(alpha = 0.18f), RoundedCornerShape(9999.dp))
                                    .frostedRimHighlight(RoundedCornerShape(9999.dp), topAlpha = 0.25f)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "$unreviewedCount waiting",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DewSage
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$unreviewedCount specimens waiting for recall",
                            style = TitleMedium.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Medium,
                            color = TextParchment
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Nested Top Item Vitrine Tray
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceTray, RoundedCornerShape(18.dp))
                                .frostedRimHighlight(RoundedCornerShape(18.dp), topAlpha = 0.20f)
                                .clip(RoundedCornerShape(18.dp))
                        ) {
                            Column {
                                if (!topItem.thumbnailUri.isNullOrBlank()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = topItem.thumbnailUri,
                                            contentDescription = topItem.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        // Deep Gradient Scrim for media harmonization
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(
                                                            Color.Transparent,
                                                            SurfaceTray.copy(alpha = 0.4f),
                                                            SurfaceTray
                                                        )
                                                    )
                                                )
                                        )
                                    }
                                }

                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(CypressGreen, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = topItem.sourceLabel?.uppercase() ?: "SAVED LINK",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = DewSage
                                            )
                                        }

                                        Text(
                                            text = topItem.normalizedUrl.take(24) + if (topItem.normalizedUrl.length > 24) "…" else "",
                                            style = LabelSmall,
                                            color = TextLichenMuted
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = topItem.title,
                                        style = TitleMedium.copy(fontSize = 16.sp, lineHeight = 22.sp),
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextParchment,
                                        maxLines = 2
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Primary Action: Start Rewind Capsule Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                                .background(ChampagneGold, RoundedCornerShape(16.dp))
                                .tactilePress()
                                .clickable(onClick = onStartRewind),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Start Rewind",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF141D16)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Bolt,
                                    contentDescription = null,
                                    tint = Color(0xFF141D16),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Peaceful Sanctuary Empty Queue Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 4.dp, radius = 20.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(24.dp))
                        .frostedRimHighlight(RoundedCornerShape(24.dp), topAlpha = 0.25f)
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "✨", fontSize = 16.sp)
                                Text(
                                    text = "INBOX QUEUE",
                                    style = BadgeCaps,
                                    fontWeight = FontWeight.Bold,
                                    color = TextChampagne
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(DewSage.copy(alpha = 0.18f), RoundedCornerShape(9999.dp))
                                    .frostedRimHighlight(RoundedCornerShape(9999.dp), topAlpha = 0.25f)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "All caught up",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DewSage
                                )
                            }
                        }

                        Text(
                            text = "Vault in Perfect Harmony 🎯",
                            style = TitleMedium.copy(fontSize = 17.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment
                        )

                        Text(
                            text = "You've reviewed all saved links. Save content from any app via the Share sheet or quick capture above to queue for your next recall.",
                            style = BodyMedium.copy(fontSize = 13.sp),
                            color = TextDewSage,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .background(SurfaceElevated, RoundedCornerShape(14.dp))
                                .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.25f)
                                .tactilePress()
                                .clickable(onClick = onNavigateToLibrary),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Inventory2,
                                    contentDescription = null,
                                    tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Explore Library Vault",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkThemeGlobal) TextChampagne else DewSage
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 3: CURATED COLLECTIONS HORIZONTAL SCROLL
            val countPerCollection = remember(savedItems) {
                savedItems.groupBy { it.item.collectionId }.mapValues { it.value.size }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURATED DECKS (${collections.size})",
                        style = BadgeCaps,
                        color = TextLichenMuted
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "MANAGE ⚙",
                            style = BadgeCaps,
                            color = if (isDarkThemeGlobal) TextChampagne else DewSage,
                            modifier = Modifier.clickable { onNavigateToManageCollections() }
                        )
                        Text(
                            text = "VIEW ALL →",
                            style = BadgeCaps,
                            color = if (isDarkThemeGlobal) TextChampagne else DewSage,
                            modifier = Modifier.clickable { onNavigateToLibrary() }
                        )
                    }
                }

                if (collections.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                            .frostedRimHighlight(RoundedCornerShape(16.dp), topAlpha = 0.20f)
                            .clickable(onClick = onNavigateToLibrary)
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No collections yet. Tap to create one in Library.",
                            style = LabelSmall,
                            color = TextLichenMuted
                        )
                    }
                } else {
                    val sortedCols = remember(collections) {
                        collections.sortedWith(
                            compareByDescending<Collection> { it.isPinned }
                                .thenBy { it.sortOrder }
                                .thenBy { it.name.lowercase() }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        sortedCols.forEach { col ->
                            val count = countPerCollection[col.id] ?: 0
                            val accentColor = getColorFromToken(col.colorToken)

                            CollectionTile(
                                title = col.name,
                                subtitle = "$count saved",
                                emoji = col.iconToken,
                                accentColor = accentColor,
                                onClick = { onOpenCollection(col.id) },
                                modifier = Modifier.width(148.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun CollectionTile(
    title: String,
    subtitle: String,
    emoji: String,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .ambientGlow(offsetY = 3.dp, radius = 12.dp)
            .background(SurfaceVitrine, RoundedCornerShape(18.dp))
            .frostedRimHighlight(RoundedCornerShape(18.dp), topAlpha = 0.25f)
            .tactilePress()
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.30f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = TitleMedium.copy(fontSize = 15.sp),
                fontWeight = FontWeight.SemiBold,
                color = TextParchment
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = LabelSmall,
                color = TextDewSage
            )
        }
    }
}
