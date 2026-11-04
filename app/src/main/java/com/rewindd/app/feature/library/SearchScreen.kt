package com.rewindd.app.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.Surface
import com.rewindd.app.core.designsystem.theme.hardShadow
import com.rewindd.app.feature.library.components.VaultItemCard

@Composable
fun SearchScreen(
    viewModel: LibraryViewModel,
    onBack: () -> Unit,
    onNavigateToItemDetail: (String) -> Unit,
    onOpenOriginal: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.setSearchQuery("")
        }
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val results by viewModel.filteredItems.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Paper)
            .imePadding()
            .padding(16.dp)
    ) {
        // Search Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .hardShadow(offset = 2.dp, radius = 8.dp)
                    .background(Surface, RoundedCornerShape(8.dp))
                    .border(2.dp, Ink, RoundedCornerShape(8.dp))
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Ink,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Search TextField
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search title, tags, or notes...", color = Ink.copy(alpha = 0.5f), fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .hardShadow(offset = 2.dp, radius = 8.dp)
                    .background(Surface, RoundedCornerShape(8.dp)),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Ink,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { viewModel.setSearchQuery("") }
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Ink,
                    unfocusedBorderColor = Ink,
                    focusedTextColor = Ink,
                    unfocusedTextColor = Ink
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results Count Header
        if (searchQuery.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SEARCH RESULTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Ink
                )

                Box(
                    modifier = Modifier
                        .background(RewindYellow, RoundedCornerShape(4.dp))
                        .border(1.dp, Ink, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${results.size} FOUND",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Results or Empty State
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .hardShadow(offset = 4.dp, radius = 12.dp)
                    .background(Surface, RoundedCornerShape(12.dp))
                    .border(2.dp, Ink, RoundedCornerShape(12.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (searchQuery.isBlank()) "🔍 Search Your Vault" else "❌ No Results Found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "Type keywords, URLs, margin notes, or #tags to quickly filter your saved items."
                        } else {
                            "No saved links match \"$searchQuery\". Try checking the spelling or searching another tag."
                        },
                        fontSize = 13.sp,
                        color = Ink.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results, key = { it.item.id }) { itemDetails ->
                    VaultItemCard(
                        itemDetails = itemDetails,
                        onItemClick = { onNavigateToItemDetail(itemDetails.item.id) },
                        onOpenOriginal = { onOpenOriginal(itemDetails.item.url) }
                    )
                }
            }
        }
    }
}
