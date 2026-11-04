package com.rewindd.app.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.Surface
import com.rewindd.app.core.designsystem.theme.hardShadow
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
        containerColor = Paper
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VAULT FILTERS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Ink
                )

                Text(
                    text = "Clear All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                    modifier = Modifier
                        .clickable { viewModel.clearFilters() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Collections Section
            Text(
                text = "BY COLLECTION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Ink.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                collections.forEach { coll ->
                    val isSelected = selectedCollectionId == coll.id
                    val tokenColor = getColorFromToken(coll.colorToken)

                    Box(
                        modifier = Modifier
                            .hardShadow(offset = 2.dp, radius = 6.dp)
                            .background(if (isSelected) tokenColor else Surface, RoundedCornerShape(6.dp))
                            .border(2.dp, Ink, RoundedCornerShape(6.dp))
                            .clickable {
                                viewModel.selectCollection(if (isSelected) null else coll.id)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = coll.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tags Section
            if (tags.isNotEmpty()) {
                Text(
                    text = "BY TAG",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        val isSelected = selectedTagId == tag.id

                        Box(
                            modifier = Modifier
                                .hardShadow(offset = 2.dp, radius = 6.dp)
                                .background(if (isSelected) Ink else Surface, RoundedCornerShape(6.dp))
                                .border(2.dp, Ink, RoundedCornerShape(6.dp))
                                .clickable {
                                    viewModel.selectTag(if (isSelected) null else tag.id)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) com.rewindd.app.core.designsystem.theme.CanvasNoir else Ink
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Apply Button
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RewindYellow,
                    contentColor = Ink
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(offset = 3.dp, radius = 8.dp)
                    .border(2.dp, Ink, RoundedCornerShape(8.dp))
            ) {
                Text("Apply Filters ✓", fontWeight = FontWeight.Black, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
