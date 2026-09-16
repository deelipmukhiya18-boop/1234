package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaBlack
import com.example.ui.theme.MayaBorder
import com.example.ui.theme.MayaCardDark
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaCoral
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.MayaRed
import com.example.ui.theme.MayaRedGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PairingScreen(
    viewModel: MayaViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    var inputCode by remember { mutableStateOf("MAYA-HUB-608") }

    val infiniteTransition = rememberInfiniteTransition(label = "laser_scan")
    val laserOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 160f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MayaBlack)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Section Title
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "DESKTOP QR PAIRING",
                    color = MayaRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Join Desktop Maya Session",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Scan QR on your Windows screen or enter session token.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Futuristic QR Camera Scanner Viewfinder Simulation
            Card(
                modifier = Modifier
                    .size(240.dp)
                    .testTag("qr_viewfinder_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MayaRed)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Scanning laser line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .offset(y = (laserOffset - 80f).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, MayaRedGlow, MayaCoral, Color.Transparent)
                                )
                            )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan QR",
                            tint = MayaCoral,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Align HUD QR Code",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "No cloud relay needed",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Manual Pairing Code Input Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pairing_code_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SESSION PAIRING TOKEN",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputCode,
                        onValueChange = { inputCode = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("session_token_input"),
                        placeholder = { Text("e.g. MAYA-HUB-608", color = TextSecondary) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MayaCardHighlight,
                            unfocusedContainerColor = MayaCardHighlight,
                            focusedBorderColor = MayaRed,
                            unfocusedBorderColor = MayaBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.pairWithSessionCode(inputCode) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pair_now_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MayaRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Pair With Desktop", fontWeight = FontWeight.Bold)
                        }

                        if (session?.isConnected == true) {
                            OutlinedButton(
                                onClick = { viewModel.disconnectSession() },
                                modifier = Modifier.testTag("disconnect_button"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MayaRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MayaRed),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LinkOff,
                                    contentDescription = "Disconnect",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Host Specifications Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("paired_host_specs_card"),
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
                            text = "PAIRED DESKTOP STATUS",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (session?.isConnected == true) MayaGreen else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (session?.isConnected == true) "Synchronized" else "Offline",
                                color = if (session?.isConnected == true) MayaGreen else Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val specs = listOf(
                        Pair("Desktop Device", session?.desktopName ?: "Hunter-Studio"),
                        Pair("Network Address", session?.desktopIp ?: "192.168.1.108:8765"),
                        Pair("Desktop Version", "Maya 6.0.8 (Windows 11)"),
                        Pair("Voice Engine", "Google Gemini Live API"),
                        Pair("Active Agents", "${session?.agentCount ?: 7} Autonomous Agents"),
                        Pair("Voice Tools", "${session?.toolsEnabledCount ?: 234} Tools across 24 Categories"),
                        Pair("Encryption", "P2P WebRTC Direct TLS")
                    )

                    specs.forEach { (label, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = label, color = TextSecondary, fontSize = 12.sp)
                            Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
