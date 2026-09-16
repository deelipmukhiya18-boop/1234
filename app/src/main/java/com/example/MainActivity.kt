package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MayaNavTab
import com.example.ui.MayaViewModel
import com.example.ui.components.ActivateLicenseDialog
import com.example.ui.components.MayaDrawerContent
import com.example.ui.components.QuickToolModal
import com.example.ui.screens.AdvancedScreen
import com.example.ui.screens.AppearanceScreen
import com.example.ui.screens.BackupScreen
import com.example.ui.screens.BehaviourScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ConnectorsScreen
import com.example.ui.screens.EmailScreen
import com.example.ui.screens.EmergencySosScreen
import com.example.ui.screens.EventTriggersScreen
import com.example.ui.screens.GroupsReportsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MayaAssistantSettingsScreen
import com.example.ui.screens.MayaRulesScreen
import com.example.ui.screens.MemoryToolsScreen
import com.example.ui.screens.OptionalApisScreen
import com.example.ui.screens.PairingScreen
import com.example.ui.screens.PatternPinScreen
import com.example.ui.screens.PermissionsScreen
import com.example.ui.screens.PersonalScreen
import com.example.ui.screens.PhoneLinkScreen
import com.example.ui.screens.ScreenLockScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.SocialMediaScreen
import com.example.ui.screens.SubAgentsScreen
import com.example.ui.screens.ThemeScreen
import com.example.ui.screens.TouchGuardScreen
import com.example.ui.screens.TypingScreen
import com.example.ui.screens.VoiceGuardianScreen
import com.example.ui.screens.VoiceHudScreen
import com.example.ui.screens.WhatsAppAutoReplyScreen
import com.example.ui.theme.MayaCardBg
import com.example.ui.theme.MayaCardBorder
import com.example.ui.theme.MayaDarkBg
import com.example.ui.theme.MayaDarkSurface
import com.example.ui.theme.MayaElectricBlue
import com.example.ui.theme.MayaTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MayaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MayaTheme {
                val permissionsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { /* permissions evaluated gracefully */ }

                LaunchedEffect(Unit) {
                    permissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA
                        )
                    )
                }

                MayaApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MayaApp(viewModel: MayaViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val showActivateDialog by viewModel.showActivateDialog.collectAsStateWithLifecycle()
    val activeQuickTool by viewModel.activeQuickTool.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MayaDarkSurface,
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                MayaDrawerContent(
                    viewModel = viewModel,
                    onNavigate = { tab ->
                        viewModel.setTab(tab)
                    },
                    onClose = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = MayaDarkBg,
            contentColor = TextPrimary,
            bottomBar = {
                // Bottom Navigation Bar matching Screenshot 1 & 2
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .background(MayaDarkBg)
                ) {
                    NavigationBar(
                        containerColor = MayaDarkBg,
                        contentColor = TextPrimary,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .testTag("bottom_navigation_bar")
                    ) {
                        // 1. Home
                        NavigationBarItem(
                            selected = currentTab == MayaNavTab.HOME,
                            onClick = { viewModel.setTab(MayaNavTab.HOME) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Home", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MayaElectricBlue,
                                selectedTextColor = MayaElectricBlue,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_home")
                        )

                        // 2. Scan
                        NavigationBarItem(
                            selected = currentTab == MayaNavTab.SCAN || currentTab == MayaNavTab.QR_PAIR,
                            onClick = { viewModel.setTab(MayaNavTab.SCAN) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.CropFree,
                                    contentDescription = "Scan",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Scan", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MayaElectricBlue,
                                selectedTextColor = MayaElectricBlue,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_scan")
                        )

                        // 3. Elevated Center Mic Button (Spacer for layout)
                        Spacer(modifier = Modifier.weight(1f))

                        // 4. Memories
                        NavigationBarItem(
                            selected = currentTab == MayaNavTab.MEMORIES || currentTab == MayaNavTab.MEMORY_VAULT,
                            onClick = { viewModel.setTab(MayaNavTab.MEMORIES) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Memories",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Memories", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MayaElectricBlue,
                                selectedTextColor = MayaElectricBlue,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_memories")
                        )

                        // 5. Chat
                        NavigationBarItem(
                            selected = currentTab == MayaNavTab.CHAT,
                            onClick = { viewModel.setTab(MayaNavTab.CHAT) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.ChatBubble,
                                    contentDescription = "Chat",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text("Chat", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MayaElectricBlue,
                                selectedTextColor = MayaElectricBlue,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_chat")
                        )
                    }

                    // ELEVATED CENTER MIC BUTTON (Exact match to Screenshot 1 & 2)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-14).dp)
                            .size(56.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF1D4ED8)
                                    )
                                )
                            )
                            .border(2.dp, Color(0xFF60A5FA).copy(alpha = 0.5f), CircleShape)
                            .clickable {
                                viewModel.toggleVoiceListening()
                                viewModel.setTab(MayaNavTab.VOICE_CORE)
                            }
                            .testTag("center_mic_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Maya Voice Trigger",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MayaDarkBg)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                when (currentTab) {
                    MayaNavTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                    MayaNavTab.SCAN, MayaNavTab.QR_PAIR -> PairingScreen(viewModel = viewModel)
                    MayaNavTab.MEMORIES, MayaNavTab.MEMORY_VAULT -> MemoryToolsScreen(viewModel = viewModel)
                    MayaNavTab.CHAT -> ChatScreen(viewModel = viewModel)
                    MayaNavTab.MAYA_RULES -> MayaRulesScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.PHONE_LINK -> PhoneLinkScreen(viewModel = viewModel)
                    MayaNavTab.PERMISSIONS -> PermissionsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.EVENT_TRIGGERS -> EventTriggersScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.OPTIONAL_SETTINGS -> OptionalApisScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.WHATSAPP_AUTO_REPLY -> WhatsAppAutoReplyScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.SCREEN_LOCK -> ScreenLockScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.PATTERN_PIN -> PatternPinScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SCREEN_LOCK) }
                    )
                    MayaNavTab.TOUCH_GUARD -> TouchGuardScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.EMERGENCY_SOS -> EmergencySosScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.VOICE_GUARDIAN -> VoiceGuardianScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.VOICE_CORE -> VoiceHudScreen(viewModel = viewModel)
                    MayaNavTab.ADVANCED -> AdvancedScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.THEME -> ThemeScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.ADVANCED) }
                    )
                    MayaNavTab.APPEARANCE -> AppearanceScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.ADVANCED) }
                    )
                    MayaNavTab.BEHAVIOUR -> BehaviourScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.ADVANCED) }
                    )
                    MayaNavTab.TYPING -> TypingScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.ADVANCED) }
                    )
                    MayaNavTab.CONNECTORS -> ConnectorsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.GROUPS_REPORTS -> GroupsReportsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.EMAIL -> EmailScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.SETTINGS -> SettingsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.HOME) }
                    )
                    MayaNavTab.MAYA_ASSISTANT -> MayaAssistantSettingsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.SKILLS -> SkillsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.PERSONAL -> PersonalScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.SUB_AGENTS -> SubAgentsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.SOCIAL_MEDIA -> SocialMediaScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                    MayaNavTab.BACKUP -> BackupScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.setTab(MayaNavTab.SETTINGS) }
                    )
                }
            }
        }
    }

    // MODAL DIALOGS
    if (showActivateDialog) {
        ActivateLicenseDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setActivateDialog(false) }
        )
    }

    if (activeQuickTool != null) {
        QuickToolModal(
            toolName = activeQuickTool!!,
            viewModel = viewModel,
            onDismiss = { viewModel.closeQuickTool() }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
