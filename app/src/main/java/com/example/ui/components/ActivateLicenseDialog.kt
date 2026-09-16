package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ActivateLicenseDialog(
    viewModel: MayaViewModel,
    onDismiss: () -> Unit
) {
    var licenseKey by remember { mutableStateOf("") }
    var isActivated by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MayaCardBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = MayaElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Activate Maya License",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isActivated) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MayaGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "License Activated Successfully!",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Unlimited talk time, 234 tools & PC link unlocked.",
                                color = MayaGreen,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Activate a license for full tool access, unlimited talk time, and low-latency PC link.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = licenseKey,
                        onValueChange = { licenseKey = it },
                        placeholder = { Text("Enter License Key (e.g. MAYA-PRO-2026)", color = TextMuted) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("license_key_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Don't have a key? Tap 'Demo Key' to test Pro features instantly.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            if (isActivated) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MayaGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Close", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {
                        isActivated = true
                        viewModel.triggerHapticFeedback()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("submit_license_button")
                ) {
                    Text("Activate", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isActivated) {
                TextButton(
                    onClick = {
                        licenseKey = "HUNTER-PRO-2026-VIP"
                        isActivated = true
                        viewModel.triggerHapticFeedback()
                    }
                ) {
                    Text("Use Demo Key", color = MayaElectricBlue)
                }
            }
        }
    )
}
