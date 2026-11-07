package com.rewindd.app.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
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
import com.rewindd.app.feature.library.components.getColorFromToken

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FiltersBottomSheet(
    viewModel: LibraryViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val collections by viewModel.allCollections.collectAsState()
    val tags by viewModel.allTags.collectAsState()
    val selectedCollectionId by viewModel.selectedCollectionId.collectAsState()
    val selectedTagId by viewModel.selectedTagId.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceVitrine,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VAULT FILTERS",
                    style = BadgeCaps.copy(fontSize = 13.sp, letterSpacing = 0.14.sp),
                    color = TextParchment
                )

                Text(
                    text = "Clear All",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = if (isDarkThemeGlobal) TextChampagne else DewSage,
                    modifier = Modifier
                        .clickable { viewModel.clearFilters() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Collections Section
            Text(
                text = "BY COLLECTION",
                style = BadgeCaps.copy(fontSize = 10.sp),
                color = TextLichenMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                collections.forEach { coll ->
                    val isSelected = selectedCollectionId == coll.id
                    val tokenColor = getColorFromToken(coll.colorToken)

                    Box(
                        modifier = Modifier
                            .ambientGlow(offsetY = 2.dp, radius = 6.dp)
                            .background(
                                if (isSelected) {
                                    if (isDarkThemeGlobal) DewSage.copy(alpha = 0.28f) else DewSage.copy(alpha = 0.16f)
                                } else {
                                    SurfaceElevated
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .frostedRimHighlight(
                                RoundedCornerShape(12.dp),
                                topAlpha = if (isSelected) 0.40f else 0.20f
                            )
                            .tactilePress()
                            .clickable {
                                viewModel.selectCollection(if (isSelected) null else coll.id)
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isDarkThemeGlobal) TextChampagne else DewSage,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = coll.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) {
                                    if (isDarkThemeGlobal) TextChampagne else DewSage
                                } else {
                                    TextParchment
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tags Section
            if (tags.isNotEmpty()) {
                Text(
                    text = "BY TAG",
                    style = BadgeCaps.copy(fontSize = 10.sp),
                    color = TextLichenMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        val isSelected = selectedTagId == tag.id

                        Box(
                            modifier = Modifier
                                .ambientGlow(offsetY = 2.dp, radius = 6.dp)
                                .background(
                                    if (isSelected) {
                                        if (isDarkThemeGlobal) DewSage.copy(alpha = 0.28f) else DewSage.copy(alpha = 0.16f)
                                    } else {
                                        SurfaceElevated
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .frostedRimHighlight(
                                    RoundedCornerShape(12.dp),
                                    topAlpha = if (isSelected) 0.40f else 0.20f
                                )
                                .tactilePress()
                                .clickable {
                                    viewModel.selectTag(if (isSelected) null else tag.id)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (isDarkThemeGlobal) TextChampagne else DewSage,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = tag.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) {
                                        if (isDarkThemeGlobal) TextChampagne else DewSage
                                    } else {
                                        TextDewSage
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Apply Button
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChampagneGold,
                    contentColor = Color(0xFF141D16)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 10.dp)
            ) {
                Text(
                    text = "Apply Filters ✓",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF141D16)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
