package com.rewindd.app.feature.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.TitleMedium
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
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.feature.library.components.getColorFromToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditCollectionSheet(
    onSave: (name: String, colorToken: String, isPinned: Boolean, iconToken: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    existingCollection: Collection? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf(existingCollection?.name ?: "") }
    var selectedColorToken by remember { mutableStateOf(existingCollection?.colorToken ?: "rewind-yellow") }
    var selectedIconToken by remember { mutableStateOf(existingCollection?.iconToken ?: "📁") }
    var isPinned by remember { mutableStateOf(existingCollection?.isPinned ?: false) }

    val presetIcons = listOf(
        "📁", "📚", "💡", "🛠️", "🎨", "🧠",
        "⚡", "🎧", "🎬", "📌", "🚀", "💬",
        "📰", "🔬", "💼", "🌿", "🍿", "🎮",
        "🎵", "⭐", "🔥", "💎", "🌐", "🔖"
    )

    val colorOptions = listOf(
        "rewind-yellow" to ChampagneGold,
        "signal-green" to DewSage,
        "warm-amber" to WarmAmber,
        "cypress" to CypressGreen,
        "lilac" to Color(0xFF9E8DB5)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CanvasNoir,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = if (existingCollection == null) "New Collection" else "Edit Collection",
                style = HeadlineSmall.copy(fontSize = 22.sp),
                fontFamily = EditorialSerif,
                color = TextParchment
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Collection Name Input
            Text("COLLECTION NAME", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
            Spacer(modifier = Modifier.height(6.dp))
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                textStyle = TextStyle(fontSize = 14.sp, color = TextParchment),
                cursorBrush = SolidColor(ChampagneGold),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceTray, RoundedCornerShape(12.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                    .padding(14.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (name.isEmpty()) {
                            Text(
                                text = "e.g. AI & Tech, Weekend Reads...",
                                style = BodyMedium.copy(fontSize = 13.sp),
                                color = TextLichenMuted
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Icon Picker
            Text("CHOOSE ICON", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetIcons.take(6).forEach { icon ->
                    val isSelected = selectedIconToken == icon
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                if (isSelected) SurfaceElevated else SurfaceTray,
                                RoundedCornerShape(10.dp)
                            )
                            .frostedRimHighlight(
                                shape = RoundedCornerShape(10.dp),
                                topAlpha = if (isSelected) 0.35f else 0.10f
                            )
                            .tactilePress()
                            .clickable { selectedIconToken = icon },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = icon, fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Color Palette Selector
            Text("ACCENT COLOR", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                colorOptions.forEach { (token, color) ->
                    val isSelected = selectedColorToken == token
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color, CircleShape)
                            .frostedRimHighlight(shape = CircleShape, topAlpha = if (isSelected) 0.45f else 0.15f)
                            .tactilePress()
                            .clickable { selectedColorToken = token },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(CanvasNoir, CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Live Preview Card
            Text("PREVIEW", style = BadgeCaps.copy(fontSize = 9.5.sp), color = TextLichenMuted)
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(14.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SurfaceElevated, RoundedCornerShape(8.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedIconToken, fontSize = 18.sp)
                    }
                    Text(
                        text = if (name.isBlank()) "Collection Name" else name,
                        style = TitleMedium.copy(fontSize = 15.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Pin to top toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVitrine, RoundedCornerShape(14.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(14.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pin Collection", style = TitleMedium.copy(fontSize = 14.sp), color = TextParchment)
                    Text("Always shows first in the Collections rail", style = BodyMedium.copy(fontSize = 11.5.sp), color = TextDewSage)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            val isEnabled = name.isNotBlank()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(
                        color = if (isEnabled) ChampagneGold else SurfaceElevated,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .tactilePress()
                    .clickable(enabled = isEnabled) {
                        if (isEnabled) {
                            onSave(name.trim(), selectedColorToken, isPinned, selectedIconToken)
                            onDismiss()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save Collection",
                    style = BadgeCaps.copy(fontSize = 12.sp),
                    color = if (isEnabled) Color(0xFF141D16) else TextLichenMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
