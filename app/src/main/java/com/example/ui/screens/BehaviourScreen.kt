package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
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
fun BehaviourScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.ADVANCED) },
    modifier: Modifier = Modifier
) {
    val floatingOrbEnabled by viewModel.floatingOrbEnabled.collectAsStateWithLifecycle()
    val echoGuardEnabled by viewModel.echoGuardEnabled.collectAsStateWithLifecycle()
    val startOnBootEnabled by viewModel.startOnBootEnabled.collectAsStateWithLifecycle()
    val batteryBypassEnabled by viewModel.batteryBypassEnabled.collectAsStateWithLifecycle()
    val keepAliveServiceEnabled by viewModel.keepAliveServiceEnabled.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("behaviour_screen")
    ) {
        MayaScreenHeader(
            title = "Behaviour",
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
            // CARD 1: ORB & VOICE BEHAVIOUR
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Overlay & voice",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "How Maya interacts while you use other apps",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                BehaviourSwitchRow(
                    title = "Floating orb overlay",
                    subtitle = "Maya stays accessible as a floating assistant orb across all apps",
                    isChecked = floatingOrbEnabled,
                    onCheckedChange = { viewModel.toggleFloatingOrb(it) },
                    testTag = "switch_floating_orb"
                )

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(MayaCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                BehaviourSwitchRow(
                    title = "Echo guard",
                    subtitle = "Mutes the microphone while Maya speaks to prevent audio feedback",
                    isChecked = echoGuardEnabled,
                    onCheckedChange = { viewModel.toggleEchoGuard(it) },
                    testTag = "switch_echo_guard"
                )
            }

            // CARD 2: SYSTEM LIFECYCLE & RELIABILITY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "System reliability",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Background persistence and auto-start",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                BehaviourSwitchRow(
                    title = "Start on boot",
                    subtitle = "Resume Voice Guardian and background monitors immediately after reboot",
                    isChecked = startOnBootEnabled,
                    onCheckedChange = { viewModel.toggleStartOnBoot(it) },
                    testTag = "switch_start_on_boot"
                )

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(MayaCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                BehaviourSwitchRow(
                    title = "Bypass battery saver",
                    subtitle = "Requests DOZE mode exemption to prevent Android from silencing Maya",
                    isChecked = batteryBypassEnabled,
                    onCheckedChange = { viewModel.toggleBatteryBypass(it) },
                    testTag = "switch_battery_bypass"
                )

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(MayaCardBorder)
                )
                Spacer(modifier = Modifier.height(14.dp))

                BehaviourSwitchRow(
                    title = "Foreground keep-alive service",
                    subtitle = "Maintains active low-power RAM state for instantaneous responses",
                    isChecked = keepAliveServiceEnabled,
                    onCheckedChange = { viewModel.toggleKeepAlive(it) },
                    testTag = "switch_keep_alive"
                )
            }

            // TIP
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
                    text = "Echo guard uses real-time hardware acoustic cancellation. Keep it enabled whenever you use voice conversations with the loudspeaker.",
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
private fun BehaviourSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MayaElectricBlue,
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
