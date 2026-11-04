package com.rewindd.app.feature.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rewindd.app.core.designsystem.theme.Ink
import com.rewindd.app.core.designsystem.theme.LinkBlue
import com.rewindd.app.core.designsystem.theme.Paper
import com.rewindd.app.core.designsystem.theme.RewindYellow
import com.rewindd.app.core.designsystem.theme.Surface
import com.rewindd.app.core.designsystem.theme.hardShadow
import com.rewindd.app.domain.model.SavedItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Stitch Screen 13 — Duplicate Link Bottom Sheet (URL Collision Guard).
 * Modal guard triggered when an attempt is made to vault a duplicate URL.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuplicateLinkBottomSheet(
    existingItem: SavedItem,
    onViewInVault: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val formattedDate = remember(existingItem.capturedAtUtc) {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        sdf.format(Date(existingItem.capturedAtUtc))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Paper,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header: Collision Badge & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(RewindYellow, RoundedCornerShape(4.dp))
                        .border(1.5.dp, Ink, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Ink,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "URL COLLISION GUARD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Ink,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Surface, RoundedCornerShape(8.dp))
                        .border(1.5.dp, Ink, RoundedCornerShape(8.dp))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Ink,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Title & Explanation
            Text(
                text = "Already in Your Vault",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Ink,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "You previously saved this link. To avoid digital clutter, Rewindd maintains a single clean source of truth.",
                fontSize = 13.sp,
                color = Ink.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Existing Saved Item Card Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(offset = 3.dp, radius = 10.dp)
                    .background(Surface, RoundedCornerShape(10.dp))
                    .border(2.dp, Ink, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Ink.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = (existingItem.sourceLabel ?: "SAVED LINK").uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Ink
                            )
                        }

                        Text(
                            text = "Saved $formattedDate",
                            fontSize = 11.sp,
                            color = Ink.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = existingItem.title.ifBlank { existingItem.url },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Ink,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = existingItem.url,
                        fontSize = 12.sp,
                        color = LinkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!existingItem.note.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Paper, RoundedCornerShape(6.dp))
                                .border(1.dp, Ink.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Margin Note: ${existingItem.note}",
                                fontSize = 11.sp,
                                color = Ink.copy(alpha = 0.85f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // View in Vault Button
                Button(
                    onClick = { onViewInVault(existingItem.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RewindYellow,
                        contentColor = Ink
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .hardShadow(offset = 3.dp, radius = 8.dp)
                        .border(2.dp, Ink, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View in Vault",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                // Update Notes Button
                Button(
                    onClick = { onUpdateNotes(existingItem.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Surface,
                        contentColor = Ink
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .hardShadow(offset = 2.dp, radius = 8.dp)
                        .border(2.dp, Ink, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Update Note / Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Dismiss Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Paper,
                        contentColor = Ink
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Ink, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "Keep Existing & Close",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
