package com.example.ui.screens

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.MayaEnergyGold
import com.example.ui.theme.MayaGreenHeart
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MayaViewModel,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val energy by viewModel.energy.collectAsStateWithLifecycle()
    val freeMinutes by viewModel.freeMinutesLeft.collectAsStateWithLifecycle()
    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val moodState by viewModel.moodState.collectAsStateWithLifecycle()
    val floatingHearts by viewModel.floatingHearts.collectAsStateWithLifecycle()
    val lastReply by viewModel.lastMayaReply.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Dynamic Current Date (Matches "11 Fri, Sep" format)
    val dayFormat = remember { SimpleDateFormat("dd", Locale.getDefault()) }
    val monthDayFormat = remember { SimpleDateFormat("EEE, MMM", Locale.getDefault()) }
    val now = remember { Date() }
    val dayNumber = remember { dayFormat.format(now) }
    val dayMonthText = remember { monthDayFormat.format(now) }

    // Floating breathing animation for Maya
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val characterBreathScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("home_screen")
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onOpenDrawer,
                modifier = Modifier
                    .size(42.dp)
                    .testTag("home_drawer_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Drawer Menu",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Maya",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { viewModel.openQuickTool("Notifications") },
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("home_notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Maya Avatar in rounded square (matches screenshot)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { viewModel.setTab(MayaNavTab.SETTINGS) }
                        .testTag("home_avatar_badge")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_maya_avatar),
                        contentDescription = "Maya Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TOP BANNER: Free Mode & Activate
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_free_mode_banner"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MayaCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, MayaCardBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MayaElectricBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = MayaElectricBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Free mode • $freeMinutes",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Activate a license for tools, PC link and unlimited talk",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }

                Text(
                    text = "Activate",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.setActivateDialog(true) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("banner_activate_button")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // GREETING & ENERGY SECTION
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Good morning,",
                    color = TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "there",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Maya is ready to help you.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            // Energy Badge (matches screenshot: lightning bolt, 1, Energy)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                    .clickable { viewModel.openQuickTool("Energy") }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("home_energy_badge")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Energy",
                        tint = MayaEnergyGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$energy",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Energy",
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // INTERACTIVE CHARACTER CENTERPIECE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    viewModel.tapCharacter()
                }
                .testTag("home_character_centerpiece"),
            contentAlignment = Alignment.Center
        ) {
            // Character Image
            Image(
                painter = painterResource(id = R.drawable.img_maya_character),
                contentDescription = "Interactive Maya Character - Tap to interact",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(characterBreathScale)
            )

            // Animated Floating Green Hearts (Matches Screenshot 2!)
            floatingHearts.forEach { heart ->
                Box(
                    modifier = Modifier
                        .offset(x = heart.offsetX.dp, y = (-70).dp)
                        .testTag("floating_heart")
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love Maya",
                        tint = MayaGreenHeart,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }
        }

        // Quick speech bubble if Maya spoke
        AnimatedVisibility(
            visible = lastReply.isNotBlank(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MayaCardBg.copy(alpha = 0.85f))
                    .border(1.dp, MayaElectricBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "✨ $lastReply",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3 QUICK ACTION PILLS (Music, Study, Journal)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickPill(
                icon = Icons.Default.MusicNote,
                label = "Music",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openQuickTool("Music") }
            )
            QuickPill(
                icon = Icons.Default.MenuBook,
                label = "Study",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openQuickTool("Study") }
            )
            QuickPill(
                icon = Icons.Default.Edit,
                label = "Journal",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openQuickTool("Journal") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // PHONE AUTOMATION CONTROLS (DIRECT PHONE SYSTEM TRIGGER)
        val phoneControlScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(phoneControlScroll),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhoneQuickButton(
                icon = Icons.Default.Phone,
                label = "Call",
                onClick = { viewModel.launchInstalledApp("Call") }
            )
            PhoneQuickButton(
                icon = Icons.Default.Chat,
                label = "WhatsApp",
                onClick = { viewModel.launchInstalledApp("WhatsApp") }
            )
            PhoneQuickButton(
                icon = Icons.Default.FlashlightOn,
                label = "Torch",
                onClick = { viewModel.toggleTorch() }
            )
            PhoneQuickButton(
                icon = Icons.Default.Alarm,
                label = "7 AM Alarm",
                onClick = { viewModel.directAlarm(7, 0) }
            )
            PhoneQuickButton(
                icon = Icons.Default.CameraAlt,
                label = "Camera",
                onClick = { viewModel.launchInstalledApp("Camera") }
            )
            PhoneQuickButton(
                icon = Icons.Default.VolumeUp,
                label = "Volume +",
                onClick = { viewModel.directVolume(true) }
            )
            PhoneQuickButton(
                icon = Icons.Default.Wifi,
                label = "Wi-Fi",
                onClick = { viewModel.launchInstalledApp("WiFi") }
            )
            PhoneQuickButton(
                icon = Icons.Default.Search,
                label = "Google Search",
                onClick = { viewModel.directWebSearch("trending news today") }
            )
            PhoneQuickButton(
                icon = Icons.Default.CalendarMonth,
                label = "Schedule",
                onClick = { viewModel.directCalendar() }
            )
            PhoneQuickButton(
                icon = Icons.Default.Email,
                label = "Email",
                onClick = { viewModel.directEmail() }
            )
            PhoneQuickButton(
                icon = Icons.Default.WarningAmber,
                label = "Strobe SOS",
                onClick = { viewModel.directStrobe() }
            )
            PhoneQuickButton(
                icon = Icons.Default.Vibration,
                label = "Vibrate Mode",
                onClick = { viewModel.directVibrate() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3 DASHBOARD WIDGET CARDS (Weather, Today, Mood)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Weather Card
            DashboardWidgetCard(
                icon = Icons.Default.Cloud,
                header = "Weather",
                mainValue = if (weatherState.contains("27°C")) "27°C" else "—",
                subValue = if (weatherState.contains("27°C")) "Clear Sky" else "No data",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.toggleWeather() },
                tag = "widget_weather"
            )

            // Today Card
            DashboardWidgetCard(
                icon = Icons.Default.CalendarToday,
                header = "Today",
                mainValue = dayNumber,
                subValue = dayMonthText,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.openQuickTool("Calendar") },
                tag = "widget_today"
            )

            // Mood Card
            val moodParts = moodState.split("\n")
            DashboardWidgetCard(
                icon = Icons.Default.Favorite,
                header = "Mood",
                mainValue = moodParts.getOrElse(0) { "Warm" },
                subValue = moodParts.getOrElse(1) { "All good" },
                modifier = Modifier.weight(1f),
                onClick = { viewModel.cycleMood() },
                tag = "widget_mood"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FLOATING QUERY INPUT BAR ("Ask Maya anything...")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(MayaCardBg)
                .border(1.dp, MayaCardBorder, RoundedCornerShape(26.dp))
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .testTag("home_ask_input_bar")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attachment Icon
                IconButton(
                    onClick = { viewModel.openQuickTool("Attachments") },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach File",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Text Input
                BasicTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ask_maya_input_field"),
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(MayaElectricBlue),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                viewModel.submitSpokenPrompt(inputText)
                                inputText = ""
                                viewModel.setTab(MayaNavTab.CHAT)
                            }
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Ask Maya anything...",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )

                // Send Icon Button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.submitSpokenPrompt(inputText)
                            inputText = ""
                            viewModel.setTab(MayaNavTab.CHAT)
                        }
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("home_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Prompt",
                        tint = if (inputText.isNotBlank()) MayaElectricBlue else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun QuickPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TextSecondary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DashboardWidgetCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    header: String,
    mainValue: String,
    subValue: String,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MayaCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, MayaCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = header,
                    tint = TextSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = header,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = mainValue,
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subValue,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun PhoneQuickButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MayaElectricBlue,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

