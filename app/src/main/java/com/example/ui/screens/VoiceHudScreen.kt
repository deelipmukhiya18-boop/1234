package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MayaPersona
import com.example.ui.MayaViewModel
import com.example.ui.components.PersonaSelector
import com.example.ui.components.VoiceWaveformOrb
import com.example.ui.theme.MayaBlack
import com.example.ui.theme.MayaBorder
import com.example.ui.theme.MayaCardDark
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.MayaRed
import com.example.ui.theme.MayaRedGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceHudScreen(
    viewModel: MayaViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val persona by viewModel.selectedPersona.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val isDuetActive by viewModel.isDuetActive.collectAsStateWithLifecycle()
    val isSleeping by viewModel.isSleeping.collectAsStateWithLifecycle()
    val lastTranscript by viewModel.lastTranscript.collectAsStateWithLifecycle()
    val lastMayaReply by viewModel.lastMayaReply.collectAsStateWithLifecycle()
    val waveforms by viewModel.waveforms.collectAsStateWithLifecycle()
    val isTorchOn by viewModel.isFlashlightOn.collectAsStateWithLifecycle()

    var manualInput by remember { mutableStateOf("") }

    val quickPrompts = listOf(
        "Ask my phone if I got an OTP.",
        "How much did I spend today?",
        "Call my phone — I left it upstairs.",
        "Open Instagram on my phone.",
        "Run morning briefing.",
        "So jao."
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MayaBlack)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Desktop Connection HUD Status Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("desktop_hud_status_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (session?.isConnected == true && !isSleeping) MayaGreen else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = session?.desktopName ?: "Hunter-Studio (Win 11)",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "P2P Link · ${session?.lastPingMs ?: 12}ms ping · 234 Tools",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.toggleSleep() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("sleep_mode_toggle"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PowerSettingsNew,
                                    contentDescription = "Sleep or Wake",
                                    tint = if (isSleeping) MayaRed else TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isSleeping) "Wake" else "Sleep",
                                    color = if (isSleeping) MayaRed else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Persona Selector
            PersonaSelector(
                selectedPersona = persona,
                onSelectPersona = { viewModel.setPersona(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Central Voice Orb Visualizer
            VoiceWaveformOrb(
                isListening = isListening,
                isSpeaking = isSpeaking,
                isDuetActive = isDuetActive,
                isSleeping = isSleeping,
                persona = persona,
                waveforms = waveforms,
                onOrbClick = { viewModel.toggleVoiceListening() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Live Speech Transcript & Maya Response Cards
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("conversation_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VOICE TRANSCRIPT",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        if (isSpeaking) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speaking",
                                    tint = MayaRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("24 kHz Natural Speech", color = MayaRed, fontSize = 10.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"$lastTranscript\"",
                        color = TextPrimary.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MayaBorder)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "MAYA RESPONSE",
                        color = MayaRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = lastMayaReply,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Hardware Shortcut Actions (Duet, Beacon, Torch)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Duet Call button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.toggleDuetCall() }
                        .testTag("action_duet_call"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDuetActive) MayaRed.copy(alpha = 0.2f) else MayaCardDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDuetActive) MayaRed else MayaBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneInTalk,
                            contentDescription = "Duet Call",
                            tint = if (isDuetActive) MayaRed else TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isDuetActive) "End Duet" else "Duet Call",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Locate Phone
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.triggerFindPhone() }
                        .testTag("action_find_phone"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Find Phone",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Find Phone",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Torch Toggle
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.toggleTorch() }
                        .testTag("action_torch"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTorchOn) MayaRed.copy(alpha = 0.2f) else MayaCardDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isTorchOn) MayaRed else MayaBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = "Flashlight",
                            tint = if (isTorchOn) MayaRed else TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isTorchOn) "Torch On" else "Torch",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hands-Free Quick Voice Prompts
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "VOICE COMMAND SAMPLES (PAGE 5 & 12)",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickPrompts.forEach { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(MayaCardDark)
                                .border(1.dp, MayaBorder, RoundedCornerShape(20.dp))
                                .clickable {
                                    viewModel.submitSpokenPrompt(prompt)
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                .testTag("chip_${prompt.take(10).replace(" ", "_")}")
                        ) {
                            Text(
                                text = prompt,
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Command input bar for manual keyboard typing if preferred
            OutlinedTextField(
                value = manualInput,
                onValueChange = { manualInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("manual_command_input"),
                placeholder = {
                    Text("Type command or tap mic...", color = TextSecondary, fontSize = 13.sp)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (manualInput.isNotBlank()) {
                                viewModel.submitSpokenPrompt(manualInput.trim())
                                manualInput = ""
                            }
                        },
                        modifier = Modifier.testTag("send_command_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Command",
                            tint = MayaRed
                        )
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MayaCardDark,
                    unfocusedContainerColor = MayaCardDark,
                    focusedBorderColor = MayaRed,
                    unfocusedBorderColor = MayaBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
