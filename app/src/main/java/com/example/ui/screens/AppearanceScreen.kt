package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

private data class OrbColorPalette(
    val name: String,
    val color: Color
)

@Composable
fun AppearanceScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.ADVANCED) },
    modifier: Modifier = Modifier
) {
    val selectedOrbStyle by viewModel.selectedOrbStyle.collectAsStateWithLifecycle()
    val selectedOrbColor by viewModel.selectedOrbColor.collectAsStateWithLifecycle()
    val floatingOrbSize by viewModel.floatingOrbSize.collectAsStateWithLifecycle()
    val useOrbOnHome by viewModel.useOrbOnHome.collectAsStateWithLifecycle()

    val colors = listOf(
        OrbColorPalette("Persona", Color(0xFF388BFD)),
        OrbColorPalette("Jarvis", Color(0xFFF59E0B)),
        OrbColorPalette("Ultron", Color(0xFF06B6D4)),
        OrbColorPalette("Neon", Color(0xFFD946EF)),
        OrbColorPalette("Toxic", Color(0xFF22C55E)),
        OrbColorPalette("Teal", Color(0xFF14B8A6))
    )

    val activeColor = colors.find { it.name == selectedOrbColor }?.color ?: Color(0xFF388BFD)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("appearance_screen")
    ) {
        MayaScreenHeader(
            title = "Appearance",
            onBack = onBack,
            viewModel = viewModel
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // MAIN CARD: THE ORB
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "The orb",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "How Maya looks on screen",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Orb style",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3 Orb Style Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OrbStyleCard(
                        title = "MAYA 2047",
                        isSelected = selectedOrbStyle == "MAYA 2047",
                        color = activeColor,
                        styleType = "rings",
                        onClick = { viewModel.setOrbStyle("MAYA 2047") },
                        modifier = Modifier.weight(1f)
                    )
                    OrbStyleCard(
                        title = "Maya Nova",
                        isSelected = selectedOrbStyle == "Maya Nova",
                        color = activeColor,
                        styleType = "nova",
                        onClick = { viewModel.setOrbStyle("Maya Nova") },
                        modifier = Modifier.weight(1f)
                    )
                    OrbStyleCard(
                        title = "J.A.R.V.I.S.",
                        isSelected = selectedOrbStyle == "J.A.R.V.I.S.",
                        color = activeColor,
                        styleType = "jarvis",
                        onClick = { viewModel.setOrbStyle("J.A.R.V.I.S.") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when (selectedOrbStyle) {
                        "MAYA 2047" -> "Concentric pulse rings with cybernetic neon glow"
                        "Maya Nova" -> "Her own neon ring"
                        else -> "Golden spherical wireframe HUD inspired by Stark OS"
                    },
                    color = TextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Orb Colour Section
                Text(
                    text = "Colour",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    colors.forEach { item ->
                        val isSelected = item.name == selectedOrbColor
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { viewModel.setOrbColor(item.name) }
                                .testTag("orb_color_${item.name.lowercase()}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(item.color)
                                    .border(
                                        2.dp,
                                        if (isSelected) Color.White else Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.name,
                                color = if (isSelected) TextPrimary else TextMuted,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Floating orb size slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Floating orb size",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${floatingOrbSize.roundToInt()} dp",
                        color = activeColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = floatingOrbSize,
                    onValueChange = { viewModel.setFloatingOrbSize(it) },
                    valueRange = 120f..260f,
                    steps = 6,
                    colors = SliderDefaults.colors(
                        thumbColor = activeColor,
                        activeTrackColor = activeColor,
                        inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("floating_orb_size_slider")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Switch: Use the orb on Home
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF131A29))
                        .border(1.dp, Color(0xFF1E2D48), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Use the orb on Home",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Replace the character with the orb in your chosen style",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Switch(
                        checked = useOrbOnHome,
                        onCheckedChange = { viewModel.setUseOrbOnHome(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = activeColor,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("use_orb_on_home_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OrbStyleCard(
    title: String,
    isSelected: Boolean,
    color: Color,
    styleType: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Color(0xFF18233C) else Color(0xFF121724))
            .border(
                1.5.dp,
                if (isSelected) color else MayaCardBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
            .testTag("orb_style_${title.lowercase().replace(' ', '_')}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0A0F1A)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(64.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                when (styleType) {
                    "rings" -> {
                        // MAYA 2047: concentric orbital glowing rings
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(color.copy(alpha = 0.4f), Color.Transparent),
                                center = center,
                                radius = size.width * 0.4f * pulseScale
                            ),
                            radius = size.width * 0.45f
                        )
                        drawCircle(
                            color = color,
                            radius = size.width * 0.38f * pulseScale,
                            style = Stroke(width = 2.dp.toPx())
                        )
                        drawCircle(
                            color = color.copy(alpha = 0.7f),
                            radius = size.width * 0.25f,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                        drawCircle(
                            color = color,
                            radius = size.width * 0.12f
                        )
                    }
                    "nova" -> {
                        // Maya Nova: futuristic HUD circle with audio waveforms
                        drawCircle(
                            color = color,
                            radius = size.width * 0.38f,
                            style = Stroke(width = 2.dp.toPx())
                        )
                        drawCircle(
                            color = color.copy(alpha = 0.3f),
                            radius = size.width * 0.28f,
                            style = Stroke(width = 1.dp.toPx())
                        )
                        // central glow
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(color, Color.Transparent),
                                center = center,
                                radius = size.width * 0.25f * pulseScale
                            ),
                            radius = size.width * 0.2f
                        )
                    }
                    else -> {
                        // J.A.R.V.I.S.: spherical wireframe HUD
                        drawCircle(
                            color = Color(0xFFF59E0B),
                            radius = size.width * 0.38f,
                            style = Stroke(width = 2.dp.toPx())
                        )
                        drawLine(
                            color = Color(0xFFF59E0B).copy(alpha = 0.6f),
                            start = Offset(center.x - size.width * 0.38f, center.y),
                            end = Offset(center.x + size.width * 0.38f, center.y),
                            strokeWidth = 1.5.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFFF59E0B).copy(alpha = 0.6f),
                            start = Offset(center.x, center.y - size.width * 0.38f),
                            end = Offset(center.x, center.y + size.width * 0.38f),
                            strokeWidth = 1.5.dp.toPx()
                        )
                        drawCircle(
                            color = Color(0xFFF59E0B).copy(alpha = 0.4f),
                            radius = size.width * 0.22f,
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = if (isSelected) color else TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
