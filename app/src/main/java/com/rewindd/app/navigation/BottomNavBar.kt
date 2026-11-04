package com.rewindd.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.LabelSmall
import com.rewindd.app.core.designsystem.theme.SurfaceGlassDock
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Home", Icons.Filled.CalendarToday),
    REWIND("rewind", "Rewind", Icons.Filled.Style),
    LIBRARY("library", "Library", Icons.Filled.LocalLibrary)
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    // Floating Obsidian Capsule Navigation Dock (Screen-anchored floating container)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isDarkThemeGlobal) CanvasNoir.copy(alpha = 0.5f) else Color.Transparent)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .ambientGlow(
                    color = if (isDarkThemeGlobal) CanvasNoir.copy(alpha = 0.6f) else Color(0x18000000),
                    offsetY = 6.dp,
                    radius = 20.dp
                )
                .background(
                    color = SurfaceGlassDock.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(32.dp)
                )
                .frostedRimHighlight(
                    shape = RoundedCornerShape(32.dp),
                    topAlpha = 0.30f,
                    bottomAlpha = 0.08f
                )
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopLevelDestination.entries.forEach { destination ->
                    val isSelected = currentRoute == destination.route ||
                        (destination == TopLevelDestination.LIBRARY && currentRoute?.startsWith("library") == true)

                    if (isSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .background(
                                    color = DewSage.copy(alpha = if (isDarkThemeGlobal) 0.20f else 0.16f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .frostedRimHighlight(
                                    shape = RoundedCornerShape(24.dp),
                                    topAlpha = 0.35f,
                                    bottomAlpha = 0.10f
                                )
                                .tactilePress()
                                .clickable { onNavigateToDestination(destination) }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = destination.label,
                                style = LabelSmall.copy(fontSize = 13.sp),
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkThemeGlobal) TextChampagne else DewSage
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .tactilePress()
                                .clickable { onNavigateToDestination(destination) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                tint = if (isDarkThemeGlobal) TextLichenMuted else Color(0xFF4E6052),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = destination.label,
                                style = LabelSmall.copy(fontSize = 11.sp),
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkThemeGlobal) TextLichenMuted else Color(0xFF4E6052)
                            )
                        }
                    }
                }
            }
        }
    }
}

