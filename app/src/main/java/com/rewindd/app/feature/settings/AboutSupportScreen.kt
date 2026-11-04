package com.rewindd.app.feature.settings

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.OutlineVariant
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
import com.rewindd.app.core.designsystem.theme.isDarkThemeGlobal
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Screen 44 — About & Support Screen
 * Refactored into Botanical Editorial Noir luxury sanctuary style.
 */
@Composable
fun AboutSupportScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNoir)
    ) {
        // Top Header
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
                        Text(
                            "v2.4.0 • BUILD 248",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = TextChampagne
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "About Rewindd",
                        style = HeadlineSmall.copy(fontSize = 24.sp),
                        fontFamily = EditorialSerif,
                        color = TextParchment
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.HelpCenter,
                        contentDescription = null,
                        tint = TextChampagne,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand Hero Card
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
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(SurfaceTray, RoundedCornerShape(14.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = if (isDarkThemeGlobal) ChampagneGold else WarmAmber,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "LOCAL-FIRST VAULT",
                                    style = BadgeCaps.copy(fontSize = 9.sp),
                                    color = TextChampagne
                                )
                            }
                            Text(
                                "REWINDD",
                                style = HeadlineSmall.copy(fontSize = 22.sp),
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                            Text(
                                "Intentional content curation & mindful screen rhythm.",
                                style = BodyMedium.copy(fontSize = 12.sp),
                                color = TextDewSage
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f, bottomAlpha = 0.03f)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = DewSage,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "Version 2.4.0 (Stable Release)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextDewSage
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Android 15 Ready",
                                    fontSize = 10.sp,
                                    color = TextLichenMuted
                                )
                            }
                        }
                    }
                }
            }

            // Manifesto Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = if (isDarkThemeGlobal) ChampagneGold else DewSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "Why Rewindd exists",
                                style = TitleMedium,
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, CircleShape)
                                .frostedRimHighlight(shape = CircleShape)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                "MANIFESTO",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = TextChampagne
                            )
                        }
                    }

                    Text(
                        text = "Modern algorithms exploit infinite feeds and dopamine loops. Rewindd acts as a physical buffer: a local-first vault where you save what matters, review with spaced intention, and regain sovereignty over your digital time.",
                        style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 20.sp),
                        color = TextDewSage
                    )
                }
            }

            // Support & Open Source Links
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Open Source Licenses
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                        .tactilePress()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Shrikanth70/-rewindd"))
                            context.startActivity(intent)
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Code,
                                    contentDescription = null,
                                    tint = DewSage,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                "Open Source Licenses",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextParchment
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = TextLichenMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Rate & Review
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(16.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(16.dp))
                        .tactilePress()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com"))
                            context.startActivity(intent)
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isDarkThemeGlobal) ChampagneGold else WarmAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                "Rate Rewindd on Play Store",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextParchment
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = TextLichenMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
