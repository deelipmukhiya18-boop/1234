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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.ReportFormat
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GroupsReportsScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val whatsAppGroups by viewModel.whatsAppGroups.collectAsStateWithLifecycle()
    val reportFormats by viewModel.reportFormats.collectAsStateWithLifecycle()
    val statusMessage by viewModel.groupReportStatusMessage.collectAsStateWithLifecycle()

    var newGroupName by remember { mutableStateOf("") }
    var showCreateFormatDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("groups_reports_screen")
    ) {
        MayaScreenHeader(
            title = "Groups & reports",
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
            // STATUS BANNER
            if (statusMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B2A1E))
                        .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = statusMessage!!,
                            color = Color(0xFF4ADE80),
                            fontSize = 12.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearGroupReportStatus() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = TextMuted)
                        }
                    }
                }
            }

            // CARD 1: YOUR WHATSAPP GROUPS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Your WhatsApp groups",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Chats Maya must treat as a group",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Warning Box ⚠️
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF291F14))
                        .border(1.dp, Color(0xFF854D0E), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Maya never posts in a group without reading the exact message to you first and getting a clear yes. That can't be switched off — a message in a work group can't be unsent.",
                        color = Color(0xFFFDE68A),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (whatsAppGroups.isEmpty()) {
                    Text(
                        text = "No groups added yet. Add the ones you send reports to — spell the name exactly as WhatsApp shows it.",
                        color = TextMuted,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        whatsAppGroups.forEach { group ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF131A29))
                                    .border(1.dp, MayaCardBorder, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = group,
                                    color = TextPrimary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                IconButton(
                                    onClick = { viewModel.removeWhatsAppGroup(group) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete group",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Add group input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        placeholder = { Text("Group name in WhatsApp", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("new_group_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder,
                            focusedContainerColor = Color(0xFF0F1420),
                            unfocusedContainerColor = Color(0xFF0F1420),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (newGroupName.isNotBlank()) {
                                viewModel.addWhatsAppGroup(newGroupName)
                                newGroupName = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("add_group_button")
                    ) {
                        Text("+ Add", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // CARD 2: REPORT FORMATS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MayaCardBg)
                    .border(1.dp, MayaCardBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Report formats",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${reportFormats.size}/20 saved",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { showCreateFormatDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("new_format_button")
                    ) {
                        Text("+ New format", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Save the message your work group expects, with {curly braces} where the values change. Then just say \"us group me report daal do\" — Maya asks for the values, fills them in, and shows you the message before sending.",
                    color = TextSecondary,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    reportFormats.forEach { format ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F1420))
                                .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                                .testTag("report_format_${format.id}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = format.title,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Target: ${format.groupTarget}",
                                        color = MayaElectricBlue,
                                        fontSize = 11.5.sp
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    IconButton(
                                        onClick = { viewModel.testSendReport(format) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "Test preview",
                                            tint = Color(0xFF22C55E),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.removeReportFormat(format.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete format",
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = format.template,
                                color = TextMuted,
                                fontSize = 11.5.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 15.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF070A10))
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // CREATE FORMAT DIALOG
    if (showCreateFormatDialog) {
        var formatTitle by remember { mutableStateOf("") }
        var formatTarget by remember { mutableStateOf(whatsAppGroups.firstOrNull() ?: "General") }
        var formatTemplate by remember { mutableStateOf("Status Report:\nDone: {completed}\nNext: {plan}\nBlockers: {none}") }

        AlertDialog(
            onDismissRequest = { showCreateFormatDialog = false },
            containerColor = MayaCardBg,
            title = {
                Text("New Report Format", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formatTitle,
                        onValueChange = { formatTitle = it },
                        label = { Text("Format Name") },
                        placeholder = { Text("e.g. Daily Standup") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formatTarget,
                        onValueChange = { formatTarget = it },
                        label = { Text("Target Group Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = formatTemplate,
                        onValueChange = { formatTemplate = it },
                        label = { Text("Template (use {curly_braces})") },
                        minLines = 4,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (formatTitle.isNotBlank() && formatTemplate.isNotBlank()) {
                            viewModel.addReportFormat(formatTitle, formatTemplate, formatTarget)
                            showCreateFormatDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue)
                ) {
                    Text("Save Format")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFormatDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
