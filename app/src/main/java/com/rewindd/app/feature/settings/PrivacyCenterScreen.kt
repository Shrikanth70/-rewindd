package com.rewindd.app.feature.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
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
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress
import com.rewindd.app.core.permissions.UsageAccessManager
import com.rewindd.app.core.permissions.UsageAccessManagerImpl

/**
 * Screen 43 — Privacy Center Screen with Live Permission Audit
 * Refactored into Botanical Editorial Noir luxury sanctuary style.
 */
@Composable
fun PrivacyCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    usageAccessManager: UsageAccessManager? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val accessManager = remember(context) { usageAccessManager ?: UsageAccessManagerImpl(context) }

    var hasUsagePermission by remember { mutableStateOf(accessManager.hasPermission()) }
    var hasNotificationPermission by remember {
        mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled())
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasUsagePermission = accessManager.hasPermission()
                hasNotificationPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "ZERO TELEMETRY",
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
                        text = "Privacy Center",
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
                            "LOCAL-FIRST",
                            style = BadgeCaps.copy(fontSize = 9.sp),
                            color = TextLichenMuted
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
            // Hero Card: "Your attention, your vault."
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 4.dp, radius = 16.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                "Your attention, your vault.",
                                style = TitleMedium,
                                fontFamily = EditorialSerif,
                                color = TextParchment
                            )
                            Text(
                                "Strict local-first privacy boundary.",
                                style = BodyMedium.copy(fontSize = 12.sp),
                                color = TextDewSage
                            )
                        }
                    }

                    Text(
                        text = "Rewindd was architected with a strict local-first privacy boundary. We have no accounts, no cloud database, no trackers, and no ad brokers.",
                        style = BodyMedium.copy(fontSize = 13.sp, lineHeight = 19.sp),
                        color = TextDewSage
                    )

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
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = TextLichenMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "Encrypted Device Realm",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextDewSage
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                "Offline Verified",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = DewSage
                            )
                        }
                    }
                }
            }

            // LIVE SYSTEM PERMISSION AUDIT
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "LIVE SYSTEM PERMISSION AUDIT",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = TextLichenMuted
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ambientGlow(offsetY = 3.dp, radius = 14.dp)
                        .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Audit Item 1: Usage Access
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SurfaceTray, RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HourglassBottom,
                                        contentDescription = null,
                                        tint = if (hasUsagePermission) DewSage else ChampagneGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "Usage Stats Access",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextParchment
                                    )
                                    Text(
                                        text = if (hasUsagePermission) "Calculates foreground dwell time locally" else "Required to correlate dwell time with links",
                                        fontSize = 11.sp,
                                        color = TextDewSage
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .frostedRimHighlight(
                                        shape = RoundedCornerShape(8.dp),
                                        topAlpha = if (hasUsagePermission) 0.15f else 0.35f
                                    )
                                    .background(
                                        if (hasUsagePermission) SurfaceElevated else ChampagneGold,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .tactilePress()
                                    .clickable {
                                        try {
                                            context.startActivity(accessManager.createSettingsIntent())
                                        } catch (_: Exception) {
                                            // Fallback
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (hasUsagePermission) "ACTIVE" else "GRANT",
                                    style = BadgeCaps.copy(fontSize = 10.sp),
                                    color = if (hasUsagePermission) DewSage else CanvasNoir
                                )
                            }
                        }

                        // Audit Item 2: Notifications
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SurfaceTray, RoundedCornerShape(10.dp))
                                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = if (hasNotificationPermission) DewSage else TextLichenMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "Mindful Reminders",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextParchment
                                    )
                                    Text(
                                        text = if (hasNotificationPermission) "Spaced repetition notifications permitted" else "Notifications muted in system settings",
                                        fontSize = 11.sp,
                                        color = TextDewSage
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .frostedRimHighlight(shape = RoundedCornerShape(8.dp))
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                    .tactilePress()
                                    .clickable {
                                        try {
                                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        } catch (_: Exception) {
                                            val fallback = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.parse("package:${context.packageName}")
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(fallback)
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (hasNotificationPermission) "ACTIVE" else "MANAGE",
                                    style = BadgeCaps.copy(fontSize = 10.sp),
                                    color = if (hasNotificationPermission) DewSage else TextDewSage
                                )
                            }
                        }
                    }
                }
            }

            // Core Privacy Pillars Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "CORE PRIVACY PILLARS",
                    style = BadgeCaps.copy(fontSize = 11.sp),
                    color = TextLichenMuted
                )
                Box(
                    modifier = Modifier
                        .background(SurfaceElevated, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "4 / 4 ENFORCED",
                        style = BadgeCaps.copy(fontSize = 9.sp),
                        color = DewSage
                    )
                }
            }

            // Pillar 1: Local-First Storage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SurfaceTray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Storage,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "1. Local-First Storage (SQLite FTS5)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "All saved links, margin notes, tags, and reading progress are stored exclusively on your device. Zero cloud backups unless you manually export.",
                        style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "STORED ON-DEVICE ONLY",
                            style = BadgeCaps.copy(fontSize = 8.sp),
                            color = DewSage
                        )
                    }
                }
            }

            // Pillar 2: Screen-Time Privacy
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SurfaceTray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "2. Screen-Time Usage Telemetry",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "Measures foreground dwell time via Android UsageStatsManager. Never inspects screen pixels, keystrokes, incognito windows, or notification text.",
                        style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "100% PROCESSED LOCALLY",
                            style = BadgeCaps.copy(fontSize = 8.sp),
                            color = TextChampagne
                        )
                    }
                }
            }

            // Pillar 3: Zero Background Scraping
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SurfaceTray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Layers,
                                contentDescription = null,
                                tint = DewSage,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "3. Zero Background Scraping",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "Saving content relies strictly on Android's native Share Sheet and user-initiated link pasting. Zero background screen recording, zero accessibility service scraping, and zero intrusive overlay popups.",
                        style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "ZERO ACCESSIBILITY SCRAPING",
                            style = BadgeCaps.copy(fontSize = 8.sp),
                            color = TextDewSage
                        )
                    }
                }
            }

            // Pillar 4: Zero SDKs & Tracking
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceVitrine, RoundedCornerShape(18.dp))
                    .frostedRimHighlight(shape = RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SurfaceTray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = RoseSignal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "4. Zero Ad Brokers & Third-Party SDKs",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "No Google Analytics, Firebase Analytics, Facebook SDK, or telemetry beacons. Offline network inspection produces zero outbound packets.",
                        style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "ZERO OUTBOUND TELEMETRY",
                            style = BadgeCaps.copy(fontSize = 8.sp),
                            color = DewSage
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
