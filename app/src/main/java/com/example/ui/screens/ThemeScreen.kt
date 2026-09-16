package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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

private data class ThemePreset(
    val name: String,
    val description: String,
    val bgColor: Color,
    val accentColor: Color
)

@Composable
fun ThemeScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.ADVANCED) },
    modifier: Modifier = Modifier
) {
    val selectedTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()
    val selectedTypeface by viewModel.selectedTypeface.collectAsStateWithLifecycle()
    val selectedFontSize by viewModel.selectedFontSize.collectAsStateWithLifecycle()
    val selectedSurfaceStyle by viewModel.selectedSurfaceStyle.collectAsStateWithLifecycle()
    val selectedCorners by viewModel.selectedCorners.collectAsStateWithLifecycle()

    val themes = listOf(
        ThemePreset("Midnight", "The original. Calm near-black blue, one clear accent.", Color(0xFF0D1220), Color(0xFF388BFD)),
        ThemePreset("Obsidian", "Neutral greys, one cold accent. The most restrained option.", Color(0xFF141416), Color(0xFF9CA3AF)),
        ThemePreset("Nocturne", "Deep indigo with a violet accent. Warmer, still quiet.", Color(0xFF0F0E1F), Color(0xFF8B5CF6)),
        ThemePreset("Ember", "Warm carbon and amber. High contrast, reads well at night.", Color(0xFF191310), Color(0xFFF59E0B)),
        ThemePreset("Abyss", "Deep teal and cyan. Cool, clinical, very dark.", Color(0xFF09171C), Color(0xFF06B6D4)),
        ThemePreset("Rosewood", "Warm plum and rose. The softest of the dark themes.", Color(0xFF1B0F18), Color(0xFFEC4899)),
        ThemePreset("Daylight", "Light background. Every screen was designed dark, so expect rough edges.", Color(0xFFF1F5F9), Color(0xFF2563EB))
    )

    val currentThemeObj = themes.find { it.name == selectedTheme } ?: themes[0]

    val typefaces = listOf("Inter", "System", "Serif", "Monospace", "Handwritten")
    val fontSizes = listOf("Compact", "Default", "Large", "Larger")
    val surfaceStyles = listOf("Flat", "Glass", "Soft", "Clay")
    val cornerOptions = listOf("Sharp", "Soft", "Rounded", "Pillowy")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("theme_screen")
    ) {
        MayaScreenHeader(
            title = "Theme",
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
            // 1. LIVE PREVIEW CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(currentThemeObj.bgColor)
                    .border(1.dp, currentThemeObj.accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .testTag("theme_preview_card")
            ) {
                Text(
                    text = "Good evening",
                    color = currentThemeObj.accentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Today",
                    color = if (selectedTheme == "Daylight") Color(0xFF0F172A) else TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Body text sits here. Secondary text has to stay readable on this card.",
                    color = if (selectedTheme == "Daylight") Color(0xFF475569) else TextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = currentThemeObj.accentColor),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Primary",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Caption",
                            color = if (selectedTheme == "Daylight") Color(0xFF334155) else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 2. THEME SELECTION
            Column {
                Text(
                    text = "Theme",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Applies everywhere, right away",
                    color = TextMuted,
                    fontSize = 12.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column {
                        themes.forEachIndexed { index, item ->
                            val isSelected = item.name == selectedTheme
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setTheme(item.name) }
                                    .padding(horizontal = 14.dp, vertical = 13.dp)
                                    .testTag("theme_option_${item.name.lowercase()}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Split pill color sample
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                                        .background(item.bgColor)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .width(12.dp)
                                            .height(24.dp)
                                            .background(item.accentColor)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        color = TextPrimary,
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = item.description,
                                        color = TextMuted,
                                        fontSize = 12.sp,
                                        lineHeight = 15.sp
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MayaElectricBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            if (index < themes.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 50.dp)
                                        .height(0.6.dp)
                                        .background(MayaCardBorder)
                                )
                            }
                        }
                    }
                }
            }

            // 3. TEXT (TYPEFACE & SIZE)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Text",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Typeface and size",
                    color = TextMuted,
                    fontSize = 12.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Typeface chips
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    typefaces.forEach { tf ->
                        val isSelected = tf == selectedTypeface
                        ChipPill(
                            label = tf,
                            isSelected = isSelected,
                            onClick = { viewModel.setTypeface(tf) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bundled. The type the app was designed in.",
                    color = TextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Font size chips
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fontSizes.forEach { sz ->
                        val isSelected = sz == selectedFontSize
                        ChipPill(
                            label = sz,
                            isSelected = isSelected,
                            onClick = { viewModel.setFontSize(sz) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your phone's own font-size setting still applies on top of this.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            // 4. SURFACES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Surfaces",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "How cards and menus are drawn",
                    color = TextMuted,
                    fontSize = 12.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Style chips
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    surfaceStyles.forEach { style ->
                        val isSelected = style == selectedSurfaceStyle
                        ChipPill(
                            label = style,
                            isSelected = isSelected,
                            onClick = { viewModel.setSurfaceStyle(style) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Translucent cards, lit from above, with deeper shadows.",
                    color = TextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Corners chips
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cornerOptions.forEach { crn ->
                        val isSelected = crn == selectedCorners
                        ChipPill(
                            label = crn,
                            isSelected = isSelected,
                            onClick = { viewModel.setCorners(crn) }
                        )
                    }
                }
            }

            // 5. TIP BOX
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131A29))
                    .border(1.dp, Color(0xFF1E2D48), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Tip",
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "The orb has its own colours under Appearance. A theme here repaints the app chrome; the orb keeps whatever palette its style was designed around unless you override it there.",
                    color = TextSecondary,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ChipPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.08f))
            .border(
                1.dp,
                if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 7.dp)
            .testTag("chip_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
