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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
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
import com.rewindd.app.feature.library.components.getColorFromToken

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditItemScreen(
    viewModel: ItemDetailViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemDetails by viewModel.itemDetails.collectAsState()
    val collections by viewModel.allCollections.collectAsState()
    val allTags by viewModel.allTags.collectAsState()

    if (itemDetails == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(CanvasNoir),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", style = BodyMedium, color = TextDewSage)
        }
        return
    }

    val item = itemDetails!!.item

    var title by remember(item) { mutableStateOf(item.title) }
    var note by remember(item) { mutableStateOf(item.note ?: "") }
    var selectedCollectionId by remember(item) { mutableStateOf(item.collectionId) }
    var isPinned by remember(item) { mutableStateOf(item.isPinned) }
    val assignedTagIds = remember(itemDetails) {
        mutableStateListOf<String>().apply {
            addAll(itemDetails!!.tags.map { it.id })
        }
    }
    var newTagInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
            .imePadding()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Header
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
                    contentDescription = "Cancel",
                    tint = TextChampagne,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "Edit Record",
                style = TitleMedium.copy(fontSize = 16.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Box(
                modifier = Modifier
                    .background(
                        color = if (title.isNotBlank()) ChampagneGold else SurfaceElevated,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .tactilePress()
                    .clickable(enabled = title.isNotBlank()) {
                        viewModel.saveEdits(
                            title = title,
                            note = note,
                            collectionId = selectedCollectionId,
                            isPinned = isPinned,
                            tagIds = assignedTagIds.toList(),
                            onSaved = onBack
                        )
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Save",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = if (title.isNotBlank()) ContentOnChampagne else TextLichenMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title Field
        Text("TITLE", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
        Spacer(modifier = Modifier.height(6.dp))
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(fontSize = 14.sp, color = TextParchment, lineHeight = 20.sp),
            cursorBrush = SolidColor(ChampagneGold),
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceTray, RoundedCornerShape(12.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                .padding(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Margin Notes Field
        Text("MARGIN NOTES & TAKEAWAYS", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
        Spacer(modifier = Modifier.height(6.dp))
        BasicTextField(
            value = note,
            onValueChange = { note = it },
            textStyle = TextStyle(fontSize = 13.sp, color = TextParchment, lineHeight = 19.sp),
            cursorBrush = SolidColor(ChampagneGold),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(SurfaceTray, RoundedCornerShape(12.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                .padding(14.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (note.isEmpty()) {
                        Text(
                            text = "Add takeaways, quotes, or thoughts...",
                            style = BodyMedium.copy(fontSize = 13.sp),
                            color = TextLichenMuted
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Collection Picker
        Text("COLLECTION", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val isNoneSelected = selectedCollectionId == null
            Box(
                modifier = Modifier
                    .background(
                        color = if (isNoneSelected) SurfaceElevated else SurfaceTray,
                        shape = RoundedCornerShape(9999.dp)
                    )
                    .frostedRimHighlight(shape = RoundedCornerShape(9999.dp), topAlpha = if (isNoneSelected) 0.35f else 0.12f)
                    .tactilePress()
                    .clickable { selectedCollectionId = null }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "None (Inbox)",
                    style = BadgeCaps.copy(fontSize = 10.sp),
                    color = if (isNoneSelected) TextChampagne else TextDewSage
                )
            }

            collections.forEach { coll ->
                val isSelected = selectedCollectionId == coll.id
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) SurfaceElevated else SurfaceTray,
                            shape = RoundedCornerShape(9999.dp)
                        )
                        .frostedRimHighlight(shape = RoundedCornerShape(9999.dp), topAlpha = if (isSelected) 0.35f else 0.12f)
                        .tactilePress()
                        .clickable { selectedCollectionId = coll.id }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = coll.name,
                        style = BadgeCaps.copy(fontSize = 10.sp),
                        color = if (isSelected) TextChampagne else TextDewSage
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tags Section
        Text("TAGS", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
        Spacer(modifier = Modifier.height(8.dp))

        // Assigned Tags FlowRow
        if (assignedTagIds.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                assignedTagIds.forEach { tagId ->
                    val tagObj = allTags.find { it.id == tagId }
                    val tagName = tagObj?.displayName ?: "#tag"

                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(9999.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tagName, style = BadgeCaps.copy(fontSize = 10.sp), color = TextChampagne)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier
                                    .size(13.dp)
                                    .clickable { assignedTagIds.remove(tagId) },
                                tint = TextDewSage
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add Tag Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = newTagInput,
                onValueChange = { newTagInput = it },
                textStyle = TextStyle(fontSize = 13.sp, color = TextParchment),
                cursorBrush = SolidColor(ChampagneGold),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .background(SurfaceTray, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp), topAlpha = 0.12f)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (newTagInput.isEmpty()) {
                            Text(
                                text = "Add tag (e.g. design)...",
                                style = BodyMedium.copy(fontSize = 12.sp),
                                color = TextLichenMuted
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(10.dp))
                    .tactilePress()
                    .clickable {
                        if (newTagInput.isNotBlank()) {
                            viewModel.createAndAddTag(newTagInput) { createdTag ->
                                if (!assignedTagIds.contains(createdTag.id)) {
                                    assignedTagIds.add(createdTag.id)
                                }
                                newTagInput = ""
                            }
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add tag", tint = TextChampagne, modifier = Modifier.size(14.dp))
                    Text("Add", style = BadgeCaps.copy(fontSize = 10.5.sp), color = TextChampagne)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Pinned Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Pin to top of Library", style = TitleMedium.copy(fontSize = 14.sp), color = TextParchment)
                Text("Keep this record easily accessible in your Pinned tab.", style = BodyMedium.copy(fontSize = 11.5.sp), color = TextDewSage)
            }

            Switch(
                checked = isPinned,
                onCheckedChange = { isPinned = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CanvasNoir,
                    checkedTrackColor = ChampagneGold,
                    uncheckedThumbColor = TextLichenMuted,
                    uncheckedTrackColor = SurfaceTray
                )
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Save Changes Bottom Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .ambientGlow(offsetY = 3.dp, radius = 14.dp)
                .background(
                    color = if (title.isNotBlank()) ChampagneGold else SurfaceElevated,
                    shape = RoundedCornerShape(14.dp)
                )
                .tactilePress()
                .clickable(enabled = title.isNotBlank()) {
                    viewModel.saveEdits(
                        title = title,
                        note = note,
                        collectionId = selectedCollectionId,
                        isPinned = isPinned,
                        tagIds = assignedTagIds.toList(),
                        onSaved = onBack
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Save Changes",
                style = BadgeCaps.copy(fontSize = 12.sp),
                color = if (title.isNotBlank()) ContentOnChampagne else TextLichenMuted
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
