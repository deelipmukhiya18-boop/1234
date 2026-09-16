package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@Composable
fun TypingScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.ADVANCED) },
    modifier: Modifier = Modifier
) {
    val humanTypingInEditors by viewModel.humanTypingInEditors.collectAsStateWithLifecycle()
    val typingSpeed by viewModel.typingSpeed.collectAsStateWithLifecycle()
    val typingWhileCoding by viewModel.typingWhileCoding.collectAsStateWithLifecycle()
    val typingApps by viewModel.typingApps.collectAsStateWithLifecycle()

    val speeds = listOf("Slow", "Normal", "Fast")
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("typing_screen")
    ) {
        MayaScreenHeader(
            title = "Typing",
            onBack = onBack,
            viewModel = viewModel
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CARD: REALISTIC TYPING
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Realistic typing",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Type like a human in editors",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Switch 1: Human typing in editors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Human typing in editors",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Notepad, Docs, code — types character by character",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Switch(
                        checked = humanTypingInEditors,
                        onCheckedChange = { viewModel.toggleHumanTyping(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("human_typing_switch")
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Speed chips
                Text(
                    text = "Speed",
                    color = TextSecondary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    speeds.forEach { spd ->
                        val isSelected = spd == typingSpeed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.08f))
                                .border(
                                    1.dp,
                                    if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.setTypingSpeed(spd) }
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                                .testTag("speed_chip_${spd.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = spd,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Switch 2: Realistic typing while coding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Realistic typing while coding",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Applies to coding tasks too",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Switch(
                        checked = typingWhileCoding,
                        onCheckedChange = { viewModel.toggleTypingWhileCoding(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("typing_coding_switch")
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Apps where it's on text field
                Text(
                    text = "Apps where it's on",
                    color = TextSecondary,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = typingApps,
                    onValueChange = { viewModel.updateTypingApps(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("typing_apps_field"),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MayaElectricBlue,
                        unfocusedBorderColor = MayaCardBorder,
                        focusedContainerColor = Color(0xFF0F1420),
                        unfocusedContainerColor = Color(0xFF0F1420)
                    ),
                    minLines = 3,
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Comma-separated package names. Everywhere else stays instant.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
