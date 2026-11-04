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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
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
import com.rewindd.app.core.designsystem.theme.IconBotanical
import com.rewindd.app.core.designsystem.theme.IconGold
import com.rewindd.app.core.designsystem.theme.IconNeutral
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

/**
 * Screen 43 — Privacy Center Screen
 * 100% Local-first sanctuary privacy audit & guarantees:
 * - Local-First SQLite Database
 * - Zero Device or App Surveillance
 * - Zero Background Scraping
 * - Zero Ad Brokers or Telemetry SDKs
 */
@Composable
fun PrivacyCenterScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasNotificationPermission by remember {
        mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled())
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
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
                                tint = IconBotanical,
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
                                tint = IconBotanical,
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
                        text = "Rewindd is built with a strict local-first privacy contract. No accounts, no cloud database, no trackers, and zero background app surveillance.",
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
                                tint = IconNeutral,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "Encrypted On-Device Realm",
                                fontSize = 11.5.sp,
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
                                color = IconBotanical
                            )
                        }
                    }
                }
            }

            // LIVE PRIVACY & INTEGRATION AUDIT
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "LIVE SYSTEM AUDIT",
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
                        // Audit Item 1: Local SQLite Storage
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
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = IconGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "On-Device Storage",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextParchment
                                    )
                                    Text(
                                        text = "Private SQLite database inside app sandbox",
                                        fontSize = 11.5.sp,
                                        color = TextDewSage
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "SECURED",
                                    style = BadgeCaps.copy(fontSize = 10.sp),
                                    color = IconBotanical
                                )
                            }
                        }

                        // Audit Item 2: Android Share Sheet
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
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = IconBotanical,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "1-Tap Share Receiver",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextParchment
                                    )
                                    Text(
                                        text = "Captures links on demand without background access",
                                        fontSize = 11.5.sp,
                                        color = TextDewSage
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(SurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = BadgeCaps.copy(fontSize = 10.sp),
                                    color = IconBotanical
                                )
                            }
                        }

                        // Audit Item 3: Mindful Reminders (Notifications)
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
                                        tint = if (hasNotificationPermission) IconBotanical else IconNeutral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "Rewind Notifications",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextParchment
                                    )
                                    Text(
                                        text = if (hasNotificationPermission) "Daily review reminders enabled" else "Optional reminders muted in system settings",
                                        fontSize = 11.5.sp,
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
                                    color = if (hasNotificationPermission) IconBotanical else TextChampagne
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
                        color = IconBotanical
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
                                tint = IconGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "1. 100% Local SQLite Storage",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "All saved bookmarks, personal notes, tags, and collections live exclusively on your physical phone. No cloud database or sync servers.",
                        style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
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
                            style = BadgeCaps.copy(fontSize = 8.5.sp),
                            color = IconBotanical
                        )
                    }
                }
            }

            // Pillar 2: Zero App Surveillance
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
                                Icons.Default.Shield,
                                contentDescription = null,
                                tint = IconBotanical,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            "2. Zero App or Screen Surveillance",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "Rewindd never monitors what other apps you use, your screen time, or your usage habits. You only save what you intentionally share.",
                        style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "ZERO APP MONITORING",
                            style = BadgeCaps.copy(fontSize = 8.5.sp),
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
                                tint = IconBotanical,
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
                        "Saving content happens only via the native Android Share Sheet or manual paste. Zero accessibility services, zero keyloggers, and zero screen recording.",
                        style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                        color = TextDewSage
                    )
                    Box(
                        modifier = Modifier
                            .background(SurfaceTray, RoundedCornerShape(6.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "ZERO ACCESSIBILITY ACCESS",
                            style = BadgeCaps.copy(fontSize = 8.5.sp),
                            color = IconBotanical
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
                            "4. Zero Ad Brokers & Analytics SDKs",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextParchment
                        )
                    }
                    Text(
                        "No Google Analytics, Firebase, Facebook SDK, or ad tracking beacons. Your content remains completely private on your physical device.",
                        style = BodyMedium.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
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
                            style = BadgeCaps.copy(fontSize = 8.5.sp),
                            color = IconBotanical
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
