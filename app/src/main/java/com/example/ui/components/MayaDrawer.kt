package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaDarkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MayaDrawerContent(
    viewModel: MayaViewModel,
    onNavigate: (MayaNavTab) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(290.dp)
            .background(MayaDarkSurface)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .testTag("maya_navigation_drawer")
    ) {
        // DRAWER HEADER (Avatar, Maya, by The Hunter AI)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_maya_avatar),
                    contentDescription = "Maya by The Hunter AI",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Maya",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "by The Hunter AI",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // GROUP 1: Primary Productivity Tools
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPillButton(
                icon = Icons.Default.TrendingUp,
                label = "Markets",
                onClick = {
                    viewModel.openQuickTool("Markets")
                    onClose()
                },
                tag = "drawer_item_markets"
            )
            DrawerPillButton(
                icon = Icons.Default.Description,
                label = "Documents",
                onClick = {
                    viewModel.openQuickTool("Documents")
                    onClose()
                },
                tag = "drawer_item_documents"
            )
            DrawerPillButton(
                icon = Icons.Default.Code,
                label = "Website / Coding",
                onClick = {
                    viewModel.openQuickTool("Website / Coding")
                    onClose()
                },
                tag = "drawer_item_coding"
            )
            DrawerPillButton(
                icon = Icons.Default.Edit,
                label = "Study / Whiteboard",
                onClick = {
                    viewModel.openQuickTool("Study / Whiteboard")
                    onClose()
                },
                tag = "drawer_item_study"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // GROUP 2: SYSTEM SECTION
        Text(
            text = "SYSTEM",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPillButton(
                icon = Icons.Default.Security,
                label = "Permissions",
                onClick = {
                    onNavigate(MayaNavTab.PERMISSIONS)
                    onClose()
                },
                tag = "drawer_item_permissions"
            )
            DrawerPillButton(
                icon = Icons.Default.Lock,
                label = "Screen lock",
                onClick = {
                    onNavigate(MayaNavTab.SCREEN_LOCK)
                    onClose()
                },
                tag = "drawer_item_screen_lock"
            )
            DrawerPillButton(
                icon = Icons.Default.FrontHand,
                label = "Touch Guard",
                onClick = {
                    onNavigate(MayaNavTab.TOUCH_GUARD)
                    onClose()
                },
                tag = "drawer_item_touch_guard"
            )
            DrawerPillButton(
                icon = Icons.Default.Sos,
                label = "Emergency SOS",
                onClick = {
                    onNavigate(MayaNavTab.EMERGENCY_SOS)
                    onClose()
                },
                tag = "drawer_item_emergency_sos"
            )
            DrawerPillButton(
                icon = Icons.Default.RecordVoiceOver,
                label = "Voice Guardian",
                onClick = {
                    onNavigate(MayaNavTab.VOICE_GUARDIAN)
                    onClose()
                },
                tag = "drawer_item_voice_guardian"
            )
            DrawerPillButton(
                icon = Icons.Default.Tune,
                label = "Event triggers",
                onClick = {
                    onNavigate(MayaNavTab.EVENT_TRIGGERS)
                    onClose()
                },
                tag = "drawer_item_event_triggers"
            )
            DrawerPillButton(
                icon = Icons.Default.Key,
                label = "Optional (APIs)",
                onClick = {
                    onNavigate(MayaNavTab.OPTIONAL_SETTINGS)
                    onClose()
                },
                tag = "drawer_item_optional"
            )
            DrawerPillButton(
                icon = Icons.Default.MarkChatUnread,
                label = "WhatsApp auto-reply",
                onClick = {
                    onNavigate(MayaNavTab.WHATSAPP_AUTO_REPLY)
                    onClose()
                },
                tag = "drawer_item_whatsapp_reply"
            )
            DrawerPillButton(
                icon = Icons.Default.Rule,
                label = "Maya Rules",
                onClick = {
                    onNavigate(MayaNavTab.MAYA_RULES)
                    onClose()
                },
                tag = "drawer_item_rules"
            )
            DrawerPillButton(
                icon = Icons.Default.SyncAlt,
                label = "PC ⇄ Phone",
                onClick = {
                    onNavigate(MayaNavTab.PHONE_LINK)
                    onClose()
                },
                tag = "drawer_item_pc_phone"
            )
            DrawerPillButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = {
                    onNavigate(MayaNavTab.SETTINGS)
                    onClose()
                },
                tag = "drawer_item_settings"
            )
            DrawerPillButton(
                icon = Icons.Default.Headphones,
                label = "Maya",
                onClick = {
                    onNavigate(MayaNavTab.MAYA_ASSISTANT)
                    onClose()
                },
                tag = "drawer_item_maya_assistant"
            )
            DrawerPillButton(
                icon = Icons.Default.Bolt,
                label = "Skills",
                onClick = {
                    onNavigate(MayaNavTab.SKILLS)
                    onClose()
                },
                tag = "drawer_item_skills"
            )
            DrawerPillButton(
                icon = Icons.Default.Tune,
                label = "Advanced",
                onClick = {
                    onNavigate(MayaNavTab.ADVANCED)
                    onClose()
                },
                tag = "drawer_item_advanced"
            )
            DrawerPillButton(
                icon = Icons.Default.Hub,
                label = "Connectors",
                onClick = {
                    onNavigate(MayaNavTab.CONNECTORS)
                    onClose()
                },
                tag = "drawer_item_connectors"
            )
            DrawerPillButton(
                icon = Icons.Default.Email,
                label = "Email",
                onClick = {
                    onNavigate(MayaNavTab.EMAIL)
                    onClose()
                },
                tag = "drawer_item_email"
            )
            DrawerPillButton(
                icon = Icons.Default.Group,
                label = "Groups & reports",
                onClick = {
                    onNavigate(MayaNavTab.GROUPS_REPORTS)
                    onClose()
                },
                tag = "drawer_item_groups_reports"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // GROUP 3: OTHER SECTION
        Text(
            text = "OTHER",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPillButton(
                icon = Icons.Default.Lock,
                label = "Privacy Policy",
                onClick = {
                    viewModel.openQuickTool("Privacy Policy")
                    onClose()
                },
                tag = "drawer_item_privacy"
            )
            DrawerPillButton(
                icon = Icons.Default.Info,
                label = "About",
                onClick = {
                    viewModel.openQuickTool("About")
                    onClose()
                },
                tag = "drawer_item_about"
            )
            DrawerPillButton(
                icon = Icons.Default.Star,
                label = "Upgrade",
                onClick = {
                    viewModel.setActivateDialog(true)
                    onClose()
                },
                tag = "drawer_item_upgrade"
            )
            DrawerPillButton(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                onClick = {
                    viewModel.openQuickTool("Notifications")
                    onClose()
                },
                tag = "drawer_item_notifications"
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // DRAWER FOOTER (v4.15.1 • The Hunter AI)
        Text(
            text = "v4.15.1 • The Hunter AI",
            color = TextMuted,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .testTag("drawer_version_footer")
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerPillButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(tag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
