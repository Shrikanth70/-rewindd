package com.rewindd.app.feature.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.CanvasNoir
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
import com.rewindd.app.domain.model.Collection

@Composable
fun TaggedCollectionsRail(
    collections: List<Collection>,
    itemCounts: Map<String, Int>,
    selectedCollectionId: String?,
    onCollectionSelected: (String?) -> Unit,
    onManageClick: () -> Unit,
    onAddCollectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TAGGED COLLECTIONS",
                style = BadgeCaps,
                color = TextLichenMuted
            )

            Row(
                modifier = Modifier
                    .clickable(onClick = onManageClick)
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Manage",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextChampagne
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Manage Collections",
                    tint = TextChampagne,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (collections.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(SurfaceVitrine, RoundedCornerShape(14.dp))
                        .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.20f)
                        .tactilePress()
                        .clickable(onClick = onAddCollectionClick)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Create your first collection",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkThemeGlobal) TextChampagne else DewSage
                        )
                    }
                }
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(collections, key = { it.id }) { collection ->
                    val isSelected = selectedCollectionId == collection.id
                    val count = itemCounts[collection.id] ?: 0

                    val bgCardColor = if (isSelected) DewSage.copy(alpha = 0.22f) else SurfaceVitrine

                    Box(
                        modifier = Modifier
                            .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                            .background(bgCardColor, RoundedCornerShape(16.dp))
                            .frostedRimHighlight(
                                shape = RoundedCornerShape(16.dp),
                                topAlpha = if (isSelected) 0.38f else 0.22f
                            )
                            .tactilePress()
                            .clickable {
                                if (isSelected) {
                                    onCollectionSelected(null)
                                } else {
                                    onCollectionSelected(collection.id)
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Squircle icon badge
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                    .frostedRimHighlight(RoundedCornerShape(8.dp), topAlpha = 0.25f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = collection.iconToken,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = collection.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextParchment,
                                    lineHeight = 16.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "$count ${if (count == 1) "item" else "items"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextDewSage
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
