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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.text.style.TextAlign
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
fun SettingsScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("settings_screen")
    ) {
        MayaScreenHeader(
            title = "Settings",
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
            // 1. ACCOUNT SECTION
            SettingsSectionHeader(title = "ACCOUNT")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Person,
                    title = "Personal",
                    subtitle = "Your name, music, Gemini & YouTube keys",
                    onClick = { viewModel.setTab(MayaNavTab.PERSONAL) },
                    testTag = "settings_row_personal"
                )
            }

            // 2. ASSISTANT SECTION
            SettingsSectionHeader(title = "ASSISTANT")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Headphones,
                    title = "Maya",
                    subtitle = "Persona, girlfriend mode, voice, language",
                    onClick = { viewModel.setTab(MayaNavTab.MAYA_ASSISTANT) },
                    testTag = "settings_row_maya"
                )
                SettingsRowDivider()
                SettingsNavigationRow(
                    icon = Icons.Default.Bolt,
                    title = "Skills",
                    subtitle = "Installed playbooks and the online skill store",
                    onClick = { viewModel.setTab(MayaNavTab.SKILLS) },
                    testTag = "settings_row_skills"
                )
                SettingsRowDivider()
                SettingsNavigationRow(
                    icon = Icons.Default.PrecisionManufacturing,
                    title = "Sub-agents",
                    subtitle = "Coding models and background agents",
                    onClick = { viewModel.setTab(MayaNavTab.SUB_AGENTS) },
                    testTag = "settings_row_sub_agents"
                )
            }

            // 3. WORK & MESSAGES SECTION
            SettingsSectionHeader(title = "WORK & MESSAGES")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Email,
                    title = "Email",
                    subtitle = "Let Maya send mail from your address",
                    onClick = { viewModel.setTab(MayaNavTab.EMAIL) },
                    testTag = "settings_row_email"
                )
                SettingsRowDivider()
                SettingsNavigationRow(
                    icon = Icons.Default.Group,
                    title = "WhatsApp groups & reports",
                    subtitle = "Your groups, and the report formats she fills in",
                    onClick = { viewModel.setTab(MayaNavTab.GROUPS_REPORTS) },
                    testTag = "settings_row_groups_reports"
                )
                SettingsRowDivider()
                SettingsNavigationRow(
                    icon = Icons.Default.Share,
                    title = "Social media",
                    subtitle = "Handle, caption voice, daily story, scheduled posts",
                    onClick = { viewModel.setTab(MayaNavTab.SOCIAL_MEDIA) },
                    testTag = "settings_row_social_media"
                )
            }

            // 4. CONNECTED ACCOUNTS SECTION
            SettingsSectionHeader(title = "CONNECTED ACCOUNTS")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Hub,
                    title = "Connectors",
                    subtitle = "GitHub, Notion, Telegram and more",
                    onClick = { viewModel.setTab(MayaNavTab.CONNECTORS) },
                    testTag = "settings_row_connectors"
                )
            }

            // 5. MEMORY & DATA SECTION
            SettingsSectionHeader(title = "MEMORY & DATA")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Cloud,
                    title = "Backup",
                    subtitle = "Export & restore your memories and chats",
                    onClick = { viewModel.setTab(MayaNavTab.BACKUP) },
                    testTag = "settings_row_backup"
                )
            }

            // 6. SYSTEM SECTION
            SettingsSectionHeader(title = "SYSTEM")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
            ) {
                SettingsNavigationRow(
                    icon = Icons.Default.Tune,
                    title = "Advanced",
                    subtitle = "Behaviour, safety, permissions",
                    onClick = { viewModel.setTab(MayaNavTab.ADVANCED) },
                    testTag = "settings_row_advanced"
                )
                SettingsRowDivider()
                SettingsNavigationRow(
                    icon = Icons.Default.Extension,
                    title = "Optional",
                    subtitle = "Extra integrations — Maps / Places",
                    onClick = { viewModel.setTab(MayaNavTab.OPTIONAL_SETTINGS) },
                    testTag = "settings_row_optional"
                )
            }

            // FOOTER VERSION
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Maya v4.15.1",
                color = TextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = TextSecondary,
        fontSize = 11.5.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun SettingsRowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.6.dp)
            .background(MayaCardBorder)
    )
}

@Composable
private fun SettingsNavigationRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF16233B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MayaElectricBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 15.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
