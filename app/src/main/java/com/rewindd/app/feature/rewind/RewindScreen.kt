package com.rewindd.app.feature.rewind

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.feature.rewind.components.OpenOriginalFallbackDialog
import com.rewindd.app.feature.rewind.components.RewindCardDeck
import com.rewindd.app.feature.rewind.components.RewindEmptyQueue
import com.rewindd.app.feature.rewind.components.RewindIntroGuide
import com.rewindd.app.feature.rewind.components.safeOpenUrl

@Composable
fun RewindScreen(
    viewModel: RewindViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deckState by viewModel.deckState.collectAsState()
    val undoBannerItem by viewModel.undoBannerItem.collectAsState()
    val unreviewedCount by viewModel.unreviewedCount.collectAsState()
    val context = LocalContext.current

    var fallbackDialogUrl by remember { mutableStateOf<String?>(null) }

    // Fallback dialog when external app is missing
    fallbackDialogUrl?.let { url ->
        OpenOriginalFallbackDialog(
            url = url,
            onOpenInBrowser = {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(browserIntent)
                } catch (ignored: Exception) {}
                fallbackDialogUrl = null
            },
            onDismiss = { fallbackDialogUrl = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                    .tactilePress()
                    .clickable { onNavigateToHome() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextChampagne,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Close",
                        style = BadgeCaps.copy(fontSize = 11.sp),
                        color = TextChampagne
                    )
                }
            }

            val waitingDisplay = when (val state = deckState) {
                is RewindDeckState.ActiveDeck -> "${state.currentIndex} of ${state.totalInSession} waiting"
                else -> "$unreviewedCount waiting"
            }

            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(9999.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "● $waitingDisplay",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = TextChampagne
                )
            }
        }

        // Inline Undo Banner
        undoBannerItem?.let { item ->
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(12.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DewSage,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Moved out of your inbox. Safe in your Library archive. Never deleted.",
                            color = TextParchment,
                            style = BodyMedium.copy(fontSize = 11.sp, lineHeight = 15.sp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(8.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(8.dp))
                            .tactilePress()
                            .clickable { viewModel.undoArchive() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Undo",
                                style = BadgeCaps.copy(fontSize = 11.sp),
                                color = TextChampagne
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Undo,
                                contentDescription = null,
                                tint = TextChampagne,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main State Content
        when (val state = deckState) {
            is RewindDeckState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TextChampagne)
                }
            }

            is RewindDeckState.IntroGuide -> {
                RewindIntroGuide(
                    onStartDeck = { viewModel.skipIntro() },
                    modifier = Modifier.padding(16.dp)
                )
            }

            is RewindDeckState.ActiveDeck -> {
                RewindCardDeck(
                    item = state.currentItem,
                    currentIndex = state.currentIndex,
                    totalCount = state.totalInSession,
                    onKeep = { viewModel.keepCurrentItem() },
                    onArchive = { viewModel.archiveCurrentItem() },
                    onOpenOriginal = {
                        safeOpenUrl(
                            context = context,
                            url = state.currentItem.url,
                            onFallbackRequired = {
                                fallbackDialogUrl = state.currentItem.url
                            }
                        )
                    },
                    onPrev = { viewModel.previousItem() },
                    onNext = { viewModel.nextItem() },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }

            is RewindDeckState.EmptyQueue -> {
                RewindEmptyQueue(
                    totalReviewedToday = state.totalReviewedToday,
                    onNavigateToHome = onNavigateToHome,
                    onNavigateToLibrary = onNavigateToLibrary
                )
            }
        }
    }
}
