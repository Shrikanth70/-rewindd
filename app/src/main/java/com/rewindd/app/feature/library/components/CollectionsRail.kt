package com.rewindd.app.feature.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.HotPink
import com.rewindd.app.core.designsystem.theme.Lilac
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.SignalGreen
import com.rewindd.app.core.designsystem.theme.SoftBlue
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.domain.model.Collection

@Composable
fun CollectionsRail(
    collections: List<Collection>,
    selectedCollectionId: String?,
    onCollectionSelected: (String?) -> Unit,
    onAddCollectionClick: () -> Unit,
    onCollectionDetailClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All Collections" Chip
        val isAllSelected = selectedCollectionId == null
        Box(
            modifier = Modifier
                .background(
                    if (isAllSelected) DewSage.copy(alpha = 0.22f) else SurfaceElevated,
                    RoundedCornerShape(12.dp)
                )
                .frostedRimHighlight(
                    RoundedCornerShape(12.dp),
                    topAlpha = if (isAllSelected) 0.35f else 0.20f
                )
                .tactilePress()
                .clickable { onCollectionSelected(null) }
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = "All Collections",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isAllSelected) TextChampagne else TextDewSage
            )
        }

        // Each Collection Chip
        collections.forEach { collection ->
            val isSelected = selectedCollectionId == collection.id

            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) DewSage.copy(alpha = 0.22f) else SurfaceElevated,
                        RoundedCornerShape(12.dp)
                    )
                    .frostedRimHighlight(
                        RoundedCornerShape(12.dp),
                        topAlpha = if (isSelected) 0.35f else 0.20f
                    )
                    .tactilePress()
                    .clickable { onCollectionSelected(collection.id) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${collection.iconToken} ${if (collection.isPinned) "📌 " else ""}${collection.name}",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) TextChampagne else TextParchment
                    )
                    if (onCollectionDetailClick != null) {
                        Box(
                            modifier = Modifier
                                .clickable { onCollectionDetailClick(collection.id) }
                                .padding(start = 2.dp)
                        ) {
                            Text("⚙", fontSize = 10.sp, color = TextDewSage)
                        }
                    }
                }
            }
        }

        // "+ New Collection" Button
        Box(
            modifier = Modifier
                .background(ChampagneGold, RoundedCornerShape(12.dp))
                .tactilePress()
                .clickable(onClick = onAddCollectionClick)
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = "+ New",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF141D16)
            )
        }
    }
}

fun getColorFromToken(token: String?): Color {
    return when (token?.lowercase()) {
        "rewind-yellow", "yellow" -> RewindYellow
        "hot-pink", "pink" -> HotPink
        "signal-green", "green" -> SignalGreen
        "lilac", "purple" -> Lilac
        "soft-blue", "blue" -> SoftBlue
        else -> RewindYellow
    }
}
