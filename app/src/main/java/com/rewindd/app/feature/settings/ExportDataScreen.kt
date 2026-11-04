package com.rewindd.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import com.rewindd.app.core.designsystem.theme.IconBotanical
import com.rewindd.app.core.designsystem.theme.IconGold
import com.rewindd.app.core.designsystem.theme.IconNeutral
import com.rewindd.app.core.designsystem.theme.OutlineVariant
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

enum class ExportFormat {
    JSON,
    MARKDOWN,
    CSV,
    CSV_SCREEN_TIME // Backward-compatible alias
}

/**
 * Screen 41 — Export Data Screen
 * Standalone offline vault exporter with high-contrast botanical styling.
 */
@Composable
fun ExportDataScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onPerformExport: (ExportFormat) -> Unit = {}
) {
    var selectedFormat by remember { mutableIntStateOf(1) } // 1: JSON, 2: Markdown, 3: CSV

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        // Sticky Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CanvasNoir)
                .drawBehind {
                    drawLine(
                        color = OutlineVariant.copy(alpha = 0.5f),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                            .background(SurfaceElevated, RoundedCornerShape(12.dp))
                            .tactilePress()
                            .clickable(onClick = onBack)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextParchment,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "Settings",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = TextParchment
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .frostedRimHighlight(shape = CircleShape, topAlpha = 0.25f, bottomAlpha = 0.05f)
                            .background(SurfaceElevated, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = IconBotanical,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "OFFLINE BACKUP",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = TextChampagne
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Export Data",
                        style = HeadlineSmall.copy(fontSize = 24.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceElevated, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "STANDALONE",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = IconBotanical
                        )
                    }
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Backup Manifest Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(SurfaceTray, RoundedCornerShape(10.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = IconGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Backup Manifest",
                                style = TitleMedium,
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                            Text(
                                "Human-readable standalone files created directly on your device.",
                                style = BodyMedium.copy(fontSize = 12.sp),
                                color = TextDewSage
                            )
                        }
                    }

                    // Manifest List
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(14.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(14.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            listOf(
                                "All saved URLs, article titles & reading state",
                                "Personal margin notes & assigned collection tags",
                                "Curated collections & deck organization",
                                "Spaced repetition review history & queue state"
                            ).forEach { desc ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = IconBotanical,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 12.sp,
                                        color = TextDewSage
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Format Selection Header
            Text(
                "SELECT FORMAT",
                style = BadgeCaps.copy(fontSize = 11.sp),
                color = TextLichenMuted
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Format 1: JSON
                val isJson = selectedFormat == 1
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                        .background(
                            if (isJson) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(16.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(16.dp),
                            topAlpha = if (isJson) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedFormat = 1 }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(SurfaceTray, CircleShape)
                                    .frostedRimHighlight(shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isJson) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(IconGold, CircleShape)
                                    )
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    "Complete JSON Vault (.json)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = if (isJson) TextChampagne else TextParchment
                                )
                                Text(
                                    "Portable schema compatible with all databases",
                                    fontSize = 11.sp,
                                    color = TextDewSage
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                "RECOMMENDED",
                                style = BadgeCaps.copy(fontSize = 8.sp),
                                color = IconGold
                            )
                        }
                    }
                }

                // Format 2: Markdown Archive
                val isMd = selectedFormat == 2
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                        .background(
                            if (isMd) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(16.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(16.dp),
                            topAlpha = if (isMd) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedFormat = 2 }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(SurfaceTray, CircleShape)
                                    .frostedRimHighlight(shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isMd) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(IconGold, CircleShape)
                                    )
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    "Markdown Notes (.md)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = if (isMd) TextChampagne else TextParchment
                                )
                                Text(
                                    "Obsidian, Logseq & Notion compatible notes",
                                    fontSize = 11.sp,
                                    color = TextDewSage
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                "NOTES",
                                style = BadgeCaps.copy(fontSize = 8.sp),
                                color = IconBotanical
                            )
                        }
                    }
                }

                // Format 3: Bookmarks Spreadsheet (.csv)
                val isCsv = selectedFormat == 3
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                        .background(
                            if (isCsv) SurfaceElevated else SurfaceVitrine,
                            RoundedCornerShape(16.dp)
                        )
                        .frostedRimHighlight(
                            shape = RoundedCornerShape(16.dp),
                            topAlpha = if (isCsv) 0.35f else 0.15f
                        )
                        .tactilePress()
                        .clickable { selectedFormat = 3 }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(SurfaceTray, CircleShape)
                                    .frostedRimHighlight(shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCsv) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(IconGold, CircleShape)
                                    )
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    "Bookmarks Spreadsheet (.csv)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = if (isCsv) TextChampagne else TextParchment
                                )
                                Text(
                                    "Tabular export of links, tags, and collections",
                                    fontSize = 11.sp,
                                    color = TextDewSage
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceTray, RoundedCornerShape(6.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                "SPREADSHEET",
                                style = BadgeCaps.copy(fontSize = 8.sp),
                                color = IconNeutral
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Export CTA Button
            Button(
                onClick = {
                    val format = when (selectedFormat) {
                        1 -> ExportFormat.JSON
                        2 -> ExportFormat.MARKDOWN
                        else -> ExportFormat.CSV
                    }
                    onPerformExport(format)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChampagneGold,
                    contentColor = ContentOnChampagne
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .tactilePress()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = ContentOnChampagne,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "Export Vault Now",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = ContentOnChampagne
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
