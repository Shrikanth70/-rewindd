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
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.LabelSmall
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.RoseSignal
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    isDarkMode: Boolean = false,
    onToggleDarkMode: (Boolean) -> Unit = {},
    onReplayOnboarding: () -> Unit = {},
    onExportData: () -> Unit = {},
    onDeleteAllData: () -> Unit = {},
    onOpenPrivacyCenter: () -> Unit = {},
    onOpenAboutSupport: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(CanvasNoir)
                .drawBehind {
                    drawLine(
                        color = OutlineVariant.copy(alpha = 0.5f),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
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
                            .size(38.dp)
                            .ambientGlow(offsetY = 2.dp, radius = 8.dp)
                            .background(color = SurfaceElevated, shape = RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.25f)
                            .tactilePress()
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextChampagne,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Settings",
                            style = HeadlineSmall.copy(fontSize = 18.sp, lineHeight = 20.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = TextParchment
                        )
                        Text(
                            text = "PREFERENCES & SANCTUARY",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextLichenMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(color = SurfaceElevated, shape = CircleShape)
                        .frostedRimHighlight(shape = CircleShape, topAlpha = 0.20f)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(DewSage, CircleShape)
                        )
                        Text(
                            text = "v2.4.0",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextDewSage
                        )
                    }
                }
            }
        }

        // Scrollable Settings
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // SECTION: APPEARANCE & THEME
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "APPEARANCE & THEME",
                    style = BadgeCaps,
                    color = TextLichenMuted
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                        .frostedRimHighlight(RoundedCornerShape(20.dp), topAlpha = 0.25f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.22f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = ChampagneGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Dark Theme",
                                    style = TitleMedium.copy(fontSize = 15.sp),
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextParchment
                                )
                                Text(
                                    text = if (isDarkMode) "Nocturnal botanical noir" else "Bright ivory porcelain (Default)",
                                    style = LabelSmall,
                                    color = TextDewSage
                                )
                            }
                        }

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onToggleDarkMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ChampagneGold,
                                checkedTrackColor = CypressGreen,
                                uncheckedThumbColor = TextDewSage,
                                uncheckedTrackColor = SurfaceElevated
                            )
                        )
                    }
                }
            }

            // SECTION 1: APP & TOUR
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "APP & TOUR",
                    style = BadgeCaps,
                    color = TextLichenMuted
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                        .frostedRimHighlight(RoundedCornerShape(20.dp), topAlpha = 0.25f)
                ) {
                    Column {
                        // Replay Tour
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tactilePress()
                                .clickable { onReplayOnboarding() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.22f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.RestartAlt, null, tint = DewSage, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Replay Onboarding Tour", style = TitleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                                    Text("Inspect welcome & sanctuary tutorial steps", style = LabelSmall, color = TextDewSage)
                                }
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = TextLichenMuted)
                        }
                    }
                }
            }

            // SECTION 3: DATA & PRIVACY
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "DATA & PRIVACY",
                    style = BadgeCaps,
                    color = TextLichenMuted
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                        .frostedRimHighlight(RoundedCornerShape(20.dp), topAlpha = 0.25f)
                ) {
                    Column {
                        // Privacy Center
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tactilePress()
                                .clickable { onOpenPrivacyCenter() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.22f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.VerifiedUser, null, tint = DewSage, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Privacy Center", style = TitleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                                    Text("Enforced zero-telemetry boundary", style = LabelSmall, color = TextDewSage)
                                }
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = TextLichenMuted)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(OutlineVariant.copy(alpha = 0.4f))
                        )

                        // Export Data
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tactilePress()
                                .clickable { onExportData() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.22f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.Download, null, tint = ChampagneGold, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Export Data (JSON)", style = TitleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                                    Text("Save offline backup of all links & tags", style = LabelSmall, color = TextDewSage)
                                }
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = TextLichenMuted)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(OutlineVariant.copy(alpha = 0.4f))
                        )

                        // Danger Zone: Delete
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tactilePress()
                                .clickable { onDeleteAllData() }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(RoseSignal.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.30f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.DeleteForever, null, tint = RoseSignal, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Delete All Local Data", style = TitleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.SemiBold, color = RoseSignal)
                                    Text("Permanently wipe local SQLite database", style = LabelSmall, color = TextDewSage)
                                }
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = RoseSignal)
                        }
                    }
                }
            }

            // SECTION 4: ABOUT & SUPPORT
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ABOUT & SUPPORT",
                    style = BadgeCaps,
                    color = TextLichenMuted
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                        .frostedRimHighlight(RoundedCornerShape(20.dp), topAlpha = 0.25f)
                        .tactilePress()
                        .clickable { onOpenAboutSupport() }
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
                                    .size(36.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                                    .frostedRimHighlight(RoundedCornerShape(10.dp), topAlpha = 0.22f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.HelpCenter, null, tint = TextChampagne, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("About Rewindd", style = TitleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                                Text("Manifesto, licenses & stable build info", style = LabelSmall, color = TextDewSage)
                            }
                        }

                        Icon(Icons.Filled.ChevronRight, null, tint = TextLichenMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
