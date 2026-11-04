package com.rewindd.app.feature.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.usecase.UrlMetadataExtractor
import com.rewindd.app.domain.usecase.UrlNormalizer
import com.rewindd.app.domain.usecase.UrlValidationResult
import com.rewindd.app.domain.model.Collection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickCaptureSheet(
    collections: List<Collection>,
    isSaving: Boolean,
    onSave: (url: String, collectionId: String?, note: String?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialUrl: String? = null,
    initialCollectionId: String? = null,
    initialNote: String? = null,
    onCreateCollectionRequested: (currentUrl: String, currentNote: String) -> Unit = { _, _ -> }
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    var urlInput by remember { mutableStateOf(initialUrl ?: "") }
    var selectedCollectionId by remember { mutableStateOf(initialCollectionId) }
    var noteInput by remember { mutableStateOf(initialNote ?: "") }

    LaunchedEffect(initialUrl) {
        if (!initialUrl.isNullOrBlank()) {
            urlInput = initialUrl
        }
    }

    // Auto-read clipboard if urlInput is empty
    LaunchedEffect(Unit) {
        if (urlInput.isBlank()) {
            val clipText = clipboardManager.getText()?.text
            if (!clipText.isNullOrBlank() && (clipText.startsWith("http://") || clipText.startsWith("https://"))) {
                urlInput = clipText.trim()
            }
        }
    }

    LaunchedEffect(initialCollectionId) {
        if (initialCollectionId != null) {
            selectedCollectionId = initialCollectionId
        }
    }

    val validationResult = remember(urlInput) {
        UrlNormalizer.extractAndNormalize(urlInput)
    }

    val isValidUrl = validationResult is UrlValidationResult.Valid
    val validInfo = validationResult as? UrlValidationResult.Valid

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CanvasNoir,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
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
                            .size(10.dp)
                            .background(DewSage, CircleShape)
                    )
                    Text(
                        text = "SAVE TO SANCTUARY",
                        style = BadgeCaps.copy(fontSize = 11.sp),
                        color = TextChampagne
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(SurfaceElevated, CircleShape)
                        .frostedRimHighlight(shape = CircleShape)
                        .tactilePress()
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextDewSage,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Value prop
            Text(
                text = "Capture with Intention",
                style = HeadlineSmall.copy(fontSize = 22.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Save any link, video, or thread to your 100% offline archive.",
                style = BodyMedium.copy(fontSize = 13.sp),
                color = TextDewSage
            )

            Spacer(modifier = Modifier.height(18.dp))

            // URL Input Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                    .padding(14.dp)
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
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = TextChampagne,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "RESOURCE LINK",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = TextDewSage
                            )
                        }

                        // Paste from clipboard chip
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .tactilePress()
                                .clickable {
                                    val clipText = clipboardManager.getText()?.text
                                    if (!clipText.isNullOrBlank()) {
                                        urlInput = clipText.trim()
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = null,
                                    tint = TextChampagne,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "PASTE",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextChampagne
                                )
                            }
                        }
                    }

                    // Input Field
                    BasicTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        singleLine = false,
                        maxLines = 3,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = TextParchment,
                            lineHeight = 19.sp
                        ),
                        cursorBrush = SolidColor(ChampagneGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(10.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(10.dp), topAlpha = 0.12f)
                            .padding(12.dp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (urlInput.isEmpty()) {
                                    Text(
                                        text = "https://example.com/article-or-video",
                                        style = BodyMedium.copy(fontSize = 13.sp),
                                        color = TextLichenMuted
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    // Detected Source Badge
                    if (isValidUrl && validInfo != null) {
                        val domain = validInfo.domain.removePrefix("www.")
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Source: $domain",
                                style = BadgeCaps.copy(fontSize = 9.5.sp),
                                color = DewSage
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Collection Pills Rail
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "COLLECTION",
                    style = BadgeCaps.copy(fontSize = 9.sp),
                    color = TextLichenMuted
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Inbox / None option
                    val isInboxSelected = selectedCollectionId == null
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isInboxSelected) SurfaceElevated else SurfaceTray,
                                shape = RoundedCornerShape(9999.dp)
                            )
                            .frostedRimHighlight(shape = RoundedCornerShape(9999.dp), topAlpha = if (isInboxSelected) 0.35f else 0.12f)
                            .tactilePress()
                            .clickable { selectedCollectionId = null }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Inbox (Unassigned)",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = if (isInboxSelected) TextChampagne else TextDewSage
                        )
                    }

                    // Existing Collections
                    collections.forEach { col ->
                        val isSelected = selectedCollectionId == col.id
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) SurfaceElevated else SurfaceTray,
                                    shape = RoundedCornerShape(9999.dp)
                                )
                                .frostedRimHighlight(shape = RoundedCornerShape(9999.dp), topAlpha = if (isSelected) 0.35f else 0.12f)
                                .tactilePress()
                                .clickable { selectedCollectionId = col.id }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = if (isSelected) (if (isDarkThemeGlobal) ChampagneGold else DewSage) else TextLichenMuted,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = col.name,
                                    style = BadgeCaps.copy(fontSize = 10.sp),
                                    color = if (isSelected) (if (isDarkThemeGlobal) TextChampagne else DewSage) else TextDewSage
                                )
                            }
                        }
                    }

                    // Add Collection Chip
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(9999.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(9999.dp), topAlpha = 0.12f)
                            .tactilePress()
                            .clickable { onCreateCollectionRequested(urlInput, noteInput) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = TextChampagne,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "New",
                                style = BadgeCaps.copy(fontSize = 10.sp),
                                color = TextChampagne
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Optional Note
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "REFLECTION NOTE (OPTIONAL)",
                    style = BadgeCaps.copy(fontSize = 9.sp),
                    color = TextLichenMuted
                )

                BasicTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    singleLine = false,
                    maxLines = 3,
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = TextParchment,
                        lineHeight = 18.sp
                    ),
                    cursorBrush = SolidColor(ChampagneGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(12.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                        .padding(12.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (noteInput.isEmpty()) {
                                Text(
                                    text = "Why is this worth revisiting later?",
                                    style = BodyMedium.copy(fontSize = 12.sp),
                                    color = TextLichenMuted
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary 1-Tap Save Action Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(
                        color = if (isValidUrl && !isSaving) ChampagneGold else SurfaceElevated,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .tactilePress()
                    .clickable(enabled = isValidUrl && !isSaving) {
                        if (isValidUrl) {
                            onSave(
                                urlInput.trim(),
                                selectedCollectionId,
                                noteInput.trim().ifBlank { null }
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color(0xFF141D16),
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isValidUrl) "Save to Sanctuary" else "Enter a Valid Link",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = if (isValidUrl) Color(0xFF141D16) else TextLichenMuted
                        )
                        if (isValidUrl) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF141D16),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer Local Privacy Note
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextDewSage,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Encrypted in local device vault • 100% offline",
                    style = BodyMedium.copy(fontSize = 11.sp),
                    color = TextDewSage
                )
            }
        }
    }
}
