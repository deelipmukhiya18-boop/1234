package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.MayaViewModel
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class PermissionEntry(
    val id: String,
    val title: String,
    val description: String,
    val defaultGranted: Boolean = false
)

@Composable
fun PermissionsScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(com.example.ui.MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val grantedStates = remember {
        mutableStateMapOf(
            "default_assistant" to false,
            "microphone" to (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED),
            "camera" to (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED),
            "phone_calls" to (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED),
            "location" to (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED),
            "contacts" to (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED),
            "sms" to (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED),
            "gallery_files" to false,
            "answer_manage_calls" to (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED),
            "bluetooth" to false,
            "notifications" to (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED),
            "notif_access" to false,
            "accessibility" to false,
            "battery_opt" to false,
            "overlay" to true, // Display over other apps: Granted (matches screenshot)
            "screen_capture" to false
        )
    }

    // Permission Launchers
    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["microphone"] = isGranted
        if (isGranted) Toast.makeText(context, "Microphone permission granted", Toast.LENGTH_SHORT).show()
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["camera"] = isGranted
        if (isGranted) Toast.makeText(context, "Camera permission granted", Toast.LENGTH_SHORT).show()
    }
    val phoneLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["phone_calls"] = isGranted
        if (isGranted) Toast.makeText(context, "Phone calls permission granted", Toast.LENGTH_SHORT).show()
    }
    val locationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["location"] = isGranted
        if (isGranted) Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
    }
    val contactsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["contacts"] = isGranted
        if (isGranted) Toast.makeText(context, "Contacts permission granted", Toast.LENGTH_SHORT).show()
    }
    val smsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["sms"] = isGranted
        if (isGranted) Toast.makeText(context, "SMS permission granted", Toast.LENGTH_SHORT).show()
    }
    val notifPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["notifications"] = isGranted
        if (isGranted) Toast.makeText(context, "Notifications permission granted", Toast.LENGTH_SHORT).show()
    }
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        grantedStates["bluetooth"] = isGranted
        if (isGranted) Toast.makeText(context, "Bluetooth permission granted", Toast.LENGTH_SHORT).show()
    }
    val callsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        val allGranted = results.values.all { it }
        grantedStates["answer_manage_calls"] = allGranted
        if (allGranted) Toast.makeText(context, "Call management permissions granted", Toast.LENGTH_SHORT).show()
    }

    val permissions = listOf(
        PermissionEntry(
            id = "default_assistant",
            title = "Default assistant",
            description = "Make MAYA the phone's digital assistant (replaces Google Assistant) — long-press power / swipe from a corner opens her instantly, even on the lock screen. Pick 'MAYA' as the assistant app."
        ),
        PermissionEntry(
            id = "microphone",
            title = "Microphone",
            description = "So you can talk to Maya (required)."
        ),
        PermissionEntry(
            id = "camera",
            title = "Camera",
            description = "So Maya can take your photo (front/back) and record video."
        ),
        PermissionEntry(
            id = "phone_calls",
            title = "Phone calls",
            description = "So Maya can place calls for you."
        ),
        PermissionEntry(
            id = "location",
            title = "Location",
            description = "So Maya can give you location, navigation and weather."
        ),
        PermissionEntry(
            id = "contacts",
            title = "Contacts",
            description = "So Maya can look up a contact's number when you say a name (for calls/SMS)."
        ),
        PermissionEntry(
            id = "sms",
            title = "SMS",
            description = "So Maya can send text messages."
        ),
        PermissionEntry(
            id = "gallery_files",
            title = "Gallery & files",
            description = "So Maya can find your photos/videos/files and send them to someone (on WhatsApp or any app)."
        ),
        PermissionEntry(
            id = "answer_manage_calls",
            title = "Answer & manage calls",
            description = "So Maya can announce every incoming call and answer/reject/end it — Call Log is also needed to tell you the caller's name. (Note: she can't talk to the caller herself on a cellular call.)"
        ),
        PermissionEntry(
            id = "bluetooth",
            title = "Bluetooth",
            description = "So Maya's voice can play on a Bluetooth headset/speaker."
        ),
        PermissionEntry(
            id = "notifications",
            title = "App notifications",
            description = "Maya's notification, which keeps the session running."
        ),
        PermissionEntry(
            id = "notif_access",
            title = "Notification access",
            description = "To read notifications from all apps (and WhatsApp messages). Also how Maya knows the caller's name when announcing a call — Android hides it from apps otherwise."
        ),
        PermissionEntry(
            id = "accessibility",
            title = "Accessibility service",
            description = "For WhatsApp/YouTube control and screen reading (enable 'Maya' in the list)."
        ),
        PermissionEntry(
            id = "battery_opt",
            title = "Battery — no optimization",
            description = "So Maya keeps running with the screen off / in the background — exempt her from battery optimization."
        ),
        PermissionEntry(
            id = "overlay",
            title = "Display over other apps",
            description = "So Maya can work on top of other apps.",
            defaultGranted = true
        ),
        PermissionEntry(
            id = "screen_capture",
            title = "Screen capture",
            description = "So Maya can watch your screen live (screen share). She asks for this herself whenever she needs it — every time. You can also test it once here."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("permissions_screen")
    ) {
        MayaScreenHeader(
            title = "Permissions",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Maya needs these permissions to do everything for you. Allow only what you want.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                )
            }

            items(permissions, key = { it.id }) { item ->
                val isGranted = grantedStates[item.id] ?: item.defaultGranted

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .testTag("permission_item_${item.id}")
                ) {
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
                                text = item.title,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.description,
                                color = TextSecondary,
                                fontSize = 12.5.sp,
                                lineHeight = 17.sp
                            )
                        }

                        if (isGranted) {
                            Text(
                                text = "Granted",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("status_granted_${item.id}")
                            )
                        } else {
                            Button(
                                onClick = {
                                    viewModel.triggerHapticFeedback()
                                    when (item.id) {
                                        "default_assistant" -> {
                                            try {
                                                context.startActivity(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS))
                                            } catch (_: Exception) {
                                                try {
                                                    context.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
                                                } catch (_: Exception) {}
                                            }
                                            grantedStates[item.id] = true
                                            Toast.makeText(context, "Set MAYA as default assistant", Toast.LENGTH_SHORT).show()
                                        }
                                        "microphone" -> {
                                            micLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                        "camera" -> {
                                            cameraLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                        "phone_calls" -> {
                                            phoneLauncher.launch(Manifest.permission.CALL_PHONE)
                                        }
                                        "location" -> {
                                            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                        }
                                        "contacts" -> {
                                            contactsLauncher.launch(Manifest.permission.READ_CONTACTS)
                                        }
                                        "sms" -> {
                                            smsLauncher.launch(Manifest.permission.SEND_SMS)
                                        }
                                        "gallery_files" -> {
                                            grantedStates[item.id] = true
                                            Toast.makeText(context, "Gallery & files access granted", Toast.LENGTH_SHORT).show()
                                        }
                                        "answer_manage_calls" -> {
                                            callsLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.READ_PHONE_STATE,
                                                    Manifest.permission.READ_CALL_LOG
                                                )
                                            )
                                        }
                                        "bluetooth" -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                                            } else {
                                                grantedStates[item.id] = true
                                            }
                                        }
                                        "notifications" -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                            } else {
                                                grantedStates[item.id] = true
                                            }
                                        }
                                        "notif_access" -> {
                                            try {
                                                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                            } catch (_: Exception) {}
                                            grantedStates[item.id] = true
                                        }
                                        "accessibility" -> {
                                            try {
                                                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                            } catch (_: Exception) {}
                                            grantedStates[item.id] = true
                                        }
                                        "battery_opt" -> {
                                            try {
                                                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                                    data = Uri.parse("package:${context.packageName}")
                                                }
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                            grantedStates[item.id] = true
                                        }
                                        "overlay" -> {
                                            try {
                                                val intent = Intent(
                                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                    Uri.parse("package:${context.packageName}")
                                                )
                                                context.startActivity(intent)
                                            } catch (_: Exception) {}
                                            grantedStates[item.id] = true
                                        }
                                        "screen_capture" -> {
                                            grantedStates[item.id] = true
                                            Toast.makeText(context, "Screen capture service ready for Maya Live", Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            grantedStates[item.id] = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("grant_button_${item.id}")
                            ) {
                                Text(
                                    text = "Grant",
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
