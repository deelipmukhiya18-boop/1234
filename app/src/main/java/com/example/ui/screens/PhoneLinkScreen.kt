package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaAmber
import com.example.ui.theme.MayaBlack
import com.example.ui.theme.MayaBlue
import com.example.ui.theme.MayaBorder
import com.example.ui.theme.MayaCardDark
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.MayaRed
import com.example.ui.theme.MayaRedGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhoneLinkScreen(
    viewModel: MayaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val session by viewModel.session.collectAsStateWithLifecycle()
    val actions by viewModel.actions.collectAsStateWithLifecycle()
    val batteryLevel by viewModel.batteryLevel.collectAsStateWithLifecycle()
    val latestOtp by viewModel.latestOtp.collectAsStateWithLifecycle()
    val isDuetActive by viewModel.isDuetActive.collectAsStateWithLifecycle()
    val isTorchOn by viewModel.isFlashlightOn.collectAsStateWithLifecycle()

    var selectedBankSim by remember { mutableStateOf("HDFC Bank") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MayaBlack)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Hero Graphic Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("phone_link_hero_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_maya_hero),
                        contentDescription = "Maya Phone Link Hero",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MayaBlack.copy(alpha = 0.55f))
                            .padding(14.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MayaGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ONE SESSION · TWO DEVICES",
                                    color = MayaRedGlow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = "Phone Link: Maya reaches into Android",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time Phone Telemetry Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = "Battery",
                            tint = MayaGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$batteryLevel%",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Wi-Fi",
                            tint = MayaBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "5GHz · ${session?.lastPingMs ?: 12}ms",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Security",
                            tint = MayaAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "P2P WebRTC",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OTP & 2FA Detection Radar (from Page 5 brochure)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("otp_radar_card"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = "OTP Vault",
                                tint = MayaRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OTP & 2FA RADAR",
                                color = MayaRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "\"Ask my phone if I got an OTP\"",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LATEST EXTRACTED CODE",
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = latestOtp,
                                color = TextPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 4.sp
                            )
                            Text(
                                text = "Synced to Desktop Clipboard · Valid for 5 min",
                                color = MayaGreen,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Maya OTP", latestOtp))
                                Toast.makeText(context, "OTP copied: $latestOtp", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MayaCardHighlight)
                                .testTag("copy_otp_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy OTP",
                                tint = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MayaBorder)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "TEST INCOMING SMS TRIGGER:",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("HDFC Bank", "SBI Card", "ICICI Bank", "Swiggy UPI", "Google 2FA").forEach { bank ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MayaCardHighlight)
                                    .clickable {
                                        selectedBankSim = bank
                                        viewModel.triggerSimulatedOtp(bank, "₹3,499")
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("simulate_otp_$bank")
                            ) {
                                Text(
                                    text = "+ $bank",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Remote App Launchers (Page 5: "Open Instagram on my phone")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("remote_apps_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "REMOTE PHONE CONTROLS (DESKTOP TRIGGER)",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Phone Call
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("Call") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_call"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📞", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Call", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // WhatsApp
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("WhatsApp") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_whatsapp"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💬", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("WhatsApp", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Camera
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("Camera") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_camera"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📷", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Camera", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Torch
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.toggleTorch() }
                                .padding(vertical = 12.dp)
                                .testTag("launch_torch"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔦", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Torch", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Alarm
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.directAlarm(7, 0) }
                                .padding(vertical = 12.dp)
                                .testTag("launch_alarm"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⏰", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Alarm", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Timer
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.directTimer(5) }
                                .padding(vertical = 12.dp)
                                .testTag("launch_timer"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⏱️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Timer 5m", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Music
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.directMusic("lofi beats") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_music"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🎵", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Music", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Volume Up
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.directVolume(true) }
                                .padding(vertical = 12.dp)
                                .testTag("launch_vol_up"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔊", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Vol +", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Mute
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.directMute() }
                                .padding(vertical = 12.dp)
                                .testTag("launch_mute"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔇", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Mute", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Wi-Fi
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("WiFi") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_wifi"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📶", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Wi-Fi", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Bluetooth
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("Bluetooth") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_bluetooth"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔵", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Bluetooth", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Settings
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MayaCardHighlight)
                                .clickable { viewModel.launchInstalledApp("Settings") }
                                .padding(vertical = 12.dp)
                                .testTag("launch_settings"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⚙️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Settings", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions & Sync Activity Log Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIONS & SYNC HISTORY",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                IconButton(
                    onClick = { viewModel.clearActionHistory() },
                    modifier = Modifier.testTag("clear_actions_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Actions",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        // Actions Feed Items
        items(actions) { action ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MayaRed.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = action.source,
                                    color = MayaRed,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = action.title,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = action.details,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(action.timestamp)),
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
