package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.MayaViewModel
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaCardHighlight
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val sender: String, // "User" or "Maya"
    val text: String,
    val time: String = "Just now"
)

@Composable
fun ChatScreen(
    viewModel: MayaViewModel,
    modifier: Modifier = Modifier
) {
    val lastReply by viewModel.lastMayaReply.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()

    var inputMsg by remember { mutableStateOf("") }
    val messageList = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "Maya",
                text = "Hello there! I'm Maya, your AI companion. You can talk to me, ask me to control your phone, or run desktop commands."
            )
        )
    }

    val quickQuestions = listOf(
        "Check my phone's battery & Wi-Fi",
        "Did I receive any OTP?",
        "Open WhatsApp on my phone",
        "Set a rule: keep answers short"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("chat_screen")
    ) {
        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_maya_avatar),
                        contentDescription = "Maya",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Maya Chat",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isSpeaking) "Speaking..." else "Gemini Live Assistant • Online",
                        color = if (isSpeaking) MayaElectricBlue else TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = {
                    messageList.clear()
                    messageList.add(
                        ChatMessage(
                            sender = "Maya",
                            text = "Chat cleared. What can I help you with?"
                        )
                    )
                },
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear Chat",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CHAT MESSAGES LIST
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messageList, key = { it.id }) { msg ->
                val isUser = msg.sender == "User"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MayaElectricBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_maya_avatar),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp).clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                )
                            )
                            .background(if (isUser) MayaElectricBlue else MayaCardBg)
                            .border(1.dp, if (isUser) MayaElectricBlue else MayaCardBorder, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .fillMaxWidth(0.82f)
                    ) {
                        Column {
                            Text(
                                text = msg.text,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            if (!isUser) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Replay Speech",
                                        tint = TextSecondary,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { viewModel.testTtsVoice() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // QUICK QUESTION CHIPS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickQuestions.take(2).forEach { q ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MayaCardBg)
                        .border(1.dp, MayaCardBorder, RoundedCornerShape(12.dp))
                        .clickable {
                            messageList.add(ChatMessage(sender = "User", text = q))
                            viewModel.submitSpokenPrompt(q)
                            messageList.add(ChatMessage(sender = "Maya", text = "Executed: $q. Check Phone Link & logs."))
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = q,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
        }

        // BOTTOM CHAT INPUT BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(MayaCardBg)
                .border(1.dp, MayaCardBorder, RoundedCornerShape(26.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.openQuickTool("Attachments") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach File",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                BasicTextField(
                    value = inputMsg,
                    onValueChange = { inputMsg = it },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                    cursorBrush = SolidColor(MayaElectricBlue),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputMsg.isNotBlank()) {
                                val text = inputMsg
                                inputMsg = ""
                                messageList.add(ChatMessage(sender = "User", text = text))
                                viewModel.submitSpokenPrompt(text)
                                messageList.add(
                                    ChatMessage(
                                        sender = "Maya",
                                        text = "Processed: \"$text\". Live synchronization active."
                                    )
                                )
                            }
                        }
                    ),
                    decorationBox = { inner ->
                        if (inputMsg.isEmpty()) {
                            Text("Type message to Maya...", color = TextMuted, fontSize = 13.sp)
                        }
                        inner()
                    }
                )

                IconButton(
                    onClick = {
                        if (inputMsg.isNotBlank()) {
                            val text = inputMsg
                            inputMsg = ""
                            messageList.add(ChatMessage(sender = "User", text = text))
                            viewModel.submitSpokenPrompt(text)
                            messageList.add(
                                ChatMessage(
                                    sender = "Maya",
                                    text = "Processed: \"$text\". Live synchronization active."
                                )
                            )
                        }
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputMsg.isNotBlank()) MayaElectricBlue else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
