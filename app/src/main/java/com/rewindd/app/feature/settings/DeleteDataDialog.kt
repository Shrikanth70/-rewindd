package com.rewindd.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.HeadlineSmall
import com.rewindd.app.core.designsystem.theme.OutlineVariant
import com.rewindd.app.core.designsystem.theme.RoseSignal
import com.rewindd.app.core.designsystem.theme.SurfaceElevated
import com.rewindd.app.core.designsystem.theme.SurfaceTray
import com.rewindd.app.core.designsystem.theme.SurfaceVitrine
import com.rewindd.app.core.designsystem.theme.TextDewSage
import com.rewindd.app.core.designsystem.theme.TextLichenMuted
import com.rewindd.app.core.designsystem.theme.TextParchment
import com.rewindd.app.core.designsystem.theme.TitleMedium
import com.rewindd.app.core.designsystem.theme.ambientGlow
import com.rewindd.app.core.designsystem.theme.frostedRimHighlight
import com.rewindd.app.core.designsystem.theme.tactilePress

/**
 * Screen 42 — Delete All Local Data Modal Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDataDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationInput by remember { mutableStateOf("") }
    val isConfirmed = confirmationInput.trim().equals("DELETE", ignoreCase = true)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .ambientGlow(offsetY = 8.dp, radius = 28.dp)
                .background(SurfaceVitrine, RoundedCornerShape(24.dp))
                .frostedRimHighlight(RoundedCornerShape(24.dp), topAlpha = 0.28f)
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(RoseSignal.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .frostedRimHighlight(RoundedCornerShape(12.dp), topAlpha = 0.30f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, tint = RoseSignal, modifier = Modifier.size(22.dp))
                    }

                    Column {
                        Box(
                            modifier = Modifier
                                .background(RoseSignal.copy(alpha = 0.20f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text("PERMANENT ACTION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RoseSignal)
                        }
                        Text("Reset & Purge", style = HeadlineSmall.copy(fontSize = 16.sp), fontWeight = FontWeight.SemiBold, color = TextParchment)
                    }
                }

                Text(
                    text = "Delete All Local Data?",
                    style = HeadlineSmall.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = TextParchment
                )

                Text(
                    text = "This will permanently wipe your offline SQLite vault, all saved URLs, custom tags, reading progress, and tracked screen-time telemetry. This action CANNOT be undone.",
                    fontSize = 13.sp,
                    color = TextDewSage,
                    lineHeight = 18.sp
                )

                // Confirmation input box
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Type DELETE to confirm:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextParchment
                    )

                    OutlinedTextField(
                        value = confirmationInput,
                        onValueChange = { confirmationInput = it },
                        placeholder = { Text("DELETE", color = TextLichenMuted, fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceTray, RoundedCornerShape(14.dp)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoseSignal,
                            unfocusedBorderColor = OutlineVariant,
                            focusedTextColor = TextParchment,
                            unfocusedTextColor = TextParchment
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                // Action Buttons
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onConfirmDelete,
                        enabled = isConfirmed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoseSignal,
                            contentColor = CanvasNoir,
                            disabledContainerColor = SurfaceElevated,
                            disabledContentColor = TextLichenMuted
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .tactilePress()
                    ) {
                        Text("Permanently Delete All Data", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(SurfaceElevated, RoundedCornerShape(14.dp))
                            .frostedRimHighlight(RoundedCornerShape(14.dp), topAlpha = 0.20f)
                            .tactilePress()
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cancel & Keep Data", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextParchment)
                    }
                }
            }
        }
    }
}
