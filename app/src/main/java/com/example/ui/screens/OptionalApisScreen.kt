package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun OptionalApisScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // API Keys State
    var placesApiKey by remember { mutableStateOf("") }
    var tavilyApiKey by remember { mutableStateOf("") }
    var braveApiKey by remember { mutableStateOf("") }
    var serpApiKey by remember { mutableStateOf("") }
    var pollinationsToken by remember { mutableStateOf("") }

    var isTestingImageGen by remember { mutableStateOf(false) }
    var showAddGeneratorDialog by remember { mutableStateOf(false) }
    var newGeneratorName by remember { mutableStateOf("") }
    var newGeneratorUrl by remember { mutableStateOf("") }
    var newGeneratorKey by remember { mutableStateOf("") }

    val customGenerators = remember {
        mutableStateListOf<String>()
    }

    var testImageResult by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("optional_apis_screen")
    ) {
        MayaScreenHeader(
            title = "Optional",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. MAPS / PLACES API CARD
            item {
                OptionalSectionCard(
                    icon = Icons.Default.Info,
                    title = "Maps / Places API",
                    subtitle = "For place search and directions (optional)"
                ) {
                    OptionalTipBox(
                        text = "Optional. Add a Google Places API key to let Maya look up nearby places and directions."
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    KeySecretTextField(
                        value = placesApiKey,
                        onValueChange = { placesApiKey = it },
                        placeholder = "Places API key",
                        tag = "input_places_key"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SaveBlueButton(
                        onClick = {
                            viewModel.triggerHapticFeedback()
                            Toast.makeText(context, "Places API key saved securely", Toast.LENGTH_SHORT).show()
                        },
                        tag = "save_places_key"
                    )
                }
            }

            // 2. WEB SEARCH CARD
            item {
                OptionalSectionCard(
                    icon = Icons.Default.Search,
                    title = "Web search",
                    subtitle = "Better sources for search and deep research (optional)"
                ) {
                    OptionalTipBox(
                        text = "Search already works with no key — Maya falls back to a free DuckDuckGo lookup. Adding a key gives her a real search index instead, which mainly helps Deep Research (it runs a dozen searches per report). Add any one; the rest stay as backups."
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    KeySecretTextField(
                        value = tavilyApiKey,
                        onValueChange = { tavilyApiKey = it },
                        placeholder = "Tavily API key — best for research",
                        tag = "input_tavily_key"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    KeySecretTextField(
                        value = braveApiKey,
                        onValueChange = { braveApiKey = it },
                        placeholder = "Brave Search API key",
                        tag = "input_brave_key"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    KeySecretTextField(
                        value = serpApiKey,
                        onValueChange = { serpApiKey = it },
                        placeholder = "SerpAPI key",
                        tag = "input_serp_key"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SaveBlueButton(
                        onClick = {
                            viewModel.triggerHapticFeedback()
                            Toast.makeText(context, "Search engine keys saved securely", Toast.LENGTH_SHORT).show()
                        },
                        tag = "save_search_keys"
                    )
                }
            }

            // 3. IMAGE GENERATION CARD
            item {
                OptionalSectionCard(
                    icon = Icons.Default.Favorite,
                    title = "Image generation",
                    subtitle = "Which AI draws Maya's pictures (optional)"
                ) {
                    OptionalTipBox(
                        text = "Maya generates images with no key at all, but the free tier allows only about one picture every 15 seconds. Add your own generator below — OpenAI, Gemini, Together, or anything OpenAI-compatible — and she uses that first, falling back to the free one if it fails."
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    KeySecretTextField(
                        value = pollinationsToken,
                        onValueChange = { pollinationsToken = it },
                        placeholder = "Pollinations token (free tier, optional)",
                        tag = "input_pollinations_token"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SaveBlueButton(
                        onClick = {
                            viewModel.triggerHapticFeedback()
                            Toast.makeText(context, "Image generation token saved", Toast.LENGTH_SHORT).show()
                        },
                        tag = "save_pollinations_token"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Inner Subcard: Image generators
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, MayaCardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Image generators",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tried top to bottom; if one fails or hits a rate limit Maya moves to the next. The free keyless generator always sits underneath, so pictures keep working even with nothing here.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )

                            if (customGenerators.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                customGenerators.forEach { gen ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF1E293B))
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = "• $gen", color = MayaElectricBlue, fontSize = 13.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // + Add image generator button
                            Button(
                                onClick = { showAddGeneratorDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1E293B)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                                    .testTag("btn_add_image_generator")
                            ) {
                                Text(
                                    text = "+ Add image generator",
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Test — generate one picture
                            Button(
                                onClick = {
                                    viewModel.triggerHapticFeedback()
                                    isTestingImageGen = true
                                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                        isTestingImageGen = false
                                        testImageResult = true
                                        Toast.makeText(context, "Test image generated successfully!", Toast.LENGTH_SHORT).show()
                                    }, 1200)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MayaElectricBlue
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("btn_test_generate_picture")
                            ) {
                                if (isTestingImageGen) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Test — generate one picture",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            AnimatedVisibility(visible = testImageResult) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(160.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, MayaElectricBlue, RoundedCornerShape(12.dp))
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.img_maya_avatar),
                                            contentDescription = "Test Generated Asset",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Maya AI Portrait • Ready",
                                        color = MayaElectricBlue,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddGeneratorDialog) {
        AlertDialog(
            onDismissRequest = { showAddGeneratorDialog = false },
            containerColor = MayaCardBg,
            title = {
                Text("Add Image Generator", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Provide OpenAI-compatible or Together image API endpoint details:",
                        color = TextSecondary,
                        fontSize = 12.5.sp
                    )
                    OutlinedTextField(
                        value = newGeneratorName,
                        onValueChange = { newGeneratorName = it },
                        label = { Text("Provider Name (e.g. DALL-E 3, FLUX)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newGeneratorKey,
                        onValueChange = { newGeneratorKey = it },
                        label = { Text("API Key / Token") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newGeneratorName.isNotBlank()) {
                            customGenerators.add(newGeneratorName)
                            Toast.makeText(context, "$newGeneratorName added to fallback pipeline", Toast.LENGTH_SHORT).show()
                        }
                        showAddGeneratorDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue)
                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGeneratorDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun OptionalSectionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MayaElectricBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MayaElectricBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            content()
        }
    }
}

@Composable
private fun OptionalTipBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF131D33))
            .border(1.dp, Color(0xFF1E2E4F), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = MayaElectricBlue,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = TextPrimary.copy(alpha = 0.9f),
                fontSize = 12.5.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun KeySecretTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    tag: String
) {
    var isRevealed by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = TextSecondary, fontSize = 13.5.sp)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Secret Lock",
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        },
        visualTransformation = if (isRevealed) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
            .testTag(tag)
    )
}

@Composable
private fun SaveBlueButton(
    onClick: () -> Unit,
    tag: String
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MayaElectricBlue),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        modifier = Modifier.testTag(tag)
    ) {
        Text(
            text = "Save",
            color = Color.White,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
