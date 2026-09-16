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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaAmber
import com.example.ui.theme.MayaBlack
import com.example.ui.theme.MayaBlue
import com.example.ui.theme.MayaBorder
import com.example.ui.theme.MayaCardDark
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaCoral
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.MayaRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class ToolCategory(val name: String, val count: Int, val description: String)

@Composable
fun MemoryToolsScreen(
    viewModel: MayaViewModel,
    modifier: Modifier = Modifier
) {
    val memories by viewModel.memories.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var newKey by remember { mutableStateOf("") }
    var newFact by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Personal") }

    val categories = listOf(
        ToolCategory("Core essentials", 68, "Always-on system commands, voice navigation, app routing"),
        ToolCategory("PC control", 25, "Hotkeys, windows, PowerShell, volume, restart"),
        ToolCategory("Accessibility", 25, "Hands-free web & desktop controls, click by number"),
        ToolCategory("Macros", 13, "Teach-by-doing routine automations"),
        ToolCategory("Documents", 12, "Word docs, PDFs, spreadsheets, resumes"),
        ToolCategory("Skills engine", 10, "Runtime Python self-authored skills"),
        ToolCategory("YouTube", 9, "Search, play, download, upload with metadata"),
        ToolCategory("WhatsApp", 8, "Message contacts, attach files, read unread chats"),
        ToolCategory("Analytics", 8, "Channel metrics, sentiment mining, viewer retention"),
        ToolCategory("Voice security", 8, "Voiceprint guardian, guest restrictions"),
        ToolCategory("Music", 8, "Media controls, favorites, taste profiling"),
        ToolCategory("Study & whiteboard", 6, "Interactive drawing diagrams and flowcharts"),
        ToolCategory("Email", 4, "Summarize unread mail, voice dictation, spam cleaning"),
        ToolCategory("Camera & vision", 4, "Webcam view, face recognition, scene description"),
        ToolCategory("Screen recording", 4, "Start, pause, review screen recording on command"),
        ToolCategory("Clipboard & Excel", 3, "Clipboard history, live spreadsheet modifications"),
        ToolCategory("Coding agent", 3, "Claude Code & HunterCode integrated in VS Code"),
        ToolCategory("Finance", 3, "Live stock tickers, price history readout"),
        ToolCategory("Plugins", 3, "Developer Python plugin runtime"),
        ToolCategory("Hermes agent", 3, "Autonomous reasoning agent from Nous Research"),
        ToolCategory("Expense tracker", 2, "Receipt reading from bank mails & SMS"),
        ToolCategory("Weather & smart home", 2, "Automatic localized forecasts"),
        ToolCategory("Live commentary", 2, "Real-time stream co-hosting and gameplay reaction"),
        ToolCategory("Support", 1, "Diagnostic self-repair and log analysis")
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

            // Section Header
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "MAYA PERSISTENT MEMORY & TOOLS",
                    color = MayaRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Memory That Persists",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Facts about you, your projects, ongoing work — carried across sessions in local Room DB.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Memory Toggle Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_memory_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Brain",
                                tint = MayaAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TEACH MAYA A FACT",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { showAddDialog = !showAddDialog },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MayaCardHighlight),
                            modifier = Modifier.testTag("toggle_teach_button")
                        ) {
                            Text(if (showAddDialog) "Cancel" else "+ Add Fact", color = MayaRed, fontSize = 11.sp)
                        }
                    }

                    if (showAddDialog) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newKey,
                            onValueChange = { newKey = it },
                            placeholder = { Text("Topic / Tag (e.g. Flight preference)", color = TextSecondary) },
                            modifier = Modifier.fillMaxWidth().testTag("new_memory_key"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MayaCardHighlight,
                                unfocusedContainerColor = MayaCardHighlight,
                                focusedBorderColor = MayaRed,
                                unfocusedBorderColor = MayaBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newFact,
                            onValueChange = { newFact = it },
                            placeholder = { Text("Fact text (e.g. Prefers window seats and Indigo flights)", color = TextSecondary) },
                            modifier = Modifier.fillMaxWidth().testTag("new_memory_text"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MayaCardHighlight,
                                unfocusedContainerColor = MayaCardHighlight,
                                focusedBorderColor = MayaRed,
                                unfocusedBorderColor = MayaBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (newKey.isNotBlank() && newFact.isNotBlank()) {
                                    viewModel.addCustomMemory(newCategory, newKey.trim(), newFact.trim())
                                    newKey = ""
                                    newFact = ""
                                    showAddDialog = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("save_memory_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MayaRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save To Local Memory", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "STORED FACTS (${memories.size})",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))
        }

        // List of Stored Memories
        items(memories) { memory ->
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MayaBlue.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = memory.category,
                                    color = MayaBlue,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = memory.keyTag,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = memory.factText,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.deleteMemory(memory) },
                        modifier = Modifier.size(28.dp).testTag("delete_memory_${memory.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Forget Fact",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))

            // 234 Tools / 24 Categories Breakdown (Page 3 of brochure)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "234 BUILT-IN VOICE TOOLS",
                        color = MayaRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "24 Categories",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "Everything a voice can reach in Windows 11 & Android",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(categories) { cat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MayaCardDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, MayaBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cat.name,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = cat.description,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MayaCardHighlight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${cat.count} tools",
                            color = MayaCoral,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
