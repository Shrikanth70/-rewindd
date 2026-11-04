package com.rewindd.app.feature.rewind.components

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.BadgeCaps
import com.rewindd.app.core.designsystem.theme.BodyMedium
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextChampagne
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.WarmAmber
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Strict URL Scheme Validation and fallback resolver.
 */
fun safeOpenUrl(
    context: Context,
    url: String,
    onFallbackRequired: () -> Unit
) {
    try {
        val parsedUri = Uri.parse(url)
        val scheme = parsedUri.scheme?.lowercase()
        if (scheme != "http" && scheme != "https") {
            onFallbackRequired()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, parsedUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        onFallbackRequired()
    } catch (_: SecurityException) {
        onFallbackRequired()
    } catch (_: Exception) {
        onFallbackRequired()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenOriginalFallbackDialog(
    url: String,
    onOpenInBrowser: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 6.dp, radius = 24.dp)
                .background(SurfaceVitrine, RoundedCornerShape(20.dp))
                .frostedRimHighlight(shape = RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(SurfaceElevated, RoundedCornerShape(10.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = WarmAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Box(
                            modifier = Modifier
                                .background(SurfaceElevated, RoundedCornerShape(4.dp))
                                .frostedRimHighlight(shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "APP NOT INSTALLED",
                                style = BadgeCaps.copy(fontSize = 9.sp),
                                color = TextChampagne
                            )
                        }
                        Text(
                            text = "Native app unavailable",
                            style = BodyMedium.copy(fontSize = 11.sp),
                            color = TextDewSage
                        )
                    }
                }

                Text(
                    text = "Cannot open external application",
                    style = HeadlineSmall.copy(fontSize = 18.sp),
                    fontFamily = EditorialSerif,
                    color = TextParchment
                )

                Text(
                    text = "The app needed to view this record is not installed on your device. Choose how you would like to proceed:",
                    style = BodyMedium.copy(fontSize = 12.sp, lineHeight = 17.sp),
                    color = TextDewSage
                )

                // URL Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceTray, RoundedCornerShape(10.dp))
                        .frostedRimHighlight(shape = RoundedCornerShape(10.dp), topAlpha = 0.12f)
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bookmark, contentDescription = null, tint = TextChampagne, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("TARGET RECORD", style = BadgeCaps.copy(fontSize = 8.5.sp), color = TextDewSage)
                            }
                            Text("QUEUED IN REWIND", style = BadgeCaps.copy(fontSize = 8.5.sp), color = DewSage)
                        }
                        Text(
                            text = url,
                            style = BodyMedium.copy(fontSize = 11.sp),
                            color = TextParchment,
                            maxLines = 1
                        )
                    }
                }

                // Resolution paths
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Option 1: Browser
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .ambientGlow(offsetY = 2.dp, radius = 10.dp)
                            .background(SurfaceElevated, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp))
                            .tactilePress()
                            .clickable(onClick = onOpenInBrowser)
                            .padding(12.dp)
                    ) {
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
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, tint = TextChampagne, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Open in Default Browser", style = BadgeCaps.copy(fontSize = 11.sp), color = TextChampagne)
                                Text("Launches system web browser", style = BodyMedium.copy(fontSize = 11.sp), color = TextDewSage)
                            }
                        }
                    }

                    // Option 2: Copy Link
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(12.dp))
                            .frostedRimHighlight(shape = RoundedCornerShape(12.dp), topAlpha = 0.12f)
                            .tactilePress()
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("URL", url)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                            .padding(12.dp)
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
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextChampagne, modifier = Modifier.size(16.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Copy Link to Clipboard", style = BadgeCaps.copy(fontSize = 11.sp), color = TextParchment)
                                Text("Ready to paste into external tools", style = BodyMedium.copy(fontSize = 11.sp), color = TextDewSage)
                            }
                        }
                    }

                    // Option 3: Dismiss
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .tactilePress()
                            .clickable(onClick = onDismiss)
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Stay in Rewind Deck",
                            style = BadgeCaps.copy(fontSize = 10.sp),
                            color = TextLichenMuted
                        )
                    }
                }
            }
        }
    }
}
