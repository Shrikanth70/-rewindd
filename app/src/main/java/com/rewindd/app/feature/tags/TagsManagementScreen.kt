package com.rewindd.app.feature.tags

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
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

@Composable
fun TagsManagementScreen(
    viewModel: LibraryViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tags by viewModel.allTags.collectAsState()
    var newTagInput by remember { mutableStateOf("") }

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
                text = "Tags Sanctuary",
                style = HeadlineSmall.copy(fontSize = 18.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Box(
                modifier = Modifier
                    .background(SurfaceElevated, RoundedCornerShape(9999.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(9999.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${tags.size} TAGS",
                    style = BadgeCaps.copy(fontSize = 9.sp),
                    color = TextChampagne
                )
            }
        }

        // Create Tag Module
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "CREATE NEW TAG",
                    style = BadgeCaps.copy(fontSize = 9.5.sp),
                    color = TextLichenMuted
                )

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
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (newTagInput.isEmpty()) {
                                    Text(
                                        text = "tag-name...",
                                        style = BodyMedium.copy(fontSize = 13.sp),
                                        color = TextLichenMuted
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                if (newTagInput.isNotBlank()) ChampagneGold else SurfaceElevated,
                                RoundedCornerShape(10.dp)
                            )
                            .tactilePress()
                            .clickable(enabled = newTagInput.isNotBlank()) {
                                viewModel.createTag(newTagInput.trim())
                                newTagInput = ""
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = if (newTagInput.isNotBlank()) ContentOnChampagne else TextLichenMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Create",
                                style = BadgeCaps.copy(fontSize = 11.sp),
                                color = if (newTagInput.isNotBlank()) ContentOnChampagne else TextLichenMuted
                            )
                        }
                    }
                }
            }
        }

        // Tags List
        if (tags.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = TextDewSage,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No tags yet",
                        style = TitleMedium.copy(fontSize = 16.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Text(
                        text = "Create tags above to categorize your reading items.",
                        style = BodyMedium.copy(fontSize = 12.sp),
                        color = TextDewSage,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tags, key = { it.id }) { tag ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceVitrine, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${tag.displayName}",
                            style = BadgeCaps.copy(fontSize = 11.5.sp),
                            color = TextChampagne
                        )

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SurfaceTray, CircleShape)
                                .tactilePress()
                                .clickable { viewModel.deleteTag(tag.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete tag",
                                tint = RoseSignal.copy(alpha = 0.85f),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
