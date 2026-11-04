package com.rewindd.app.feature.library.components.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.rewindd.app.core.designsystem.theme.CanvasNoir
import com.rewindd.app.core.designsystem.theme.ChampagneGold
import com.rewindd.app.core.designsystem.theme.CypressGreen
import com.rewindd.app.core.designsystem.theme.DewSage
import com.rewindd.app.core.designsystem.theme.EditorialSerif
import com.rewindd.app.core.designsystem.theme.RoseSignal
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

@Composable
fun SmartMediaPreview(
    spec: MediaPresentationSpec,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(18.dp)

    when (spec.presentationMode) {
        PresentationMode.COMPACT_SQUARE -> {
            Box(
                modifier = modifier
                    .size(92.dp)
                    .ambientGlow(offsetY = 3.dp, radius = 12.dp)
                    .background(SurfaceTray, shape)
                    .frostedRimHighlight(shape, topAlpha = 0.22f)
                    .clip(shape)
                    .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            ) {
                if (!spec.primaryImageUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = spec.primaryImageUrl,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            VitrineFallbackTile(spec = spec, isCompact = true)
                        },
                        error = {
                            VitrineFallbackTile(spec = spec, isCompact = true)
                        }
                    )
                    // Micro-Tonal Warmth Pass
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ChampagneGold.copy(alpha = 0.04f))
                    )
                } else {
                    VitrineFallbackTile(spec = spec, isCompact = true)
                }
            }
        }

        PresentationMode.HERO_VIDEO_LANDSCAPE -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .ambientGlow(
                        color = WarmAmber.copy(alpha = 0.12f),
                        offsetY = 4.dp,
                        radius = 18.dp
                    )
                    .background(SurfaceTray, shape)
                    .frostedRimHighlight(shape, topAlpha = 0.25f)
                    .clip(shape)
                    .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            ) {
                RenderHeroVideoContent(spec = spec, contentDescription = contentDescription)
            }
        }

        PresentationMode.HERO_VIDEO_PORTRAIT -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f, matchHeightConstraintsFirst = false)
                    .heightIn(max = 280.dp)
                    .ambientGlow(
                        color = WarmAmber.copy(alpha = 0.12f),
                        offsetY = 4.dp,
                        radius = 18.dp
                    )
                    .background(SurfaceTray, shape)
                    .frostedRimHighlight(shape, topAlpha = 0.25f)
                    .clip(shape)
                    .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            ) {
                RenderHeroVideoContent(spec = spec, contentDescription = contentDescription)
            }
        }

        PresentationMode.HERO_VIDEO_SQUARE, PresentationMode.FULL_SQUARE_COVER -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .heightIn(max = 280.dp)
                    .ambientGlow(offsetY = 4.dp, radius = 18.dp)
                    .background(SurfaceTray, shape)
                    .frostedRimHighlight(shape, topAlpha = 0.25f)
                    .clip(shape)
                    .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            ) {
                if (!spec.primaryImageUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = spec.primaryImageUrl,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            VitrineFallbackTile(spec = spec, isCompact = false)
                        },
                        error = {
                            VitrineFallbackTile(spec = spec, isCompact = false)
                        }
                    )
                    // Deep Gradient Scrim & Micro-Tonal Warmth
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        CanvasNoir.copy(alpha = 0.65f)
                                    )
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ChampagneGold.copy(alpha = 0.04f))
                    )
                } else {
                    VitrineFallbackTile(spec = spec, isCompact = false)
                }

                if (spec.badgeLabel != null) {
                    EditorialBadge(
                        label = spec.badgeLabel,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.RenderHeroVideoContent(
    spec: MediaPresentationSpec,
    contentDescription: String?
) {
    if (!spec.primaryImageUrl.isNullOrBlank()) {
        SubcomposeAsyncImage(
            model = spec.primaryImageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                VitrineFallbackTile(spec = spec, isCompact = false)
            },
            error = {
                VitrineFallbackTile(spec = spec, isCompact = false)
            }
        )

        // Deep Gradient Scrim (Lower 45%)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            CanvasNoir.copy(alpha = 0.70f)
                        )
                    )
                )
        )

        // Micro-Tonal Warmth Pass
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ChampagneGold.copy(alpha = 0.04f))
        )
    } else {
        VitrineFallbackTile(spec = spec, isCompact = false)
    }

    // Play button center overlay in soft frosted obsidian glass
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .size(48.dp)
            .background(Color(0xCC111813), CircleShape)
            .frostedRimHighlight(CircleShape, topAlpha = 0.40f)
            .ambientGlow(color = Color.Black.copy(alpha = 0.4f), offsetY = 2.dp, radius = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play Video",
            tint = Color(0xFFF4EFEA),
            modifier = Modifier.size(26.dp)
        )
    }

    // Bottom Bar (Badge & Source Domain)
    Row(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (spec.badgeLabel != null) {
            EditorialBadge(
                label = spec.badgeLabel
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!spec.sourceDomain.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .background(CanvasNoir.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                    .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.30f)
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = spec.sourceDomain,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextChampagne
                )
            }
        }
    }
}

@Composable
fun VitrineFallbackTile(
    spec: MediaPresentationSpec,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) {
    val tileBackground = when (spec.fallbackCategory) {
        FallbackCategory.VIDEO -> Color(0xFF162019)
        FallbackCategory.ARTICLE -> SurfaceElevated
        FallbackCategory.PAPER -> Color(0xFF1E2822)
        FallbackCategory.NOTE -> Color(0xFF222C25)
        FallbackCategory.WEBSITE -> SurfaceTray
    }

    val iconEmoji = when (spec.fallbackCategory) {
        FallbackCategory.VIDEO -> "▶"
        FallbackCategory.ARTICLE -> "📄"
        FallbackCategory.PAPER -> "📜"
        FallbackCategory.NOTE -> "📝"
        FallbackCategory.WEBSITE -> "🌐"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tileBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(6.dp)
        ) {
            if (!spec.faviconUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = spec.faviconUrl,
                    contentDescription = spec.sourceDomain,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(if (isCompact) 28.dp else 38.dp)
                        .clip(CircleShape)
                        .background(SurfaceElevated, CircleShape)
                        .frostedRimHighlight(CircleShape, topAlpha = 0.30f)
                        .padding(4.dp),
                    error = {
                        Text(
                            text = iconEmoji,
                            fontSize = if (isCompact) 20.sp else 30.sp
                        )
                    }
                )
            } else {
                Text(
                    text = iconEmoji,
                    fontSize = if (isCompact) 22.sp else 32.sp
                )
            }

            if (!isCompact && !spec.sourceDomain.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = spec.sourceDomain,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextChampagne,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EditorialBadge(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CanvasNoir.copy(alpha = 0.85f)
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .frostedRimHighlight(RoundedCornerShape(6.dp), topAlpha = 0.30f)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = DewSage
        )
    }
}

// Backward compatibility aliases
@Composable
fun NeoBrutalistFallbackTile(
    spec: MediaPresentationSpec,
    isCompact: Boolean,
    modifier: Modifier = Modifier
) = VitrineFallbackTile(spec = spec, isCompact = isCompact, modifier = modifier)

@Composable
fun NeoBrutalistBadge(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ChampagneGold
) = EditorialBadge(label = label, modifier = modifier, backgroundColor = CanvasNoir.copy(alpha = 0.85f))
