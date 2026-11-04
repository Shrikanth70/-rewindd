package com.rewindd.app.feature.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.Surface
import com.rewindd.app.core.designsystem.theme.hardShadow

@Composable
fun VaultTopBar(
    itemCount: Int,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFilterActive: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "VAULT",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Ink,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .background(RewindYellow, RoundedCornerShape(9999.dp))
                    .border(2.dp, Ink, RoundedCornerShape(9999.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$itemCount SAVED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Ink
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Search Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .hardShadow(offset = 2.dp, radius = 8.dp)
                    .background(Surface, RoundedCornerShape(8.dp))
                    .border(2.dp, Ink, RoundedCornerShape(8.dp))
                    .clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Vault",
                    tint = Ink,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Filter Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .hardShadow(offset = 2.dp, radius = 8.dp)
                    .background(if (isFilterActive) RewindYellow else Surface, RoundedCornerShape(8.dp))
                    .border(2.dp, Ink, RoundedCornerShape(8.dp))
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Vault",
                    tint = Ink,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
