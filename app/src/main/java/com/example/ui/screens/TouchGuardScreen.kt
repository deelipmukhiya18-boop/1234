package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.MayaEnergyGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TouchGuardScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val touchGuardEnabled by viewModel.touchGuardEnabled.collectAsStateWithLifecycle()
    val wakeScreenEnabled by viewModel.touchGuardWakeScreen.collectAsStateWithLifecycle()
    val pickupEnabled by viewModel.touchGuardPickup.collectAsStateWithLifecycle()
    val chargerEnabled by viewModel.touchGuardCharger.collectAsStateWithLifecycle()
    val sirenEnabled by viewModel.touchGuardSiren.collectAsStateWithLifecycle()
    val incidents by viewModel.touchGuardIncidents.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("touch_guard_screen")
    ) {
        MayaScreenHeader(
            title = "Touch Guard",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // MAIN CARD (Exact match to Screenshot 20260911_072443)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rounded blue square hand icon
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.18f))
                                    .border(1.dp, MayaElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FrontHand,
                                    contentDescription = "Touch Guard Icon",
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Touch Guard",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Watch the phone while you are away from it",
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Paragraph description matching Screenshot
                        Text(
                            text = "When armed, anyone who wakes the screen, picks the phone up or pulls the charger out gets photographed with the front camera. Maya then tells you out loud that someone touched your phone, warns whoever is holding it, and sounds a siren.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(thickness = 0.8.dp, color = MayaCardBorder)
                        Spacer(modifier = Modifier.height(14.dp))

                        // TOGGLE ROW: Enable Touch Guard
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Enable Touch Guard",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Off by default. Nothing below does anything until this is on.",
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Switch(
                                checked = touchGuardEnabled,
                                onCheckedChange = { viewModel.setTouchGuardEnabled(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MayaElectricBlue,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = Color(0xFF1E2638)
                                ),
                                modifier = Modifier.testTag("toggle_touch_guard")
                            )
                        }
                    }
                }
            }

            // EXTENDED SETTINGS (VISIBLE WHEN TOUCH GUARD IS ENABLED)
            item {
                AnimatedVisibility(
                    visible = touchGuardEnabled,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // SENSOR TRIGGERS CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MayaCardBg)
                                .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Armed Triggers",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                TriggerOptionRow(
                                    icon = Icons.Default.ScreenLockPortrait,
                                    title = "Wake screen trigger",
                                    subtitle = "Snap photo if screen is powered on",
                                    checked = wakeScreenEnabled,
                                    onCheckedChange = { viewModel.toggleTouchGuardWake(it) }
                                )

                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MayaCardBorder,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )

                                TriggerOptionRow(
                                    icon = Icons.Default.Vibration,
                                    title = "Pick-up / Motion sensor",
                                    subtitle = "Detect phone being lifted or tilted",
                                    checked = pickupEnabled,
                                    onCheckedChange = { viewModel.toggleTouchGuardPickup(it) }
                                )

                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MayaCardBorder,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )

                                TriggerOptionRow(
                                    icon = Icons.Default.Power,
                                    title = "Charger unplugged",
                                    subtitle = "Alarm when USB charging cable is detached",
                                    checked = chargerEnabled,
                                    onCheckedChange = { viewModel.toggleTouchGuardCharger(it) }
                                )
                            }
                        }

                        // DETERRENCE & ACTIONS CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MayaCardBg)
                                .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Alert Responses",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                TriggerOptionRow(
                                    icon = Icons.Default.VolumeUp,
                                    title = "High-decibel siren",
                                    subtitle = "Sound loud alarm siren through speaker",
                                    checked = sirenEnabled,
                                    onCheckedChange = { viewModel.toggleTouchGuardSiren(it) }
                                )

                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MayaCardBorder,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )

                                TriggerOptionRow(
                                    icon = Icons.Default.CameraAlt,
                                    title = "Front camera snapshot",
                                    subtitle = "Silent selfie capture of the intruder",
                                    checked = true,
                                    enabled = false,
                                    onCheckedChange = { }
                                )
                            }
                        }

                        // TEST SIMULATION CONTROLS
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MayaCardBg)
                                .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Test Guard Triggers",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Simulate an intruder event to verify front camera snapshot and siren.",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.simulateTouchGuardTrigger("Screen Wakeup")
                                            Toast.makeText(context, "Simulating screen touch trigger...", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MayaElectricBlue)
                                    ) {
                                        Text("Wake Touch", fontSize = 12.sp)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            viewModel.simulateTouchGuardTrigger("Charger Unplugged")
                                            Toast.makeText(context, "Simulating charger disconnect...", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MayaElectricBlue)
                                    ) {
                                        Text("Unplug Cable", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // INTRUSION LOG SECTION
            if (incidents.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Detected Incidents (${incidents.size})",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { viewModel.clearTouchGuardIncidents() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear Log",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                items(incidents) { incident ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MayaCardBg)
                            .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Alert",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = incident.trigger,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = incident.timestamp,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Snapshot",
                                        tint = MayaElectricBlue,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Photo taken",
                                        color = MayaElectricBlue,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TriggerOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (checked) MayaElectricBlue else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MayaElectricBlue,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color(0xFF1E2638)
            )
        )
    }
}
