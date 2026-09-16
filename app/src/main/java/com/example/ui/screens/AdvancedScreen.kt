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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun AdvancedScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("advanced_screen")
    ) {
        MayaScreenHeader(
            title = "Advanced",
            onBack = onBack,
            viewModel = viewModel
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. LOOK AND FEEL
            SectionBlock(
                title = "LOOK AND FEEL",
                items = listOf(
                    AdvancedItem(
                        icon = Icons.Default.Palette,
                        title = "Theme",
                        subtitle = "Colours, typeface, text size and corners",
                        onClick = { viewModel.setTab(MayaNavTab.THEME) },
                        testTag = "item_theme"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.MotionPhotosOn,
                        title = "Appearance",
                        subtitle = "Orb style, colour and size",
                        onClick = { viewModel.setTab(MayaNavTab.APPEARANCE) },
                        testTag = "item_appearance"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Tune,
                        title = "Behaviour",
                        subtitle = "Floating orb, echo guard, start on boot",
                        onClick = { viewModel.setTab(MayaNavTab.BEHAVIOUR) },
                        testTag = "item_behaviour"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Keyboard,
                        title = "Typing",
                        subtitle = "Human-paced typing in editors",
                        onClick = { viewModel.setTab(MayaNavTab.TYPING) },
                        testTag = "item_typing"
                    )
                )
            )

            // 2. SAFETY AND ACCESS
            SectionBlock(
                title = "SAFETY AND ACCESS",
                items = listOf(
                    AdvancedItem(
                        icon = Icons.Default.RecordVoiceOver,
                        title = "Voice Guardian",
                        subtitle = "Answer only your voice",
                        onClick = { viewModel.setTab(MayaNavTab.VOICE_GUARDIAN) },
                        testTag = "item_voice_guardian"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Sos,
                        title = "Emergency SOS",
                        subtitle = "Contacts, siren and what gets sent",
                        onClick = { viewModel.setTab(MayaNavTab.EMERGENCY_SOS) },
                        testTag = "item_emergency_sos"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.FrontHand,
                        title = "Touch Guard",
                        subtitle = "Watch the phone while you are away from it",
                        onClick = { viewModel.setTab(MayaNavTab.TOUCH_GUARD) },
                        testTag = "item_touch_guard"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Lock,
                        title = "Screen lock",
                        subtitle = "Waking the phone, and unlocking it for you",
                        onClick = { viewModel.setTab(MayaNavTab.SCREEN_LOCK) },
                        testTag = "item_screen_lock"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Security,
                        title = "Permissions",
                        subtitle = "Grant the access Maya's features need",
                        onClick = { viewModel.setTab(MayaNavTab.PERMISSIONS) },
                        testTag = "item_permissions"
                    )
                )
            )

            // 3. SYSTEM
            SectionBlock(
                title = "SYSTEM",
                items = listOf(
                    AdvancedItem(
                        icon = Icons.Default.MarkChatUnread,
                        title = "WhatsApp auto-reply",
                        subtitle = "Answer messages for you while you are away",
                        onClick = { viewModel.setTab(MayaNavTab.WHATSAPP_AUTO_REPLY) },
                        testTag = "item_whatsapp_autoreply"
                    ),
                    AdvancedItem(
                        icon = Icons.Default.Tune,
                        title = "Event triggers",
                        subtitle = "What she announces on her own",
                        onClick = { viewModel.setTab(MayaNavTab.EVENT_TRIGGERS) },
                        testTag = "item_event_triggers"
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private data class AdvancedItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit,
    val testTag: String
)

@Composable
private fun SectionBlock(
    title: String,
    items: List<AdvancedItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MayaCardBg)
                .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = item.onClick)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .testTag(item.testTag),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1E2640)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MayaElectricBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.subtitle,
                                color = TextMuted,
                                fontSize = 12.5.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Open ${item.title}",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (index < items.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 66.dp)
                                .height(0.6.dp)
                                .background(MayaCardBorder)
                        )
                    }
                }
            }
        }
    }
}
