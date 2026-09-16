package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.ConnectorItem
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.components.MayaScreenHeader
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ConnectorsScreen(
    viewModel: MayaViewModel,
    onBack: () -> Unit = { viewModel.setTab(MayaNavTab.HOME) },
    modifier: Modifier = Modifier
) {
    val connectors by viewModel.connectors.collectAsStateWithLifecycle()
    val searchQuery by viewModel.connectorSearchQuery.collectAsStateWithLifecycle()
    val activeCategory by viewModel.connectorCategoryFilter.collectAsStateWithLifecycle()

    var selectedConnectorForConfig by remember { mutableStateOf<ConnectorItem?>(null) }

    val categories = listOf("All", "Files", "Code", "Notes & tasks", "Messages")

    val filteredConnectors = connectors.filter { item ->
        val matchesCategory = (activeCategory == "All" || item.category.equals(activeCategory, ignoreCase = true))
        val matchesSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MayaDarkBg)
            .testTag("connectors_screen")
    ) {
        MayaScreenHeader(
            title = "Connectors",
            onBack = onBack,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. SEARCH BAR
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setConnectorSearch(it) },
                    placeholder = { Text("Search connectors", color = TextMuted, fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("connector_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MayaElectricBlue,
                        unfocusedBorderColor = MayaCardBorder,
                        focusedContainerColor = MayaCardBg,
                        unfocusedContainerColor = MayaCardBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            }

            // 2. CATEGORY CHIPS
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == activeCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.08f))
                                .border(
                                    1.dp,
                                    if (isSelected) MayaElectricBlue else Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.setConnectorCategory(cat) }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("connector_cat_${cat.lowercase().replace(" ", "_")}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // 3. CONNECTORS LIST ITEMS
            items(filteredConnectors) { connector ->
                ConnectorRowCard(
                    connector = connector,
                    onClick = { selectedConnectorForConfig = connector },
                    onToggle = { viewModel.toggleConnector(connector.id) }
                )
            }

            // 4. TIP BOX
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF131A29))
                        .border(1.dp, Color(0xFF1E2D48), RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Tip",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Connector credentials stay on this phone, encrypted. Maya never sends them anywhere, and disconnecting deletes them.",
                        color = TextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // CONFIG DIALOG
    selectedConnectorForConfig?.let { connector ->
        var tokenInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { selectedConnectorForConfig = null },
            containerColor = MayaCardBg,
            title = {
                Text(
                    text = "${connector.name} Integration",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = connector.description,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (connector.isConnected) "Status: Connected & Ready" else "Status: Disconnected",
                        color = if (connector.isConnected) Color(0xFF22C55E) else TextMuted,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = tokenInput,
                        onValueChange = { tokenInput = it },
                        label = { Text("API Key / Personal Access Token") },
                        placeholder = { Text("Paste secret token here") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MayaElectricBlue,
                            unfocusedBorderColor = MayaCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.toggleConnector(connector.id)
                        selectedConnectorForConfig = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (connector.isConnected) Color(0xFFEF4444) else MayaElectricBlue
                    )
                ) {
                    Text(if (connector.isConnected) "Disconnect" else "Save & Connect")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedConnectorForConfig = null }) {
                    Text("Close", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ConnectorRowCard(
    connector: ConnectorItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MayaCardBg)
            .border(1.dp, MayaCardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("connector_row_${connector.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand logo icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(getConnectorColor(connector.iconKey).copy(alpha = 0.15f))
                .border(1.dp, getConnectorColor(connector.iconKey).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = connector.name.take(1),
                color = getConnectorColor(connector.iconKey),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = connector.name,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = connector.description,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Connect/Connected button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(if (connector.isConnected) Color(0xFF132B20) else Color.White.copy(alpha = 0.08f))
                .border(
                    1.dp,
                    if (connector.isConnected) Color(0xFF22C55E).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f),
                    RoundedCornerShape(18.dp)
                )
                .clickable(onClick = onToggle)
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .testTag("connector_btn_${connector.id}"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (connector.isConnected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Connected",
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = if (connector.isConnected) "Connected" else "Connect",
                    color = if (connector.isConnected) Color(0xFF22C55E) else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getConnectorColor(key: String): Color {
    return when (key) {
        "gdrive" -> Color(0xFF34A853)
        "github" -> Color(0xFFE6EDF3)
        "vercel" -> Color(0xFFF9FAFB)
        "notion" -> Color(0xFFEA580C)
        "telegram" -> Color(0xFF0EA5E9)
        "todoist" -> Color(0xFFDC2626)
        "gitlab" -> Color(0xFFF97316)
        "linear" -> Color(0xFF6366F1)
        else -> MayaElectricBlue
    }
}
