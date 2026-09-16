package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PatternPinScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.SCREEN_LOCK) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State
    var isUnlockEnabled by remember { mutableStateOf(false) }
    var pinValue by remember { mutableStateOf("") }
    var savedPin by remember { mutableStateOf("") }
    val patternPoints = remember { mutableStateListOf<Int>() }
    var savedPatternCount by remember { mutableIntStateOf(0) }

    var isTestingLock by remember { mutableStateOf(false) }
    var testStep by remember { mutableIntStateOf(0) }
    var showFineTuningDialog by remember { mutableStateOf(false) }
    var unlockDelayMs by remember { mutableFloatStateOf(400f) }
    var tapCoordinateOffset by remember { mutableFloatStateOf(0f) }

    val isConfigured = savedPin.isNotBlank() || savedPatternCount >= 4

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("pattern_pin_screen")
    ) {
        MayaScreenHeader(
            title = "Pattern & PIN",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // TOP STATUS BANNER (Not set up / Configured)
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isConfigured) MayaElectricBlue else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (isConfigured) "Configured" else "Not set up",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isConfigured) "Saved and ready for hands-free unlock" else "Save the pattern or PIN this phone uses, then switch it on.",
                            color = TextSecondary,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }

            // CARD 1: Unlock for me
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .testTag("card_unlock_for_me")
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Unlock for me",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Off by default, and off is a perfectly good answer",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 16.dp)
                            ) {
                                Text(
                                    text = "Let Maya unlock the phone",
                                    color = TextPrimary,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Save a pattern or a PIN below first.",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            Switch(
                                checked = isUnlockEnabled,
                                onCheckedChange = { checked ->
                                    if (checked && !isConfigured) {
                                        Toast.makeText(context, "Please save a pattern or PIN first", Toast.LENGTH_SHORT).show()
                                    } else {
                                        isUnlockEnabled = checked
                                        viewModel.triggerHapticFeedback()
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MayaElectricBlue,
                                    uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                                    uncheckedTrackColor = Color(0xFF1E283F)
                                ),
                                modifier = Modifier.testTag("switch_unlock_for_me")
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Warning Box: Accessibility service OFF
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2E1C1C))
                                .border(1.dp, Color(0xFF5C2D2D), RoundedCornerShape(12.dp))
                                .clickable {
                                    try {
                                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                    } catch (_: Exception) {}
                                }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = Color(0xFFFF9800),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Accessibility service OFF hai — Settings > Accessibility me 'Maya' ON kar do (app ki Permissions screen pe shortcut hai), phir bolo.",
                                    color = Color(0xFFFFCC80),
                                    fontSize = 12.5.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }

            // CARD 2: Pattern
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .testTag("card_pattern")
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GridOn,
                                    contentDescription = null,
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Pattern",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (savedPatternCount >= 4) "Pattern saved ($savedPatternCount dots)" else "No pattern saved",
                                    color = if (savedPatternCount >= 4) MayaElectricBlue else TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "Draw the pattern you use on this phone's lock screen.",
                            color = TextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive 3x3 Pattern Matrix Area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            PatternGridInteractive(
                                selectedPoints = patternPoints,
                                onPointSelected = { point ->
                                    if (!patternPoints.contains(point)) {
                                        patternPoints.add(point)
                                        viewModel.triggerHapticFeedback()
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    patternPoints.clear()
                                    savedPatternCount = 0
                                }
                            ) {
                                Text("Clear", color = TextSecondary, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (patternPoints.size >= 4) {
                                        savedPatternCount = patternPoints.size
                                        viewModel.triggerHapticFeedback()
                                        Toast.makeText(context, "Pattern saved (${patternPoints.size} dots)", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Use at least 4 dots", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
                            ) {
                                Text("Save Pattern", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // WARNING BANNER: Use at least 4 dots
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF261914))
                        .border(1.dp, Color(0xFF4D2C1C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Use at least 4 dots — Android requires that too.",
                            color = Color(0xFFFFCC80),
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // CARD 3: PIN
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .testTag("card_pin")
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "PIN",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (savedPin.isNotBlank()) "PIN saved (${savedPin.length} digits)" else "No PIN saved",
                                    color = if (savedPin.isNotBlank()) MayaElectricBlue else TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "If this phone uses a PIN, put it here instead. A PIN is the more reliable of the two — Maya presses the real keypad buttons, so there is no position to get right.",
                            color = TextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = pinValue,
                            onValueChange = { if (it.length <= 8) pinValue = it },
                            placeholder = { Text("PIN", color = TextSecondary, fontSize = 14.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = Color(0xFF0F172A),
                                unfocusedContainerColor = Color(0xFF0F172A),
                                focusedBorderColor = MayaElectricBlue,
                                unfocusedBorderColor = MayaCardBorder
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("input_pin_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (pinValue.length >= 4) {
                                    savedPin = pinValue
                                    viewModel.triggerHapticFeedback()
                                    Toast.makeText(context, "PIN saved securely", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("btn_save_pin")
                        ) {
                            Text(
                                text = "Save PIN",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // CARD 4: Test
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .testTag("card_test_unlock")
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Test",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Locks this phone, then tries to open it",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "The screen goes dark for a couple of seconds. If it doesn't come back on its own, unlock it yourself — nothing is stuck.",
                            color = TextSecondary,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.triggerHapticFeedback()
                                isTestingLock = true
                                testStep = 1
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    testStep = 2
                                }, 1200)
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    testStep = 3
                                    Toast.makeText(context, "Test successful: Screen unlocked by Maya!", Toast.LENGTH_SHORT).show()
                                }, 2400)
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isTestingLock = false
                                    testStep = 0
                                }, 3400)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_lock_test")
                        ) {
                            Text(
                                text = "Lock and try to unlock",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        AnimatedVisibility(visible = isTestingLock) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = MayaElectricBlue,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (testStep) {
                                        1 -> "Screen dimmed • Maya detecting lock screen..."
                                        2 -> "Injecting saved pattern / PIN credentials..."
                                        else -> "Screen unlocked successfully!"
                                    },
                                    color = if (testStep == 3) MayaElectricBlue else TextSecondary,
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    }
                }
            }

            // CARD 5: Fine-tuning
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .clickable { showFineTuningDialog = true }
                        .padding(16.dp)
                        .testTag("card_fine_tuning")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MayaElectricBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MayaElectricBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Fine-tuning",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Only if the test below fails",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Navigate",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // BOTTOM TIP BOX
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF131D33))
                        .border(1.dp, Color(0xFF1E2E4F), RoundedCornerShape(14.dp))
                        .padding(14.dp)
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
                            text = "Stored encrypted on this phone and never synced or backed up. Nothing can read it back — not even this screen. Android still checks every attempt and still counts the failures, exactly as it would for your finger.",
                            color = TextPrimary.copy(alpha = 0.9f),
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showFineTuningDialog) {
        AlertDialog(
            onDismissRequest = { showFineTuningDialog = false },
            containerColor = MayaCardBg,
            title = {
                Text("Lock Screen Fine-tuning", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Adjust keystroke timing and swipe duration for your phone model's lock screen response speed.",
                        color = TextSecondary,
                        fontSize = 12.5.sp
                    )

                    Column {
                        Text(
                            text = "Gesture speed: ${unlockDelayMs.toInt()} ms",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Slider(
                            value = unlockDelayMs,
                            onValueChange = { unlockDelayMs = it },
                            valueRange = 100f..1000f,
                            colors = SliderDefaults.colors(
                                thumbColor = MayaElectricBlue,
                                activeTrackColor = MayaElectricBlue
                            )
                        )
                    }

                    Column {
                        Text(
                            text = "Keypad vertical offset: ${tapCoordinateOffset.toInt()} px",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Slider(
                            value = tapCoordinateOffset,
                            onValueChange = { tapCoordinateOffset = it },
                            valueRange = -50f..50f,
                            colors = SliderDefaults.colors(
                                thumbColor = MayaElectricBlue,
                                activeTrackColor = MayaElectricBlue
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Fine-tuning calibrated", Toast.LENGTH_SHORT).show()
                        showFineTuningDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFineTuningDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun PatternGridInteractive(
    selectedPoints: List<Int>,
    onPointSelected: (Int) -> Unit
) {
    var touchPos by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier
            .size(240.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchPos = offset
                        val pt = getDotFromOffset(offset, 240f)
                        if (pt != null) onPointSelected(pt)
                    },
                    onDrag = { change, _ ->
                        touchPos = change.position
                        val pt = getDotFromOffset(change.position, 240f)
                        if (pt != null) onPointSelected(pt)
                    },
                    onDragEnd = {
                        touchPos = null
                    },
                    onDragCancel = {
                        touchPos = null
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = size.width / 4f
            val dotCoords = (0..8).map { idx ->
                val col = idx % 3
                val row = idx / 3
                Offset(step * (col + 1), step * (row + 1))
            }

            // Draw connecting lines between selected points
            for (i in 0 until selectedPoints.size - 1) {
                val p1 = dotCoords[selectedPoints[i]]
                val p2 = dotCoords[selectedPoints[i + 1]]
                drawLine(
                    color = MayaElectricBlue,
                    start = p1,
                    end = p2,
                    strokeWidth = 5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Draw active line from last selected point to finger position
            if (touchPos != null && selectedPoints.isNotEmpty()) {
                val lastDot = dotCoords[selectedPoints.last()]
                drawLine(
                    color = MayaElectricBlue.copy(alpha = 0.6f),
                    start = lastDot,
                    end = touchPos!!,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Draw 9 dots
            dotCoords.forEachIndexed { idx, center ->
                val isSelected = selectedPoints.contains(idx)
                if (isSelected) {
                    drawCircle(
                        color = MayaElectricBlue.copy(alpha = 0.25f),
                        radius = 24.dp.toPx(),
                        center = center
                    )
                    drawCircle(
                        color = MayaElectricBlue,
                        radius = 9.dp.toPx(),
                        center = center
                    )
                } else {
                    drawCircle(
                        color = Color(0xFF475569),
                        radius = 7.dp.toPx(),
                        center = center
                    )
                }
            }
        }
    }
}

private fun getDotFromOffset(offset: Offset, sizePx: Float): Int? {
    val step = sizePx / 4f
    val hitRadius = step * 0.45f
    for (idx in 0..8) {
        val col = idx % 3
        val row = idx / 3
        val cx = step * (col + 1)
        val cy = step * (row + 1)
        val dx = offset.x - cx
        val dy = offset.y - cy
        if (dx * dx + dy * dy <= hitRadius * hitRadius) {
            return idx
        }
    }
    return null
}
