package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EventTriggersScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val switchStates = remember {
        mutableStateMapOf(
            // Announcements
            "announcements_master" to true,
            "speak_asleep" to true,
            "speak_silent" to false,
            // Power & Battery
            "charger_plugged" to true,
            "charger_unplugged" to true,
            "battery_full" to true,
            "battery_low" to true,
            "battery_critical" to true,
            "battery_saver_on" to false,
            "battery_saver_off" to false,
            // Headphones & Bluetooth
            "headphones_plugged" to true,
            "headphones_unplugged" to true,
            "bt_connected" to true,
            "bt_disconnected" to true,
            // Network
            "wifi_connected" to false,
            "wifi_lost" to false,
            "airplane_on" to true,
            "airplane_off" to true,
            // System
            "silent_on" to false,
            "ringer_on" to false,
            "app_installed" to true,
            "app_uninstalled" to true
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("event_triggers_screen")
    ) {
        MayaScreenHeader(
            title = "Event triggers",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. ANNOUNCEMENTS CARD
            item {
                TriggerCard(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = "Announcements",
                    subtitle = "Maya speaks up when the phone does something"
                ) {
                    TriggerSwitchRow(
                        title = "Event announcements",
                        subtitle = "Master switch — off means she never brings any of this up",
                        checked = switchStates["announcements_master"] == true,
                        onCheckedChange = {
                            switchStates["announcements_master"] = it
                            viewModel.triggerHapticFeedback()
                        },
                        tag = "trigger_announcements_master"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Speak while she's asleep",
                        subtitle = "Uses the phone's own voice when no session is live. Stays quiet on silent, in Do Not Disturb, and during calls.",
                        checked = switchStates["speak_asleep"] == true,
                        onCheckedChange = {
                            switchStates["speak_asleep"] = it
                            viewModel.triggerHapticFeedback()
                        },
                        tag = "trigger_speak_asleep"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Speak on silent too",
                        subtitle = "Ignore silent mode and Do Not Disturb. Turn this on if your phone lives on silent. Calls still silence her.",
                        checked = switchStates["speak_silent"] == true,
                        onCheckedChange = {
                            switchStates["speak_silent"] = it
                            viewModel.triggerHapticFeedback()
                        },
                        tag = "trigger_speak_silent"
                    )
                }
            }

            // INFO TIP BANNER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF131D33))
                        .border(1.dp, Color(0xFF1E2E4F), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MayaElectricBlue,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "These only change what Maya says. Nothing here alters a phone setting — turning off \"Battery saver on\" stops the announcement, not the saver.",
                            color = TextPrimary.copy(alpha = 0.9f),
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // 2. POWER & BATTERY CARD
            item {
                TriggerCard(
                    icon = Icons.Default.BatteryChargingFull,
                    title = "Power & battery"
                ) {
                    TriggerSwitchRow(
                        title = "Charger plugged in",
                        checked = switchStates["charger_plugged"] == true,
                        onCheckedChange = { switchStates["charger_plugged"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_charger_plugged"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Charger unplugged",
                        checked = switchStates["charger_unplugged"] == true,
                        onCheckedChange = { switchStates["charger_unplugged"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_charger_unplugged"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Battery full",
                        checked = switchStates["battery_full"] == true,
                        onCheckedChange = { switchStates["battery_full"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_battery_full"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Battery low (20%)",
                        subtitle = "Once per discharge, not every reading",
                        checked = switchStates["battery_low"] == true,
                        onCheckedChange = { switchStates["battery_low"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_battery_low"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Battery critical (10%)",
                        subtitle = "Once per discharge, not every reading",
                        checked = switchStates["battery_critical"] == true,
                        onCheckedChange = { switchStates["battery_critical"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_battery_critical"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Battery saver on",
                        checked = switchStates["battery_saver_on"] == true,
                        onCheckedChange = { switchStates["battery_saver_on"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_battery_saver_on"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Battery saver off",
                        checked = switchStates["battery_saver_off"] == true,
                        onCheckedChange = { switchStates["battery_saver_off"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_battery_saver_off"
                    )
                }
            }

            // 3. HEADPHONES & BLUETOOTH CARD
            item {
                TriggerCard(
                    icon = Icons.Default.Headphones,
                    title = "Headphones & Bluetooth"
                ) {
                    TriggerSwitchRow(
                        title = "Headphones plugged in",
                        checked = switchStates["headphones_plugged"] == true,
                        onCheckedChange = { switchStates["headphones_plugged"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_headphones_plugged"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Headphones unplugged",
                        checked = switchStates["headphones_unplugged"] == true,
                        onCheckedChange = { switchStates["headphones_unplugged"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_headphones_unplugged"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Bluetooth device connected",
                        checked = switchStates["bt_connected"] == true,
                        onCheckedChange = { switchStates["bt_connected"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_bt_connected"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Bluetooth device disconnected",
                        checked = switchStates["bt_disconnected"] == true,
                        onCheckedChange = { switchStates["bt_disconnected"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_bt_disconnected"
                    )
                }
            }

            // 4. NETWORK CARD
            item {
                TriggerCard(
                    icon = Icons.Default.Wifi,
                    title = "Network"
                ) {
                    TriggerSwitchRow(
                        title = "Wi-Fi connected",
                        subtitle = "Off by default — networks change all day",
                        checked = switchStates["wifi_connected"] == true,
                        onCheckedChange = { switchStates["wifi_connected"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_wifi_connected"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Wi-Fi lost",
                        subtitle = "Off by default — networks change all day",
                        checked = switchStates["wifi_lost"] == true,
                        onCheckedChange = { switchStates["wifi_lost"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_wifi_lost"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Airplane mode on",
                        checked = switchStates["airplane_on"] == true,
                        onCheckedChange = { switchStates["airplane_on"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_airplane_on"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Airplane mode off",
                        checked = switchStates["airplane_off"] == true,
                        onCheckedChange = { switchStates["airplane_off"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_airplane_off"
                    )
                }
            }

            // 5. SYSTEM CARD
            item {
                TriggerCard(
                    icon = Icons.Default.Tune,
                    title = "System"
                ) {
                    TriggerSwitchRow(
                        title = "Phone put on silent",
                        subtitle = "Off by default — you just did it yourself",
                        checked = switchStates["silent_on"] == true,
                        onCheckedChange = { switchStates["silent_on"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_silent_on"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "Ringer back on",
                        subtitle = "Off by default — you just did it yourself",
                        checked = switchStates["ringer_on"] == true,
                        onCheckedChange = { switchStates["ringer_on"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_ringer_on"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "App installed",
                        subtitle = "New apps only, not Play Store updates",
                        checked = switchStates["app_installed"] == true,
                        onCheckedChange = { switchStates["app_installed"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_app_installed"
                    )
                    HorizontalDivider(color = MayaCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                    TriggerSwitchRow(
                        title = "App uninstalled",
                        subtitle = "Real uninstalls only, not updates",
                        checked = switchStates["app_uninstalled"] == true,
                        onCheckedChange = { switchStates["app_uninstalled"] = it; viewModel.triggerHapticFeedback() },
                        tag = "trigger_app_uninstalled"
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TriggerCard(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MayaElectricBlue.copy(alpha = 0.15f)),
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
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            content()
        }
    }
}

@Composable
private fun TriggerSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MayaElectricBlue,
                uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                uncheckedTrackColor = Color(0xFF1E283F)
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}
