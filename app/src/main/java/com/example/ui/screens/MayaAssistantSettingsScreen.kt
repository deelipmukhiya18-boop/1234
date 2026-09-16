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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.VoiceOption
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MayaAssistantSettingsScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.SETTINGS) },
    modifier: Modifier = Modifier
) {
    val assistantName by viewModel.assistantName.collectAsStateWithLifecycle()
    val persona by viewModel.assistantPersonaSelection.collectAsStateWithLifecycle()
    val girlfriendMode by viewModel.girlfriendModeEnabled.collectAsStateWithLifecycle()
    val memoryAutoSave by viewModel.memoryAutoSave.collectAsStateWithLifecycle()
    val memoryIncognito by viewModel.memoryIncognito.collectAsStateWithLifecycle()

    val voicePersonaTab by viewModel.selectedVoicePersonaTab.collectAsStateWithLifecycle()
    val selectedVoiceId by viewModel.selectedVoiceId.collectAsStateWithLifecycle()
    val conversationMode by viewModel.conversationModeEnabled.collectAsStateWithLifecycle()
    val messageAlerts by viewModel.messageAlertsEnabled.collectAsStateWithLifecycle()

    val language by viewModel.assistantLanguage.collectAsStateWithLifecycle()
    val autoStartWakeWord by viewModel.autoStartWakeWord.collectAsStateWithLifecycle()
    val proactiveMaya by viewModel.proactiveMayaEnabled.collectAsStateWithLifecycle()
    val callAnnouncement by viewModel.callAnnouncementEnabled.collectAsStateWithLifecycle()
    val keepRingtonePlaying by viewModel.keepRingtonePlaying.collectAsStateWithLifecycle()

    val drivingMode by viewModel.drivingModeEnabled.collectAsStateWithLifecycle()
    val drivingTemplate by viewModel.drivingAutoReplyTemplate.collectAsStateWithLifecycle()
    val drivingStatusMessage by viewModel.drivingStatusMessage.collectAsStateWithLifecycle()

    var drivingInput by remember(drivingTemplate) { mutableStateOf(drivingTemplate) }
    var personaDropdownOpen by remember { mutableStateOf(false) }
    var languageDropdownOpen by remember { mutableStateOf(false) }

    val personaOptions = listOf("Maya", "Friday", "Venom", "Ultron")
    val languageOptions = listOf(
        "Hinglish (Hindi + English) — default",
        "Hindi (हिन्दी)",
        "English (India)",
        "English (US)"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("maya_assistant_settings_screen")
    ) {
        MayaScreenHeader(
            title = "Maya",
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
            // STATUS BANNER (for Driving mode save, etc.)
            if (drivingStatusMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF132238))
                        .border(1.dp, MayaElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = drivingStatusMessage!!,
                            color = MayaElectricBlue,
                            fontSize = 12.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearDrivingStatus() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = TextMuted)
                        }
                    }
                }
            }

            // 1. ASSISTANT NAME (Screenshot 6)
            AssistantCardContainer(
                icon = Icons.Default.Person,
                title = "Assistant name",
                subtitle = "What you call her"
            ) {
                OutlinedTextField(
                    value = assistantName,
                    onValueChange = { viewModel.updateAssistantName(it) },
                    placeholder = { Text("e.g. Maya, Aria, Jarvis...", color = TextMuted, fontSize = 13.5.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("assistant_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MayaElectricBlue,
                        unfocusedBorderColor = MayaCardBorder,
                        focusedContainerColor = Color(0xFF0F1420),
                        unfocusedContainerColor = Color(0xFF0F1420),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }

            // 2. PERSONA (Screenshot 6)
            AssistantCardContainer(
                icon = Icons.Default.Favorite,
                title = "Persona",
                subtitle = "Her overall vibe"
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F1420))
                            .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                            .clickable { personaDropdownOpen = true }
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = persona, color = TextPrimary, fontSize = 14.5.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select", tint = TextMuted)
                    }

                    DropdownMenu(
                        expanded = personaDropdownOpen,
                        onDismissRequest = { personaDropdownOpen = false },
                        modifier = Modifier.background(MayaCardBg)
                    ) {
                        personaOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt, color = TextPrimary) },
                                onClick = {
                                    viewModel.setAssistantPersona(opt)
                                    personaDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // 3. GIRLFRIEND MODE (Screenshot 6)
            AssistantCardContainer(
                icon = Icons.Default.Favorite,
                title = "Girlfriend mode",
                subtitle = "Maya's romantic side"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Enable girlfriend mode",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Maya talks like a close friend — warm and caring, no romance.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = girlfriendMode,
                        onCheckedChange = { viewModel.toggleGirlfriendMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("girlfriend_mode_switch")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                TipBox(text = "Takes effect the next time Maya starts. Can't be switched by voice.")
            }

            // 4. MEMORY (Screenshot 6)
            AssistantCardContainer(
                icon = Icons.Default.Settings,
                title = "Memory",
                subtitle = "What Maya is allowed to remember"
            ) {
                // Let Maya remember on her own
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Let Maya remember on her own",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "She saves durable things she picks up — people, preferences, routine.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = memoryAutoSave,
                        onCheckedChange = { viewModel.toggleMemoryAutoSave(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(MayaCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Incognito
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Incognito",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Turn on to stop all new memories without deleting anything.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = memoryIncognito,
                        onCheckedChange = { viewModel.toggleMemoryIncognito(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                TipBox(text = "Passwords, OTPs, PINs and card numbers are never stored, whatever these are set to.")
            }

            // 5. VOICE (Screenshots 4 & 5)
            AssistantCardContainer(
                icon = Icons.Default.RecordVoiceOver,
                title = "Voice",
                subtitle = "Tap to listen, then pick"
            ) {
                // Segmented tabs: Maya | Friday | Venom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F1420))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Maya", "Friday", "Venom").forEach { tab ->
                        val isSelected = tab == voicePersonaTab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF1E2D48) else Color.Transparent)
                                .clickable { viewModel.setVoicePersonaTab(tab) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 13.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Voice list cards
                val displayedVoices = viewModel.availableVoices.filter { it.persona.equals(voicePersonaTab, ignoreCase = true) }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    displayedVoices.forEach { voice ->
                        val isSelected = voice.id == selectedVoiceId
                        VoiceOptionCard(
                            voice = voice,
                            isSelected = isSelected,
                            onSelect = { viewModel.selectVoice(voice.id) },
                            onPlay = { viewModel.previewVoice(voice) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                TipBox(text = "Applies the next time Maya starts.")
            }

            // 6. CONVERSATION MODE (Screenshot 4)
            AssistantCardContainer(
                icon = Icons.Default.Favorite,
                title = "Conversation mode",
                subtitle = "Talk to her, rather than ask her"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Conversation mode",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Emotion-adaptive voice — slower replies (password needed)",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = conversationMode,
                        onCheckedChange = { viewModel.toggleConversationMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "She hears the mood in your voice and answers in kind. Maya only — Venom and Friday stay in normal mode.\n\nReplies take about 5s instead of 2.4s, and she keeps only her quick tools — everything else still works, one step slower. This can only be switched here, not by voice.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            // 7. MESSAGE ALERTS (Screenshots 3 & 4)
            AssistantCardContainer(
                icon = Icons.Default.Notifications,
                title = "Message alerts",
                subtitle = "Whether she interrupts to tell you about new WhatsApp messages"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tell me about new WhatsApp messages",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Only when you have been quiet for a moment",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = messageAlerts,
                        onCheckedChange = { viewModel.toggleMessageAlerts(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                TipBox(text = "Saying \"mat batao\" to Maya switches this off too, and it stays off. Reactions to messages never interrupt, whatever this is set to.")
            }

            // 8. LANGUAGE (Screenshot 3)
            AssistantCardContainer(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "The language she speaks"
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F1420))
                            .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                            .clickable { languageDropdownOpen = true }
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = language, color = TextPrimary, fontSize = 14.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select", tint = TextMuted)
                    }

                    DropdownMenu(
                        expanded = languageDropdownOpen,
                        onDismissRequest = { languageDropdownOpen = false },
                        modifier = Modifier.background(MayaCardBg)
                    ) {
                        languageOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt, color = TextPrimary) },
                                onClick = {
                                    viewModel.setAssistantLanguage(opt)
                                    languageDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // 9. AUTO START (Screenshot 3)
            AssistantCardContainer(
                icon = Icons.Default.Mic,
                title = "Auto start",
                subtitle = "What the wake word does after you stop Maya"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bring the wake word back after a stop",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Stopping her from the mic button closes the conversation; 5 seconds later \"Hey Maya\" works again.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = autoStartWakeWord,
                        onCheckedChange = { viewModel.toggleAutoStartWakeWord(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                TipBox(text = "The notification's \"Band karo\" always switches everything off, whatever this is set to.")
            }

            // 10. PROACTIVE MAYA (Screenshot 3)
            AssistantCardContainer(
                icon = Icons.Default.Psychology,
                title = "Proactive Maya",
                subtitle = "Let her start conversations on her own"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Start conversations on her own",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Occasional helpful nudges throughout your work day.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = proactiveMaya,
                        onCheckedChange = { viewModel.toggleProactiveMaya(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            // 11. CALL ANNOUNCEMENT (Screenshot 2)
            AssistantCardContainer(
                icon = Icons.Default.Phone,
                title = "Call announcement",
                subtitle = "Maya announces every incoming call and can pick up / reject it"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Announce incoming calls",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Switch(
                        checked = callAnnouncement,
                        onCheckedChange = { viewModel.toggleCallAnnouncement(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(MayaCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Keep my ringtone playing",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Maya turns your ringtone all the way down while she says who is calling. Switch this on to keep hearing it underneath her, at the volume you pick.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Switch(
                        checked = keepRingtonePlaying,
                        onCheckedChange = { viewModel.toggleKeepRingtonePlaying(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            // 12. DRIVING MODE (Screenshot 2)
            AssistantCardContainer(
                icon = Icons.Default.DirectionsCar,
                title = "Driving mode",
                subtitle = "Every call is rejected, the caller gets an SMS, and Maya says who it was"
            ) {
                // Amber License Warning Box
                WarningBox(
                    text = "Driving mode license ke saath aata hai. Activate karte hi calls apne aap reject hone lagengi."
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Toggle reject calls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reject calls while I'm driving",
                            color = TextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Switch(
                        checked = drivingMode,
                        onCheckedChange = { viewModel.toggleDrivingMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MayaElectricBlue,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Auto-reply text field
                Text(
                    text = "Auto-reply sent to the caller",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = drivingInput,
                    onValueChange = { drivingInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("driving_autoreply_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MayaElectricBlue,
                        unfocusedBorderColor = MayaCardBorder,
                        focusedContainerColor = Color(0xFF0F1420),
                        unfocusedContainerColor = Color(0xFF0F1420),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(10.dp))

                TipBox(text = "{name} becomes your name from Personal settings. Leave it empty for the default.")

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.saveDrivingAutoReply(drivingInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("driving_save_button")
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                WarningBox(
                    text = "While this is on your phone answers nobody. It stays on until you turn it off — say \"driving mode band karo\", or use the notification."
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AssistantCardContainer(
    icon: ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF16233B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MayaElectricBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 12.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        content()
    }
}

@Composable
private fun TipBox(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF131A29))
            .border(1.dp, Color(0xFF1E2D48), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = "Tip",
            tint = Color(0xFF38BDF8),
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun WarningBox(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF291F14))
            .border(1.dp, Color(0xFF854D0E), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            tint = Color(0xFFFBBF24),
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = Color(0xFFFDE68A),
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun VoiceOptionCard(
    voice: VoiceOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onPlay: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0F1420))
            .border(
                1.dp,
                if (isSelected) MayaElectricBlue else MayaCardBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = voice.name,
                    color = TextPrimary,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${voice.voiceCode})",
                    color = MayaElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = voice.description,
                color = TextMuted,
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E2D48))
                    .clickable(onClick = onPlay),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play voice preview",
                    tint = MayaElectricBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MayaElectricBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
