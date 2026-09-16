package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.MayaEnergyGold
import com.example.ui.theme.MayaGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuickToolModal(
    toolName: String,
    viewModel: MayaViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MayaCardBg,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (toolName) {
                        "Markets" -> Icons.Default.TrendingUp
                        "Documents" -> Icons.Default.Description
                        "Website / Coding" -> Icons.Default.Code
                        "Study / Whiteboard", "Study", "Journal" -> Icons.Default.Edit
                        "Music" -> Icons.Default.MusicNote
                        "Energy" -> Icons.Default.Bolt
                        else -> Icons.Default.Info
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = toolName,
                        tint = MayaElectricBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = toolName,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (toolName) {
                    "Markets" -> {
                        Text(
                            text = "Live Financial Intelligence & Asset Tracking",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MarketRow("BTC / USDT", "$64,320.00", "+3.4%", true)
                        MarketRow("ETH / USDT", "$3,485.50", "+2.1%", true)
                        MarketRow("NIFTY 50", "24,982.10", "+0.7%", true)
                        MarketRow("Gold (XAU)", "$2,584.20", "+0.4%", true)
                    }
                    "Documents" -> {
                        Text(
                            text = "OCR Reader, Smart Summarizer & PDF Vault",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        DocumentItem("Meeting_Notes_Sept.pdf", "AI Summary ready • 4 pages")
                        DocumentItem("Invoice_HDFC_0982.pdf", "Auto-filed to Finance • Verified")
                        DocumentItem("Project_Maya_Specs.md", "Synced with Desktop PC Link")
                    }
                    "Website / Coding" -> {
                        Text(
                            text = "AI Coding Agent & Multi-language Execution",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CodeSnippetBox("Kotlin / Jetpack Compose", "val maya = MayaCompanion(mode = AI_LIVE)")
                        Text(
                            text = "Maya can generate, refactor, and review code on your linked PC directly through voice commands.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    "Music" -> {
                        Text(
                            text = "Maya Lo-Fi & Focus Ambient Stream",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        MusicPlayerWidget(viewModel)
                    }
                    "Study / Whiteboard", "Study" -> {
                        Text(
                            text = "Deep Focus Timer & Whiteboard Canvas",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        StudyTimerWidget()
                    }
                    "Journal" -> {
                        Text(
                            text = "Personal Reflection & Mood Log",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        JournalWidget()
                    }
                    "Energy" -> {
                        Text(
                            text = "Maya Neural Compute Energy: 1 Token",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        EnergyWidget(viewModel, onDismiss)
                    }
                    else -> {
                        Text(
                            text = "Maya v4.15.1 by The Hunter AI.\nBuilt with high-speed P2P WebRTC link, Gemini Live intelligence, and local Room DB vault.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Done", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun MarketRow(pair: String, price: String, change: String, isUp: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MayaCardBorder.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(pair, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(price, color = TextSecondary, fontSize = 11.sp)
        }
        Text(
            text = change,
            color = if (isUp) MayaGreen else MayaElectricBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DocumentItem(name: String, status: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MayaCardBorder.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(status, color = TextSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun CodeSnippetBox(lang: String, snippet: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MayaCardBorder.copy(alpha = 0.4f))
            .padding(10.dp)
    ) {
        Text(lang, color = MayaElectricBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(snippet, color = TextPrimary, fontSize = 11.sp)
    }
}

@Composable
private fun MusicPlayerWidget(viewModel: MayaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MayaCardBorder.copy(alpha = 0.35f))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Midnight Synth & Chill Beats", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text("Maya Ambient Audio Engine", color = TextSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { viewModel.testTtsVoice() },
            colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Play Ambient Audio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StudyTimerWidget() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MayaCardBorder.copy(alpha = 0.35f))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("25:00", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Pomodoro Focus Session", color = TextSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Maya keeps your notifications muted during focus blocks.", color = TextMuted, fontSize = 11.sp)
    }
}

@Composable
private fun JournalWidget() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MayaCardBorder.copy(alpha = 0.35f))
            .padding(12.dp)
    ) {
        Text("Today's Reflection", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Tell Maya: 'Note down that today we achieved the Android app layout target.'", color = TextSecondary, fontSize = 11.sp)
    }
}

@Composable
private fun EnergyWidget(viewModel: MayaViewModel, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MayaCardBorder.copy(alpha = 0.35f))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = MayaEnergyGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Active Balance: 1 Energy", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text("Energy powers autonomous web scrapers, deep code reviews, and remote phone automations.", color = TextSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                viewModel.setActivateDialog(true)
                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MayaEnergyGold),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recharge Energy Tokens", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
