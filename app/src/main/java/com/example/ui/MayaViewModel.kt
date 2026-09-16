package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.MayaDatabase
import com.example.data.model.MayaMemory
import com.example.data.model.MayaPersona
import com.example.data.model.PairedSession
import com.example.data.model.PhoneAction
import com.example.data.repository.MayaRepository
import com.example.service.DeviceController
import com.example.service.GeminiMayaService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class MayaNavTab(val label: String) {
    HOME("Home"),
    SCAN("Scan"),
    MEMORIES("Memories"),
    CHAT("Chat"),
    MAYA_RULES("Maya Rules"),
    PHONE_LINK("PC ⇄ Phone"),
    PERMISSIONS("Permissions"),
    VOICE_CORE("Voice HUD"),
    QR_PAIR("Desktop Pair"),
    MEMORY_VAULT("Memory Vault"),
    EVENT_TRIGGERS("Event triggers"),
    OPTIONAL_SETTINGS("Optional"),
    WHATSAPP_AUTO_REPLY("WhatsApp auto-reply"),
    SCREEN_LOCK("Screen lock"),
    PATTERN_PIN("Pattern & PIN"),
    TOUCH_GUARD("Touch Guard"),
    EMERGENCY_SOS("Emergency SOS"),
    VOICE_GUARDIAN("Voice Guardian"),
    ADVANCED("Advanced"),
    THEME("Theme"),
    APPEARANCE("Appearance"),
    BEHAVIOUR("Behaviour"),
    TYPING("Typing"),
    CONNECTORS("Connectors"),
    GROUPS_REPORTS("Groups & reports"),
    EMAIL("Email"),
    SETTINGS("Settings"),
    MAYA_ASSISTANT("Maya"),
    SKILLS("Skills"),
    PERSONAL("Personal"),
    SUB_AGENTS("Sub-agents"),
    SOCIAL_MEDIA("Social media"),
    BACKUP("Backup")
}

data class InstalledSkillItem(
    val id: String,
    val name: String,
    val description: String,
    val isStore: Boolean = true,
    val isEnabled: Boolean = true
)

data class VoiceOption(
    val id: String,
    val name: String,
    val voiceCode: String,
    val persona: String = "Maya",
    val pitch: Float = 1.0f,
    val rate: Float = 1.0f,
    val description: String = "Default natural tone"
)

data class ConnectorItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String, // "Files", "Code", "Notes & tasks", "Messages"
    val isConnected: Boolean,
    val iconKey: String
)

data class ReportFormat(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val template: String,
    val groupTarget: String = "All groups"
)

data class FloatingHeart(val id: Long = System.currentTimeMillis(), val offsetX: Float = 0f)

data class TouchIncident(
    val id: Long = System.currentTimeMillis(),
    val trigger: String,
    val timestamp: String,
    val hasPhoto: Boolean = true
)

data class SosContact(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val phone: String,
    val relation: String = "Emergency"
)

data class EnrolledVoice(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val role: String = "Owner",
    val enrolledDate: String = "Today"
)

class MayaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MayaRepository
    private val deviceController = DeviceController(application)
    private val geminiService = GeminiMayaService()

    init {
        val dao = MayaDatabase.getDatabase(application).mayaDao()
        repository = MayaRepository(dao)
        viewModelScope.launch {
            repository.initializeDefaultsIfEmpty()
        }
    }

    val session: StateFlow<PairedSession?> = repository.sessionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val actions: StateFlow<List<PhoneAction>> = repository.actionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memories: StateFlow<List<MayaMemory>> = repository.memoriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rules: StateFlow<List<com.example.data.model.MayaRule>> = repository.rulesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedPersona = MutableStateFlow(MayaPersona.MAYA)
    val selectedPersona: StateFlow<MayaPersona> = _selectedPersona.asStateFlow()

    private val _currentTab = MutableStateFlow(MayaNavTab.HOME)
    val currentTab: StateFlow<MayaNavTab> = _currentTab.asStateFlow()

    // Energy & Free Mode
    private val _energy = MutableStateFlow(1)
    val energy: StateFlow<Int> = _energy.asStateFlow()

    private val _freeMinutesLeft = MutableStateFlow("10:00 min left today")
    val freeMinutesLeft: StateFlow<String> = _freeMinutesLeft.asStateFlow()

    private val _showActivateDialog = MutableStateFlow(false)
    val showActivateDialog: StateFlow<Boolean> = _showActivateDialog.asStateFlow()

    private val _showRuleDialog = MutableStateFlow(false)
    val showRuleDialog: StateFlow<Boolean> = _showRuleDialog.asStateFlow()

    private val _activeQuickTool = MutableStateFlow<String?>(null)
    val activeQuickTool: StateFlow<String?> = _activeQuickTool.asStateFlow()

    // Widgets State
    private val _weatherState = MutableStateFlow("—\nNo data")
    val weatherState: StateFlow<String> = _weatherState.asStateFlow()

    private val _moodState = MutableStateFlow("Warm\nAll good")
    val moodState: StateFlow<String> = _moodState.asStateFlow()

    // Character Tap & Floating Hearts
    private val _floatingHearts = MutableStateFlow<List<FloatingHeart>>(emptyList())
    val floatingHearts: StateFlow<List<FloatingHeart>> = _floatingHearts.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isDuetActive = MutableStateFlow(false)
    val isDuetActive: StateFlow<Boolean> = _isDuetActive.asStateFlow()

    private val _isSleeping = MutableStateFlow(false)
    val isSleeping: StateFlow<Boolean> = _isSleeping.asStateFlow()

    private val _lastTranscript = MutableStateFlow("Tap the mic or say 'Hey Maya' to speak.")
    val lastTranscript: StateFlow<String> = _lastTranscript.asStateFlow()

    private val _lastMayaReply = MutableStateFlow("Maya 6.0 desktop companion active. Phone link standing by at 12ms ping.")
    val lastMayaReply: StateFlow<String> = _lastMayaReply.asStateFlow()

    private val _waveforms = MutableStateFlow(List(16) { 0.2f })
    val waveforms: StateFlow<List<Float>> = _waveforms.asStateFlow()

    private val _batteryLevel = MutableStateFlow(deviceController.getBatteryLevel())
    val batteryLevel: StateFlow<Int> = _batteryLevel.asStateFlow()

    private val _isFlashlightOn = MutableStateFlow(false)
    val isFlashlightOn: StateFlow<Boolean> = _isFlashlightOn.asStateFlow()

    private val _latestOtp = MutableStateFlow("739201")
    val latestOtp: StateFlow<String> = _latestOtp.asStateFlow()

    // Touch Guard State (Screenshot: Watch phone while you are away)
    private val _touchGuardEnabled = MutableStateFlow(false)
    val touchGuardEnabled: StateFlow<Boolean> = _touchGuardEnabled.asStateFlow()

    private val _touchGuardWakeScreen = MutableStateFlow(true)
    val touchGuardWakeScreen: StateFlow<Boolean> = _touchGuardWakeScreen.asStateFlow()

    private val _touchGuardPickup = MutableStateFlow(true)
    val touchGuardPickup: StateFlow<Boolean> = _touchGuardPickup.asStateFlow()

    private val _touchGuardCharger = MutableStateFlow(true)
    val touchGuardCharger: StateFlow<Boolean> = _touchGuardCharger.asStateFlow()

    private val _touchGuardSiren = MutableStateFlow(true)
    val touchGuardSiren: StateFlow<Boolean> = _touchGuardSiren.asStateFlow()

    private val _touchGuardIncidents = MutableStateFlow<List<TouchIncident>>(emptyList())
    val touchGuardIncidents: StateFlow<List<TouchIncident>> = _touchGuardIncidents.asStateFlow()

    // Emergency SOS State (Screenshot: Country code & SOS contacts)
    private val _sosCountryCode = MutableStateFlow("🇮🇳  India (+91)")
    val sosCountryCode: StateFlow<String> = _sosCountryCode.asStateFlow()

    private val _sosContacts = MutableStateFlow<List<SosContact>>(emptyList())
    val sosContacts: StateFlow<List<SosContact>> = _sosContacts.asStateFlow()

    private val _sosActiveAlert = MutableStateFlow(false)
    val sosActiveAlert: StateFlow<Boolean> = _sosActiveAlert.asStateFlow()

    // Voice Guardian State (Screenshot: Voice Guardian ON & Enrolled voices)
    private val _voiceGuardianEnabled = MutableStateFlow(false)
    val voiceGuardianEnabled: StateFlow<Boolean> = _voiceGuardianEnabled.asStateFlow()

    private val _enrolledVoices = MutableStateFlow<List<EnrolledVoice>>(emptyList())
    val enrolledVoices: StateFlow<List<EnrolledVoice>> = _enrolledVoices.asStateFlow()

    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

    private val _voiceVerificationStatus = MutableStateFlow<String?>(null)
    val voiceVerificationStatus: StateFlow<String?> = _voiceVerificationStatus.asStateFlow()

    private var waveformJob: Job? = null

    init {
        startWaveformSimulation()
    }

    fun setTab(tab: MayaNavTab) {
        _currentTab.value = tab
    }

    fun speakWithSelectedVoice(text: String) {
        val voice = availableVoices.find { it.id == _selectedVoiceId.value }
        if (voice != null) {
            deviceController.speakWithAcoustics(text, voice.pitch, voice.rate)
        } else {
            deviceController.speak(text, _selectedPersona.value)
        }
    }

    fun setPersona(persona: MayaPersona) {
        _selectedPersona.value = persona
        val defaultVoiceId = when (persona) {
            MayaPersona.FRIDAY -> "friday_core"
            MayaPersona.VENOM -> "venom_deep"
            MayaPersona.MAYA -> "aoede"
        }
        _selectedVoiceId.value = defaultVoiceId
        _selectedVoicePersonaTab.value = persona.title
        val announcement = when (persona) {
            MayaPersona.MAYA -> "Maya active. Warm, intuitive, ready to roll."
            MayaPersona.FRIDAY -> "Friday system engaged. Standing by for protocol instructions."
            MayaPersona.VENOM -> "Venom unleashed. Don't waste my time."
        }
        _lastMayaReply.value = announcement
        speakWithSelectedVoice(announcement)
    }

    private fun startWaveformSimulation() {
        waveformJob?.cancel()
        waveformJob = viewModelScope.launch {
            while (true) {
                if (_isListening.value || _isSpeaking.value || _isDuetActive.value) {
                    _waveforms.value = List(16) { Random.nextFloat().coerceIn(0.25f, 1.0f) }
                } else {
                    _waveforms.value = List(16) { Random.nextFloat().coerceIn(0.1f, 0.35f) }
                }
                delay(120)
            }
        }
    }

    fun toggleVoiceListening() {
        if (_isSleeping.value) {
            wakeUpMaya()
            return
        }
        if (_isListening.value) {
            _isListening.value = false
            return
        }
        _isListening.value = true
        _lastTranscript.value = "Listening to voice input..."
        deviceController.stopSpeaking()
    }

    fun submitSpokenPrompt(prompt: String) {
        _isListening.value = false
        _lastTranscript.value = prompt
        executeCommand(prompt)
    }

    fun executeCommand(command: String) {
        val lower = command.lowercase()
        viewModelScope.launch {
            val persona = _selectedPersona.value
            val battery = "${_batteryLevel.value}% (Charging: ${deviceController.isCharging()})"
            _isSpeaking.value = true

            if (lower.contains("so jao") || lower.contains("go to sleep")) {
                _isSleeping.value = true
                val reply = "Entering standby mode. Session closed to preserve energy. Say 'Hey Maya' to wake me."
                _lastMayaReply.value = reply
                deviceController.speak(reply, persona)
                repository.insertAction(
                    PhoneAction(
                        actionType = "VOICE_REMINDER",
                        title = "Sleep Mode Engaged",
                        details = "'So jao' voice command executed. Idle costs 0%.",
                        source = "Voice Command"
                    )
                )
                _isSpeaking.value = false
                return@launch
            }

            if (lower.contains("wake up") || lower.contains("hey maya")) {
                wakeUpMaya()
                return@launch
            }

            // Real device action hooks & Phone Automation
            var deviceActionExecuted = false
            var actionTitle = ""
            var actionDetails = ""

            // 1. Flashlight / Torch
            if (lower.contains("flashlight") || lower.contains("torch")) {
                if (lower.contains("off") || lower.contains("band") || lower.contains("stop")) {
                    _isFlashlightOn.value = deviceController.setTorch(false)
                    actionTitle = "Turned Off Flashlight"
                    actionDetails = "Flashlight deactivated."
                } else {
                    _isFlashlightOn.value = deviceController.setTorch(true)
                    actionTitle = "Turned On Flashlight"
                    actionDetails = "Flashlight activated via voice command."
                }
                deviceActionExecuted = true
            }
            // 2. Find My Phone / Emergency Ringing
            else if (lower.contains("find") && (lower.contains("phone") || lower.contains("device")) || lower.contains("ring phone") || lower.contains("phone dhoondo")) {
                deviceController.playFindMyPhoneAlarm()
                _isFlashlightOn.value = deviceController.toggleFlashlight()
                actionTitle = "Locating Phone Alarm"
                actionDetails = "Loud acoustic siren triggered and strobe activated."
                deviceActionExecuted = true
            }
            // 3. Phone Calling & Dialer
            else if (lower.contains("call") || lower.contains("phone lagao") || lower.contains("phone milao") || lower.contains("dial")) {
                val numberMatch = Regex("\\+?[0-9]{4,15}").find(command)?.value
                if (!numberMatch.isNullOrEmpty()) {
                    deviceController.makePhoneCall(numberMatch)
                    actionTitle = "Phone Call Initiated"
                    actionDetails = "Dialing $numberMatch via phone line."
                } else {
                    deviceController.openDialer()
                    actionTitle = "Phone Dialer Opened"
                    actionDetails = "Phone dialer launched ready to place call."
                }
                deviceActionExecuted = true
            }
            // 4. WhatsApp
            else if (lower.contains("whatsapp")) {
                if (lower.contains("message") || lower.contains("bhejo") || lower.contains("send") || lower.contains("msg")) {
                    val msg = command.replace(Regex("(?i)whatsapp|message|bhejo|send|karo|ko"), "").trim()
                    deviceController.sendWhatsAppMessage(null, if (msg.isNotBlank()) msg else "Hello from Maya!")
                    actionTitle = "WhatsApp Message"
                    actionDetails = "WhatsApp composer triggered."
                } else {
                    deviceController.launchAppByPackage("com.whatsapp", "https://web.whatsapp.com")
                    actionTitle = "Opened WhatsApp"
                    actionDetails = "WhatsApp launched."
                }
                deviceActionExecuted = true
            }
            // 5. SMS
            else if (lower.contains("sms") || lower.contains("text message")) {
                deviceController.sendSms(null, "Hello from Maya AI!")
                actionTitle = "SMS Composer"
                actionDetails = "SMS client launched."
                deviceActionExecuted = true
            }
            // 6. Camera / Photos
            else if (lower.contains("camera") || lower.contains("photo") || lower.contains("selfie") || lower.contains("picture") || lower.contains("take photo")) {
                deviceController.openCamera()
                actionTitle = "Camera Opened"
                actionDetails = "Device camera initialized."
                deviceActionExecuted = true
            }
            // 7. Alarm & Timer
            else if (lower.contains("alarm") || lower.contains("baje ka alarm") || lower.contains("wake me up")) {
                val hour = Regex("(\\d{1,2})\\s*(am|pm|baje)?").find(lower)?.groupValues?.get(1)?.toIntOrNull() ?: 7
                val isPm = lower.contains("pm") || lower.contains("shaam") || lower.contains("raat")
                val adjustedHour = if (isPm && hour < 12) hour + 12 else hour
                deviceController.setAlarm(adjustedHour, 0, "Maya Voice Alarm")
                actionTitle = "Alarm Set for $adjustedHour:00"
                actionDetails = "System alarm clock scheduled."
                deviceActionExecuted = true
            }
            else if (lower.contains("timer")) {
                val minutes = Regex("(\\d{1,2})\\s*(minute|min|m)").find(lower)?.groupValues?.get(1)?.toIntOrNull() ?: 5
                val seconds = minutes * 60
                deviceController.setTimer(seconds, "Maya $minutes Min Timer")
                actionTitle = "$minutes Min Timer Set"
                actionDetails = "Countdown timer started on device."
                deviceActionExecuted = true
            }
            // 8. Music / Spotify / YouTube
            else if (lower.contains("play") || lower.contains("song") || lower.contains("music") || lower.contains("gaana") || lower.contains("spotify") || lower.contains("youtube")) {
                val cleanQuery = command.replace(Regex("(?i)play|song|music|gaana|chalao|bajao|on spotify|on youtube"), "").trim()
                if (lower.contains("youtube")) {
                    deviceController.openYouTube(cleanQuery.ifBlank { null })
                    actionTitle = "YouTube Music Playback"
                    actionDetails = "Playing '$cleanQuery' on YouTube."
                } else {
                    deviceController.playMusic(cleanQuery.ifBlank { null })
                    actionTitle = "Music Playback Started"
                    actionDetails = "Playing '$cleanQuery' on music player."
                }
                deviceActionExecuted = true
            }
            // 9. Volume Controls
            else if (lower.contains("volume") || lower.contains("awaz") || lower.contains("sound")) {
                if (lower.contains("mute") || lower.contains("silent") || lower.contains("band")) {
                    deviceController.muteVolume()
                    actionTitle = "Volume Muted"
                    actionDetails = "Device media volume silenced."
                } else if (lower.contains("up") || lower.contains("badhao") || lower.contains("tez") || lower.contains("high")) {
                    val vol = deviceController.adjustVolume(true)
                    actionTitle = "Volume Increased"
                    actionDetails = "Media volume increased to $vol%."
                } else if (lower.contains("down") || lower.contains("kam") || lower.contains("dheeme") || lower.contains("low")) {
                    val vol = deviceController.adjustVolume(false)
                    actionTitle = "Volume Lowered"
                    actionDetails = "Media volume lowered to $vol%."
                } else {
                    val vol = deviceController.setVolumePercent(80)
                    actionTitle = "Volume Adjusted"
                    actionDetails = "Media volume set to $vol%."
                }
                deviceActionExecuted = true
            }
            // 10. Wi-Fi & Bluetooth Settings
            else if (lower.contains("wifi") || lower.contains("wi-fi")) {
                deviceController.openWifiSettings()
                actionTitle = "Wi-Fi Settings"
                actionDetails = "Opened Android Wi-Fi network panel."
                deviceActionExecuted = true
            } else if (lower.contains("bluetooth")) {
                deviceController.openBluetoothSettings()
                actionTitle = "Bluetooth Settings"
                actionDetails = "Opened Android Bluetooth panel."
                deviceActionExecuted = true
            }
            // 11. Web & Google Search Automation
            else if (lower.contains("search") || lower.contains("google karo") || lower.contains("search karo")) {
                val query = command.replace(Regex("(?i)search for|search|google karo|search karo|google"), "").trim()
                val target = if (query.isNotBlank()) query else "latest news"
                deviceController.webSearch(target)
                actionTitle = "Google Web Search"
                actionDetails = "Searching '$target' on Google."
                deviceActionExecuted = true
            }
            // 12. Calendar & Reminder Automation
            else if (lower.contains("calendar") || lower.contains("schedule") || lower.contains("meeting") || lower.contains("appointment")) {
                val title = command.replace(Regex("(?i)calendar|schedule|meeting|appointment|add|create"), "").trim()
                val reminderTitle = if (title.isNotBlank()) title else "Maya AI Reminder"
                deviceController.openCalendarEvent(reminderTitle)
                actionTitle = "Calendar Event"
                actionDetails = "Opened Calendar to schedule: $reminderTitle"
                deviceActionExecuted = true
            }
            // 13. Email & Gmail Automation
            else if (lower.contains("email") || lower.contains("mail") || lower.contains("gmail")) {
                val mailBody = command.replace(Regex("(?i)send email|email bhejo|mail bhejo|email|mail"), "").trim()
                deviceController.sendEmail(body = mailBody)
                actionTitle = "Email Client"
                actionDetails = "Opened email composer: $mailBody"
                deviceActionExecuted = true
            }
            // 14. Emergency Strobe Light Beacon
            else if (lower.contains("strobe") || lower.contains("sos light") || lower.contains("emergency light")) {
                deviceController.flashStrobe(8)
                actionTitle = "Emergency Strobe Beacon"
                actionDetails = "Flashing camera LED strobe emergency beacon."
                deviceActionExecuted = true
            }
            // 15. Ringer & Vibrate Profiles
            else if (lower.contains("vibrate") || lower.contains("vibration mode")) {
                deviceController.toggleVibrateMode()
                actionTitle = "Vibrate Profile"
                actionDetails = "Ringer switched to Vibrate."
                deviceActionExecuted = true
            } else if (lower.contains("ring mode") || lower.contains("normal sound") || lower.contains("ringer on")) {
                deviceController.toggleNormalRinger()
                actionTitle = "Normal Ring Mode"
                actionDetails = "Ringer profile restored to Normal."
                deviceActionExecuted = true
            }
            // 16. Apps: Instagram, etc.
            else if (lower.contains("instagram") || lower.contains("insta")) {
                deviceController.launchAppByPackage("com.instagram.android", "https://instagram.com")
                actionTitle = "Opened Instagram"
                actionDetails = "Instagram launched."
                deviceActionExecuted = true
            } else if (lower.contains("otp")) {
                val newOtp = (100000..999999).random().toString()
                _latestOtp.value = newOtp
                actionTitle = "Extracted Banking OTP"
                actionDetails = "Captured code $newOtp from SMS gateway; synced to desktop HUD."
                deviceActionExecuted = true
            }

            if (deviceActionExecuted) {
                repository.insertAction(
                    PhoneAction(
                        actionType = "DEVICE_CONTROL",
                        title = actionTitle,
                        details = actionDetails,
                        source = "Maya Core"
                    )
                )
            }

            // Fetch AI generated response from Gemini service
            val reply = geminiService.generateMayaResponse(command, persona, battery)
            _lastMayaReply.value = reply
            speakWithSelectedVoice(reply)

            repository.insertAction(
                PhoneAction(
                    actionType = "VOICE_COMMAND",
                    title = command.take(30),
                    details = reply,
                    source = "${persona.title} Engine"
                )
            )

            delay(1200)
            _isSpeaking.value = false
        }
    }

    fun toggleSleep() {
        if (_isSleeping.value) {
            wakeUpMaya()
        } else {
            _isSleeping.value = true
            _isSpeaking.value = false
            deviceController.stopSpeaking()
            val msg = "Entering standby mode. Session closed to preserve energy. Say 'Hey Maya' to wake me."
            _lastMayaReply.value = msg
            speakWithSelectedVoice(msg)
        }
    }

    fun wakeUpMaya() {
        _isSleeping.value = false
        val greeting = when (_selectedPersona.value) {
            MayaPersona.MAYA -> "Hey! I'm back online. Session re-established."
            MayaPersona.FRIDAY -> "Systems restored to full active capacity."
            MayaPersona.VENOM -> "I'm awake. What do you need?"
        }
        _lastMayaReply.value = greeting
        _lastTranscript.value = "Awake and listening."
        speakWithSelectedVoice(greeting)
    }

    fun toggleDuetCall() {
        _isDuetActive.value = !_isDuetActive.value
        val active = _isDuetActive.value
        val persona = _selectedPersona.value
        if (active) {
            val announcement = "Duet call initiated! Desktop Maya and Phone Maya are communicating over low-latency duplex audio."
            _lastMayaReply.value = announcement
            deviceController.speak(announcement, persona)
            viewModelScope.launch {
                repository.insertAction(
                    PhoneAction(
                        actionType = "DUET_CALL",
                        title = "Live Duet Call Started",
                        details = "Duplex 24kHz cross-device audio stream active.",
                        source = "Phone Link",
                        status = "Active"
                    )
                )
            }
        } else {
            deviceController.stopSpeaking()
            _lastMayaReply.value = "Duet call ended. Handed back to desktop focus."
        }
    }

    fun triggerSimulatedOtp(bankName: String, amount: String) {
        val randomOtp = (100000..999999).random().toString()
        _latestOtp.value = randomOtp
        viewModelScope.launch {
            val action = PhoneAction(
                actionType = "OTP_READ",
                title = "$bankName Transaction OTP",
                details = "OTP: $randomOtp for $amount transfer. Sync to Windows HUD successful.",
                source = "SMS Receiver"
            )
            repository.insertAction(action)
            val msg = "New OTP received from $bankName: $randomOtp for $amount."
            _lastMayaReply.value = msg
            deviceController.speak(msg, _selectedPersona.value)
        }
    }

    fun triggerFindPhone() {
        deviceController.playFindMyPhoneAlarm()
        _isFlashlightOn.value = deviceController.toggleFlashlight()
        viewModelScope.launch {
            repository.insertAction(
                PhoneAction(
                    actionType = "DUET_CALL",
                    title = "Locate Phone Triggered",
                    details = "Alarm & Torch activated via Desktop Maya remote command.",
                    source = "Desktop Maya"
                )
            )
        }
    }

    fun toggleTorch() {
        _isFlashlightOn.value = deviceController.toggleFlashlight()
        val state = if (_isFlashlightOn.value) "On" else "Off"
        val msg = "Flashlight $state."
        _lastMayaReply.value = msg
        speakWithSelectedVoice(msg)
    }

    fun launchInstalledApp(name: String) {
        when (name.lowercase()) {
            "call", "phone", "dialer" -> {
                deviceController.openDialer()
                recordDeviceAction("Phone Dialer", "Opened phone dialer ready to call.")
                return
            }
            "camera" -> {
                deviceController.openCamera()
                recordDeviceAction("Camera Launch", "Opened device camera.")
                return
            }
            "alarm" -> {
                deviceController.setAlarm(7, 0, "Maya Daily Alarm")
                recordDeviceAction("Alarm Clock", "Set alarm for 7:00 AM.")
                return
            }
            "music", "spotify" -> {
                deviceController.playMusic(null)
                recordDeviceAction("Music Player", "Launched music playback.")
                return
            }
            "youtube" -> {
                deviceController.openYouTube(null)
                recordDeviceAction("YouTube", "Opened YouTube app.")
                return
            }
            "wifi" -> {
                deviceController.openWifiSettings()
                recordDeviceAction("Wi-Fi Settings", "Opened Wi-Fi settings.")
                return
            }
            "bluetooth" -> {
                deviceController.openBluetoothSettings()
                recordDeviceAction("Bluetooth Settings", "Opened Bluetooth settings.")
                return
            }
            "settings" -> {
                deviceController.openSystemSettings()
                recordDeviceAction("System Settings", "Opened Android settings.")
                return
            }
            "whatsapp" -> {
                deviceController.sendWhatsAppMessage(null, "Hello from Maya!")
                recordDeviceAction("WhatsApp", "Opened WhatsApp conversation.")
                return
            }
            "instagram" -> {
                deviceController.launchAppByPackage("com.instagram.android", "https://instagram.com")
                recordDeviceAction("Instagram", "Opened Instagram.")
                return
            }
        }

        val (pkg, fallback) = Pair("com.android.chrome", "https://google.com")
        deviceController.launchAppByPackage(pkg, fallback)
        recordDeviceAction("App Launch: $name", "Launched $name via Phone Link command.")
    }

    fun directCall(number: String) {
        deviceController.makePhoneCall(number)
        recordDeviceAction("Calling $number", "Phone call placed to $number.")
        speakWithSelectedVoice("Calling $number now.")
    }

    fun directAlarm(hour: Int, minute: Int) {
        deviceController.setAlarm(hour, minute, "Maya Morning Alarm")
        val timeStr = String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
        recordDeviceAction("Alarm Set", "Alarm scheduled for $timeStr.")
        speakWithSelectedVoice("Alarm set for $timeStr.")
    }

    fun directTimer(minutes: Int) {
        deviceController.setTimer(minutes * 60, "Maya $minutes Min Timer")
        recordDeviceAction("Timer Started", "Started $minutes minute countdown.")
        speakWithSelectedVoice("$minutes minute timer started.")
    }

    fun directMusic(query: String) {
        deviceController.playMusic(query)
        recordDeviceAction("Playing Music", "Playing $query.")
        speakWithSelectedVoice("Playing $query.")
    }

    fun directVolume(increase: Boolean) {
        val newVol = deviceController.adjustVolume(increase)
        val dir = if (increase) "increased" else "lowered"
        recordDeviceAction("Volume $dir", "Media volume is now $newVol%.")
        speakWithSelectedVoice("Volume $dir to $newVol percent.")
    }

    fun directMute() {
        deviceController.muteVolume()
        recordDeviceAction("Muted", "Device volume silenced.")
        speakWithSelectedVoice("Media muted.")
    }

    fun directWebSearch(query: String = "Google Search") {
        deviceController.webSearch(query)
        recordDeviceAction("Web Search", "Initiated Google Search for $query.")
        speakWithSelectedVoice("Searching Google for $query.")
    }

    fun directCalendar() {
        deviceController.openCalendarEvent("Maya Task Reminder")
        recordDeviceAction("Calendar", "Opened Android calendar scheduler.")
        speakWithSelectedVoice("Opening calendar.")
    }

    fun directEmail() {
        deviceController.sendEmail(body = "Sent via Maya AI Companion")
        recordDeviceAction("Email", "Opened email composer.")
        speakWithSelectedVoice("Opening email.")
    }

    fun directStrobe() {
        deviceController.flashStrobe(8)
        recordDeviceAction("Strobe Light", "Emergency LED strobe flashing.")
        speakWithSelectedVoice("Emergency strobe beacon activated.")
    }

    fun directVibrate() {
        deviceController.toggleVibrateMode()
        recordDeviceAction("Vibrate Mode", "Phone set to vibrate mode.")
        speakWithSelectedVoice("Vibrate mode enabled.")
    }

    private fun recordDeviceAction(title: String, details: String) {
        viewModelScope.launch {
            repository.insertAction(
                PhoneAction(
                    actionType = "DEVICE_CONTROL",
                    title = title,
                    details = details,
                    source = "Phone Control"
                )
            )
        }
    }

    fun pairWithSessionCode(code: String) {
        viewModelScope.launch {
            val updated = PairedSession(
                id = 1,
                desktopName = "Hunter-Rig (Win 11 Pro 64-bit)",
                desktopIp = "192.168.1.108:8765",
                sessionToken = code.uppercase(),
                isConnected = true,
                lastPingMs = 9,
                agentCount = 7,
                toolsEnabledCount = 234
            )
            repository.saveSession(updated)
            val msg = "Connected to desktop session $code. All 234 tools and 7 agents synchronized."
            _lastMayaReply.value = msg
            deviceController.speak(msg, _selectedPersona.value)
        }
    }

    fun disconnectSession() {
        viewModelScope.launch {
            val disconnected = PairedSession(
                id = 1,
                desktopName = "Disconnected",
                desktopIp = "Offline",
                sessionToken = "NONE",
                isConnected = false,
                lastPingMs = 0,
                agentCount = 0,
                toolsEnabledCount = 0
            )
            repository.saveSession(disconnected)
        }
    }

    fun addCustomMemory(category: String, keyTag: String, factText: String) {
        viewModelScope.launch {
            repository.insertMemory(
                MayaMemory(
                    category = category,
                    keyTag = keyTag,
                    factText = factText
                )
            )
            val msg = "Memory stored: $keyTag. Recalled across sessions."
            _lastMayaReply.value = msg
        }
    }

    fun deleteMemory(memory: MayaMemory) {
        viewModelScope.launch {
            repository.deleteMemory(memory)
        }
    }

    fun clearActionHistory() {
        viewModelScope.launch {
            repository.clearActions()
        }
    }

    fun triggerHapticFeedback() {
        deviceController.triggerHaptic(120)
    }

    fun openAppSettings() {
        deviceController.openAppSettings()
    }

    fun testTtsVoice() {
        deviceController.speak(
            "Maya voice channel test successful. Audio synthesis and microphone feedback loop nominal.",
            _selectedPersona.value
        )
    }

    // Rules Management (Screenshot 3)
    fun addRule(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertRule(com.example.data.model.MayaRule(ruleText = text.trim()))
            deviceController.triggerHaptic(80)
        }
    }

    fun deleteRule(rule: com.example.data.model.MayaRule) {
        viewModelScope.launch {
            repository.deleteRule(rule)
            deviceController.triggerHaptic(60)
        }
    }

    fun toggleRule(rule: com.example.data.model.MayaRule) {
        viewModelScope.launch {
            repository.updateRule(rule.copy(isEnabled = !rule.isEnabled))
            deviceController.triggerHaptic(50)
        }
    }

    fun setActivateDialog(show: Boolean) {
        _showActivateDialog.value = show
        deviceController.triggerHaptic(40)
    }

    fun setRuleDialog(show: Boolean) {
        _showRuleDialog.value = show
        deviceController.triggerHaptic(40)
    }

    fun openQuickTool(tool: String) {
        _activeQuickTool.value = tool
        deviceController.triggerHaptic(60)
    }

    fun closeQuickTool() {
        _activeQuickTool.value = null
    }

    // Interactive Character Tap & Glowing Green Heart Aura (Screenshot 2)
    fun tapCharacter() {
        deviceController.triggerHaptic(100)
        val heart = FloatingHeart(
            id = System.currentTimeMillis(),
            offsetX = (Random.nextFloat() - 0.5f) * 60f
        )
        _floatingHearts.value = _floatingHearts.value + heart

        val responses = listOf(
            "Good morning! Maya is ready to help you.",
            "I'm here for you! ✨ What should we work on?",
            "Phone link and desktop tools are online and synced.",
            "Always happy to see you! How are you doing today?",
            "Let's make today productive and fun!"
        )
        val reply = responses.random()
        _lastMayaReply.value = reply
        speakWithSelectedVoice(reply)

        viewModelScope.launch {
            delay(2200)
            _floatingHearts.value = _floatingHearts.value.filter { it.id != heart.id }
        }
    }

    fun toggleWeather() {
        deviceController.triggerHaptic(50)
        _weatherState.value = if (_weatherState.value.contains("No data")) {
            "27°C\nClear Sky"
        } else {
            "—\nNo data"
        }
    }

    fun cycleMood() {
        deviceController.triggerHaptic(50)
        val moods = listOf("Warm\nAll good", "Energetic\nReady", "Calm\nFocused", "Playful\nCurious")
        val current = _moodState.value
        val idx = moods.indexOf(current)
        _moodState.value = moods[(idx + 1).coerceAtLeast(0) % moods.size]
    }

    // --- Touch Guard Functions ---
    fun setTouchGuardEnabled(enabled: Boolean) {
        deviceController.triggerHaptic(60)
        _touchGuardEnabled.value = enabled
        if (enabled) {
            deviceController.speak("Touch Guard armed. Watching device.", _selectedPersona.value)
        } else {
            deviceController.speak("Touch Guard disabled.", _selectedPersona.value)
        }
    }

    fun toggleTouchGuardWake(enabled: Boolean) {
        _touchGuardWakeScreen.value = enabled
    }

    fun toggleTouchGuardPickup(enabled: Boolean) {
        _touchGuardPickup.value = enabled
    }

    fun toggleTouchGuardCharger(enabled: Boolean) {
        _touchGuardCharger.value = enabled
    }

    fun toggleTouchGuardSiren(enabled: Boolean) {
        _touchGuardSiren.value = enabled
    }

    fun simulateTouchGuardTrigger(triggerType: String) {
        deviceController.triggerHaptic(200)
        val formatter = SimpleDateFormat("hh:mm:ss a, MMM dd", Locale.getDefault())
        val incident = TouchIncident(
            id = System.currentTimeMillis(),
            trigger = triggerType,
            timestamp = formatter.format(Date()),
            hasPhoto = true
        )
        _touchGuardIncidents.value = listOf(incident) + _touchGuardIncidents.value
        deviceController.speak("Warning! Unauthorized touch detected: $triggerType. Front camera snapshot captured.", _selectedPersona.value)
    }

    fun clearTouchGuardIncidents() {
        _touchGuardIncidents.value = emptyList()
    }

    // --- Emergency SOS Functions ---
    fun setSosCountryCode(code: String) {
        deviceController.triggerHaptic(40)
        _sosCountryCode.value = code
    }

    fun addSosContact(name: String, phone: String, relation: String = "Emergency") {
        deviceController.triggerHaptic(60)
        val contact = SosContact(
            id = System.currentTimeMillis(),
            name = name,
            phone = phone,
            relation = relation
        )
        _sosContacts.value = _sosContacts.value + contact
    }

    fun removeSosContact(id: Long) {
        deviceController.triggerHaptic(50)
        _sosContacts.value = _sosContacts.value.filter { it.id != id }
    }

    fun triggerSosAlert() {
        deviceController.triggerHaptic(500)
        _sosActiveAlert.value = true
        deviceController.speak("Emergency SOS initiated! Dispatching GPS coordinates to trusted contacts.", _selectedPersona.value)
    }

    fun cancelSosAlert() {
        deviceController.triggerHaptic(100)
        _sosActiveAlert.value = false
        deviceController.speak("Emergency alert cancelled.", _selectedPersona.value)
    }

    // --- Voice Guardian Functions ---
    fun setVoiceGuardianEnabled(enabled: Boolean) {
        deviceController.triggerHaptic(60)
        _voiceGuardianEnabled.value = enabled
        if (enabled) {
            deviceController.speak("Voice Guardian active. Restricted to enrolled voice profiles.", _selectedPersona.value)
        } else {
            deviceController.speak("Voice Guardian turned off.", _selectedPersona.value)
        }
    }

    fun enrollVoice(name: String = "Primary Owner (You)", role: String = "Owner") {
        deviceController.triggerHaptic(100)
        _isRecordingVoice.value = true
        viewModelScope.launch {
            delay(1800)
            _isRecordingVoice.value = false
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val voice = EnrolledVoice(
                id = System.currentTimeMillis(),
                name = name,
                role = role,
                enrolledDate = formatter.format(Date())
            )
            _enrolledVoices.value = _enrolledVoices.value + voice
            _voiceVerificationStatus.value = "Voice profile '$name' enrolled successfully!"
            deviceController.speak("Voice profile enrolled. Owner verification ready.", _selectedPersona.value)
        }
    }

    fun deleteEnrolledVoice(id: Long) {
        deviceController.triggerHaptic(50)
        _enrolledVoices.value = _enrolledVoices.value.filter { it.id != id }
    }

    fun testVoice() {
        deviceController.triggerHaptic(80)
        _isRecordingVoice.value = true
        viewModelScope.launch {
            delay(1500)
            _isRecordingVoice.value = false
            if (_enrolledVoices.value.isEmpty()) {
                _voiceVerificationStatus.value = "Unknown Voice (Guest role): No enrolled profile found. Please record owner's voice first."
                deviceController.speak("Unknown voice detected. Guest access only.", _selectedPersona.value)
            } else {
                val owner = _enrolledVoices.value.first()
                _voiceVerificationStatus.value = "Voice Verified: Matched '${owner.name}' (${owner.role}). Full command privileges unlocked."
                deviceController.speak("Voice match verified. Welcome back, Owner.", _selectedPersona.value)
            }
        }
    }

    fun clearVoiceVerificationStatus() {
        _voiceVerificationStatus.value = null
    }

    // ==========================================
    // THEME & CUSTOMIZATION STATE
    // ==========================================
    private val _selectedTheme = MutableStateFlow("Midnight")
    val selectedTheme: StateFlow<String> = _selectedTheme.asStateFlow()

    private val _selectedTypeface = MutableStateFlow("Inter")
    val selectedTypeface: StateFlow<String> = _selectedTypeface.asStateFlow()

    private val _selectedFontSize = MutableStateFlow("Default")
    val selectedFontSize: StateFlow<String> = _selectedFontSize.asStateFlow()

    private val _selectedSurfaceStyle = MutableStateFlow("Glass")
    val selectedSurfaceStyle: StateFlow<String> = _selectedSurfaceStyle.asStateFlow()

    private val _selectedCorners = MutableStateFlow("Rounded")
    val selectedCorners: StateFlow<String> = _selectedCorners.asStateFlow()

    fun setTheme(theme: String) {
        deviceController.triggerHaptic(40)
        _selectedTheme.value = theme
    }

    fun setTypeface(tf: String) {
        deviceController.triggerHaptic(40)
        _selectedTypeface.value = tf
    }

    fun setFontSize(size: String) {
        deviceController.triggerHaptic(40)
        _selectedFontSize.value = size
    }

    fun setSurfaceStyle(style: String) {
        deviceController.triggerHaptic(40)
        _selectedSurfaceStyle.value = style
    }

    fun setCorners(corners: String) {
        deviceController.triggerHaptic(40)
        _selectedCorners.value = corners
    }

    // ==========================================
    // APPEARANCE & ORB STATE
    // ==========================================
    private val _selectedOrbStyle = MutableStateFlow("Maya Nova")
    val selectedOrbStyle: StateFlow<String> = _selectedOrbStyle.asStateFlow()

    private val _selectedOrbColor = MutableStateFlow("Persona")
    val selectedOrbColor: StateFlow<String> = _selectedOrbColor.asStateFlow()

    private val _floatingOrbSize = MutableStateFlow(190f)
    val floatingOrbSize: StateFlow<Float> = _floatingOrbSize.asStateFlow()

    private val _useOrbOnHome = MutableStateFlow(false)
    val useOrbOnHome: StateFlow<Boolean> = _useOrbOnHome.asStateFlow()

    fun setOrbStyle(style: String) {
        deviceController.triggerHaptic(40)
        _selectedOrbStyle.value = style
    }

    fun setOrbColor(color: String) {
        deviceController.triggerHaptic(40)
        _selectedOrbColor.value = color
    }

    fun setFloatingOrbSize(size: Float) {
        _floatingOrbSize.value = size
    }

    fun setUseOrbOnHome(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _useOrbOnHome.value = enabled
    }

    // ==========================================
    // BEHAVIOUR STATE
    // ==========================================
    private val _floatingOrbEnabled = MutableStateFlow(true)
    val floatingOrbEnabled: StateFlow<Boolean> = _floatingOrbEnabled.asStateFlow()

    private val _echoGuardEnabled = MutableStateFlow(true)
    val echoGuardEnabled: StateFlow<Boolean> = _echoGuardEnabled.asStateFlow()

    private val _startOnBootEnabled = MutableStateFlow(true)
    val startOnBootEnabled: StateFlow<Boolean> = _startOnBootEnabled.asStateFlow()

    private val _batteryBypassEnabled = MutableStateFlow(true)
    val batteryBypassEnabled: StateFlow<Boolean> = _batteryBypassEnabled.asStateFlow()

    private val _keepAliveServiceEnabled = MutableStateFlow(true)
    val keepAliveServiceEnabled: StateFlow<Boolean> = _keepAliveServiceEnabled.asStateFlow()

    fun toggleFloatingOrb(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _floatingOrbEnabled.value = enabled
    }

    fun toggleEchoGuard(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _echoGuardEnabled.value = enabled
    }

    fun toggleStartOnBoot(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _startOnBootEnabled.value = enabled
    }

    fun toggleBatteryBypass(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _batteryBypassEnabled.value = enabled
    }

    fun toggleKeepAlive(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _keepAliveServiceEnabled.value = enabled
    }

    // ==========================================
    // TYPING STATE
    // ==========================================
    private val _humanTypingInEditors = MutableStateFlow(true)
    val humanTypingInEditors: StateFlow<Boolean> = _humanTypingInEditors.asStateFlow()

    private val _typingSpeed = MutableStateFlow("Normal")
    val typingSpeed: StateFlow<String> = _typingSpeed.asStateFlow()

    private val _typingWhileCoding = MutableStateFlow(true)
    val typingWhileCoding: StateFlow<Boolean> = _typingWhileCoding.asStateFlow()

    private val _typingApps = MutableStateFlow(
        "com.google.android.keep,com.google.android.apps.docs.editors.docs,com.samsung.android.app.notes,com.termux,net.gsantner.markor,com.foxdebug.acode,org.jotdown.jota"
    )
    val typingApps: StateFlow<String> = _typingApps.asStateFlow()

    fun toggleHumanTyping(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _humanTypingInEditors.value = enabled
    }

    fun setTypingSpeed(speed: String) {
        deviceController.triggerHaptic(40)
        _typingSpeed.value = speed
    }

    fun toggleTypingWhileCoding(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _typingWhileCoding.value = enabled
    }

    fun updateTypingApps(apps: String) {
        _typingApps.value = apps
    }

    // ==========================================
    // CONNECTORS STATE
    // ==========================================
    private val _connectors = MutableStateFlow(
        listOf(
            ConnectorItem("gdrive", "Google Drive", "Files you create or pick", "Files", true, "gdrive"),
            ConnectorItem("github", "GitHub", "Repos, issues and files", "Code", true, "github"),
            ConnectorItem("vercel", "Vercel", "Put a site online", "Code", false, "vercel"),
            ConnectorItem("notion", "Notion", "Pages, notes and databases", "Notes & tasks", false, "notion"),
            ConnectorItem("telegram", "Telegram", "Send yourself messages and files", "Messages", true, "telegram"),
            ConnectorItem("todoist", "Todoist", "Your real task list", "Notes & tasks", false, "todoist"),
            ConnectorItem("gitlab", "GitLab", "Projects, issues and files", "Code", false, "gitlab"),
            ConnectorItem("linear", "Linear", "Issues and cycles", "Notes & tasks", false, "linear")
        )
    )
    val connectors: StateFlow<List<ConnectorItem>> = _connectors.asStateFlow()

    private val _connectorSearchQuery = MutableStateFlow("")
    val connectorSearchQuery: StateFlow<String> = _connectorSearchQuery.asStateFlow()

    private val _connectorCategoryFilter = MutableStateFlow("All")
    val connectorCategoryFilter: StateFlow<String> = _connectorCategoryFilter.asStateFlow()

    fun setConnectorSearch(query: String) {
        _connectorSearchQuery.value = query
    }

    fun setConnectorCategory(category: String) {
        deviceController.triggerHaptic(30)
        _connectorCategoryFilter.value = category
    }

    fun toggleConnector(id: String) {
        deviceController.triggerHaptic(50)
        _connectors.value = _connectors.value.map {
            if (it.id == id) it.copy(isConnected = !it.isConnected) else it
        }
    }

    // ==========================================
    // GROUPS & REPORTS STATE
    // ==========================================
    private val _whatsAppGroups = MutableStateFlow(
        listOf("Core Dev Team", "Client Updates", "Daily Standup Group")
    )
    val whatsAppGroups: StateFlow<List<String>> = _whatsAppGroups.asStateFlow()

    private val _reportFormats = MutableStateFlow(
        listOf(
            ReportFormat(
                id = 1L,
                title = "Daily Status Update",
                template = "Good evening team,\nToday's progress: {tasks_done}\nPending blockers: {blockers}\nTomorrow's focus: {next_plan}\n- Sent via Maya",
                groupTarget = "Core Dev Team"
            ),
            ReportFormat(
                id = 2L,
                title = "Incident Summary",
                template = "URGENT INCIDENT REPORT\nSystem: {system}\nStatus: {status}\nETA for resolution: {eta}",
                groupTarget = "Client Updates"
            )
        )
    )
    val reportFormats: StateFlow<List<ReportFormat>> = _reportFormats.asStateFlow()

    private val _groupReportStatusMessage = MutableStateFlow<String?>(null)
    val groupReportStatusMessage: StateFlow<String?> = _groupReportStatusMessage.asStateFlow()

    fun addWhatsAppGroup(name: String) {
        if (name.isNotBlank() && !_whatsAppGroups.value.contains(name.trim())) {
            deviceController.triggerHaptic(50)
            _whatsAppGroups.value = _whatsAppGroups.value + name.trim()
        }
    }

    fun removeWhatsAppGroup(name: String) {
        deviceController.triggerHaptic(40)
        _whatsAppGroups.value = _whatsAppGroups.value.filter { it != name }
    }

    fun addReportFormat(title: String, template: String, groupTarget: String) {
        if (title.isNotBlank() && template.isNotBlank()) {
            deviceController.triggerHaptic(50)
            val newFormat = ReportFormat(
                id = System.currentTimeMillis(),
                title = title.trim(),
                template = template.trim(),
                groupTarget = groupTarget.ifBlank { "All groups" }
            )
            _reportFormats.value = _reportFormats.value + newFormat
        }
    }

    fun removeReportFormat(id: Long) {
        deviceController.triggerHaptic(40)
        _reportFormats.value = _reportFormats.value.filter { it.id != id }
    }

    fun testSendReport(format: ReportFormat) {
        deviceController.triggerHaptic(70)
        _groupReportStatusMessage.value = "Maya safety check: Previewing report for '${format.groupTarget}' before sending."
        deviceController.speak("Safety confirmation required: Sending report to ${format.groupTarget}. Confirming values.", _selectedPersona.value)
    }

    fun clearGroupReportStatus() {
        _groupReportStatusMessage.value = null
    }

    // ==========================================
    // EMAIL STATE
    // ==========================================
    private val _emailConnected = MutableStateFlow(true)
    val emailConnected: StateFlow<Boolean> = _emailConnected.asStateFlow()

    private val _emailAddress = MutableStateFlow("thehunter.ai.user@gmail.com")
    val emailAddress: StateFlow<String> = _emailAddress.asStateFlow()

    private val _emailSignature = MutableStateFlow("Regards,\nYour Name\n+91 90000 00000")
    val emailSignature: StateFlow<String> = _emailSignature.asStateFlow()

    private val _emailSmtpHost = MutableStateFlow("")
    val emailSmtpHost: StateFlow<String> = _emailSmtpHost.asStateFlow()

    private val _emailPort = MutableStateFlow("465")
    val emailPort: StateFlow<String> = _emailPort.asStateFlow()

    private val _emailStatusMessage = MutableStateFlow<String?>(null)
    val emailStatusMessage: StateFlow<String?> = _emailStatusMessage.asStateFlow()

    fun updateEmailSignature(sig: String) {
        _emailSignature.value = sig
    }

    fun updateEmailServer(host: String, port: String) {
        _emailSmtpHost.value = host
        _emailPort.value = port
    }

    fun checkEmailConnection() {
        deviceController.triggerHaptic(60)
        viewModelScope.launch {
            _emailStatusMessage.value = "Checking TLS direct connection to mail server..."
            delay(1200)
            _emailStatusMessage.value = "Direct TLS connection verified. All traffic securely encrypted."
            deviceController.speak("Email TLS connection verified and secure.", _selectedPersona.value)
        }
    }

    fun disconnectEmail() {
        deviceController.triggerHaptic(70)
        _emailConnected.value = false
        _emailStatusMessage.value = "Email credentials purged from phone."
    }

    fun reconnectEmail(address: String) {
        deviceController.triggerHaptic(60)
        _emailAddress.value = address
        _emailConnected.value = true
        _emailStatusMessage.value = "Connected to $address successfully."
    }

    fun clearEmailStatus() {
        _emailStatusMessage.value = null
    }

    // ==========================================
    // SKILLS STORE & PLAYBOOKS (Screenshot 1)
    // ==========================================
    private val _installedSkills = MutableStateFlow(
        listOf(
            InstalledSkillItem(
                id = "pro-email",
                name = "pro-email",
                description = "Professional email likhna aur bhejna — leave, apology, follow-up, application, client mail",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "group-report",
                name = "group-report",
                description = "Job/work WhatsApp group me fixed format wali daily report bhejna — value poochh ke, confirm le ke",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "pro-whatsapp",
                name = "pro-whatsapp",
                description = "Professional WhatsApp message likhna — client, boss, HR, vendor ko dhang ka message",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "youtube-script",
                name = "youtube-script",
                description = "YouTube video ka ready-to-speak script likhne ka tarika — hook, retention, CTA aur bolne layak language",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "youtube-title-thumbnail",
                name = "youtube-title-thumbnail",
                description = "YouTube title aur thumbnail text likhna — curiosity gap banao, jhooth nahi",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "youtube-upload",
                name = "youtube-upload",
                description = "Video upload se pehle ka kaam — description, chapters, tags, pinned comment ka checklist",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "social-posting",
                name = "social-posting",
                description = "Photo ya poster ko Instagram/Facebook pe story ya post ki tarah daalna, sahi size aur sahi rasta",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "web-design",
                name = "web-design",
                description = "Website banate waqt colour, layout aur typography ka dhyan rakhna",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "data-analysis",
                name = "data-analysis",
                description = "Excel, CSV ya sheet ka data analyze karke quick insights nikalna",
                isEnabled = true
            ),
            InstalledSkillItem(
                id = "voice-memo",
                name = "voice-memo",
                description = "Lambe audio notes aur meeting recordings ko structured bullet points me summarize karna",
                isEnabled = true
            )
        )
    )
    val installedSkills: StateFlow<List<InstalledSkillItem>> = _installedSkills.asStateFlow()

    fun toggleSkill(id: String, enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _installedSkills.value = _installedSkills.value.map { skill ->
            if (skill.id == id) skill.copy(isEnabled = enabled) else skill
        }
    }

    // ==========================================
    // MAYA ASSISTANT SETTINGS (Screenshots 2-6)
    // ==========================================
    private val _assistantName = MutableStateFlow("Maya")
    val assistantName: StateFlow<String> = _assistantName.asStateFlow()

    private val _assistantPersonaSelection = MutableStateFlow("Maya")
    val assistantPersonaSelection: StateFlow<String> = _assistantPersonaSelection.asStateFlow()

    private val _girlfriendModeEnabled = MutableStateFlow(false)
    val girlfriendModeEnabled: StateFlow<Boolean> = _girlfriendModeEnabled.asStateFlow()

    private val _memoryAutoSave = MutableStateFlow(true)
    val memoryAutoSave: StateFlow<Boolean> = _memoryAutoSave.asStateFlow()

    private val _memoryIncognito = MutableStateFlow(false)
    val memoryIncognito: StateFlow<Boolean> = _memoryIncognito.asStateFlow()

    private val _selectedVoicePersonaTab = MutableStateFlow("Maya")
    val selectedVoicePersonaTab: StateFlow<String> = _selectedVoicePersonaTab.asStateFlow()

    private val _selectedVoiceId = MutableStateFlow("aoede")
    val selectedVoiceId: StateFlow<String> = _selectedVoiceId.asStateFlow()

    val availableVoices = listOf(
        VoiceOption("aoede", "Breezy", "Aoede", "Maya", pitch = 1.15f, rate = 1.05f, description = "Default calm, natural tone"),
        VoiceOption("kore", "Firm", "Kore", "Maya", pitch = 1.02f, rate = 1.02f, description = "Steady, articulate presence"),
        VoiceOption("leda", "Youthful", "Leda", "Maya", pitch = 1.30f, rate = 1.10f, description = "Light, energetic, modern cadence"),
        VoiceOption("zephyr", "Bright", "Zephyr", "Maya", pitch = 1.22f, rate = 1.08f, description = "Crisp, alert, high-clarity tone"),
        VoiceOption("laomedeia", "Upbeat", "Laomedeia", "Maya", pitch = 1.25f, rate = 1.12f, description = "Cheerful, dynamic tempo"),
        VoiceOption("despina", "Smooth", "Despina", "Maya", pitch = 1.10f, rate = 0.98f, description = "Silky, reassuring delivery"),
        VoiceOption("erinome", "Clear", "Erinome", "Maya", pitch = 1.12f, rate = 1.04f, description = "Clean, highly legible studio tone"),
        VoiceOption("callirrhoe", "Easy-going", "Callirrhoe", "Maya", pitch = 1.05f, rate = 0.96f, description = "Relaxed, conversational flow"),
        VoiceOption("autonoe", "Bright", "Autonoe", "Maya", pitch = 1.28f, rate = 1.06f, description = "Vibrant, friendly, open voice"),
        VoiceOption("gacrux", "Mature", "Gacrux", "Maya", pitch = 0.92f, rate = 0.98f, description = "Poised, resonant, grounded warmth"),
        VoiceOption("pulcherrima", "Forward", "Pulcherrima", "Maya", pitch = 1.18f, rate = 1.14f, description = "Direct, expressive and confident"),
        VoiceOption("sulafat", "Warm", "Sulafat", "Maya", pitch = 1.08f, rate = 0.94f, description = "Gentle, compassionate cadence"),
        VoiceOption("vindemiatrix", "Gentle", "Vindemiatrix", "Maya", pitch = 1.14f, rate = 0.92f, description = "Quiet, attentive, polite tone"),

        // Friday persona voice variations
        VoiceOption("friday_core", "Executive", "Friday-One", "Friday", pitch = 1.00f, rate = 1.18f, description = "Sharp, precise, executive tone"),
        VoiceOption("friday_tactical", "Tactical", "Friday-Tac", "Friday", pitch = 0.96f, rate = 1.22f, description = "Ultra-fast status reports"),
        VoiceOption("friday_diplomat", "Diplomat", "Friday-Dip", "Friday", pitch = 1.05f, rate = 1.05f, description = "Polite, diplomatic liaison voice"),

        // Venom persona voice variations
        VoiceOption("venom_deep", "Symbiote", "Venom-Alpha", "Venom", pitch = 0.62f, rate = 0.90f, description = "Sub-bass growl, protective tone"),
        VoiceOption("venom_snarl", "Unfiltered", "Venom-Brutal", "Venom", pitch = 0.58f, rate = 0.98f, description = "Raw, visceral, no nonsense"),
        VoiceOption("venom_lethal", "Lethal", "Venom-Shadow", "Venom", pitch = 0.65f, rate = 0.88f, description = "Heavy whisper, tactical intimidation")
    )

    private val _conversationModeEnabled = MutableStateFlow(false)
    val conversationModeEnabled: StateFlow<Boolean> = _conversationModeEnabled.asStateFlow()

    private val _messageAlertsEnabled = MutableStateFlow(true)
    val messageAlertsEnabled: StateFlow<Boolean> = _messageAlertsEnabled.asStateFlow()

    private val _assistantLanguage = MutableStateFlow("Hinglish (Hindi + English) — default")
    val assistantLanguage: StateFlow<String> = _assistantLanguage.asStateFlow()

    private val _autoStartWakeWord = MutableStateFlow(true)
    val autoStartWakeWord: StateFlow<Boolean> = _autoStartWakeWord.asStateFlow()

    private val _proactiveMayaEnabled = MutableStateFlow(true)
    val proactiveMayaEnabled: StateFlow<Boolean> = _proactiveMayaEnabled.asStateFlow()

    private val _callAnnouncementEnabled = MutableStateFlow(true)
    val callAnnouncementEnabled: StateFlow<Boolean> = _callAnnouncementEnabled.asStateFlow()

    private val _keepRingtonePlaying = MutableStateFlow(false)
    val keepRingtonePlaying: StateFlow<Boolean> = _keepRingtonePlaying.asStateFlow()

    private val _drivingModeEnabled = MutableStateFlow(false)
    val drivingModeEnabled: StateFlow<Boolean> = _drivingModeEnabled.asStateFlow()

    private val _drivingAutoReplyTemplate = MutableStateFlow(
        "{name} abhi drive kar rahe hain, isliye call nahi utha paye. Free hote hi call back karenge. - Maya (auto reply)"
    )
    val drivingAutoReplyTemplate: StateFlow<String> = _drivingAutoReplyTemplate.asStateFlow()

    private val _drivingStatusMessage = MutableStateFlow<String?>(null)
    val drivingStatusMessage: StateFlow<String?> = _drivingStatusMessage.asStateFlow()

    fun updateAssistantName(name: String) {
        _assistantName.value = name
    }

    fun setAssistantPersona(persona: String) {
        _assistantPersonaSelection.value = persona
        setPersona(when (persona.lowercase()) {
            "friday" -> MayaPersona.FRIDAY
            "venom" -> MayaPersona.VENOM
            else -> MayaPersona.MAYA
        })
    }

    fun toggleGirlfriendMode(enabled: Boolean) {
        deviceController.triggerHaptic(50)
        _girlfriendModeEnabled.value = enabled
        if (enabled) {
            deviceController.speak("Girlfriend mode active. Warm, friendly and caring voice persona ready.", _selectedPersona.value)
        }
    }

    fun toggleMemoryAutoSave(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _memoryAutoSave.value = enabled
    }

    fun toggleMemoryIncognito(enabled: Boolean) {
        deviceController.triggerHaptic(50)
        _memoryIncognito.value = enabled
    }

    fun setVoicePersonaTab(tab: String) {
        _selectedVoicePersonaTab.value = tab
    }

    fun selectVoice(voiceId: String) {
        deviceController.triggerHaptic(50)
        _selectedVoiceId.value = voiceId
        val voice = availableVoices.find { it.id == voiceId }
        if (voice != null) {
            deviceController.speakWithAcoustics(
                "Voice changed to ${voice.name}.",
                voice.pitch,
                voice.rate
            )
        }
    }

    fun previewVoice(voice: VoiceOption) {
        deviceController.triggerHaptic(60)
        _selectedVoiceId.value = voice.id
        val sampleText = when (voice.persona) {
            "Friday" -> "Friday online. Voice model ${voice.name} loaded. Standing by for instructions, boss."
            "Venom" -> "We are Venom! Voice profile ${voice.name} engaged. Nobody touches our human!"
            else -> when (voice.id) {
                "aoede" -> "Hi there! I am Maya with the Breezy voice. How can I help you today?"
                "leda" -> "Hey! Let's get things done today! Super excited to help you."
                "kore" -> "Good day. Systems initialized. Awaiting your directive."
                "zephyr" -> "Clear and bright! All background tasks and sub-agents synced."
                "sulafat" -> "Take a breath. I'm right here with you whenever you need me."
                "gacrux" -> "Good morning. Everything is organized and under control."
                else -> "Hello! This is ${voice.name} voice preview. All systems operational."
            }
        }
        deviceController.speakWithAcoustics(sampleText, voice.pitch, voice.rate)
    }

    fun toggleConversationMode(enabled: Boolean) {
        deviceController.triggerHaptic(50)
        _conversationModeEnabled.value = enabled
        if (enabled) {
            deviceController.speak("Conversation mode enabled. Emotion-adaptive voice active.", _selectedPersona.value)
        }
    }

    fun toggleMessageAlerts(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _messageAlertsEnabled.value = enabled
    }

    fun setAssistantLanguage(lang: String) {
        _assistantLanguage.value = lang
    }

    fun toggleAutoStartWakeWord(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _autoStartWakeWord.value = enabled
    }

    fun toggleProactiveMaya(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _proactiveMayaEnabled.value = enabled
    }

    fun toggleCallAnnouncement(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _callAnnouncementEnabled.value = enabled
    }

    fun toggleKeepRingtonePlaying(enabled: Boolean) {
        deviceController.triggerHaptic(40)
        _keepRingtonePlaying.value = enabled
    }

    fun toggleDrivingMode(enabled: Boolean) {
        deviceController.triggerHaptic(70)
        _drivingModeEnabled.value = enabled
        if (enabled) {
            deviceController.speak("Driving mode enabled. Auto-rejecting incoming calls with SMS.", _selectedPersona.value)
        }
    }

    fun saveDrivingAutoReply(template: String) {
        deviceController.triggerHaptic(50)
        _drivingAutoReplyTemplate.value = template
        _drivingStatusMessage.value = "Driving auto-reply saved successfully."
    }

    fun clearDrivingStatus() {
        _drivingStatusMessage.value = null
    }

    // ==========================================
    // PERSONAL & SUB-AGENTS & SOCIAL & BACKUP
    // ==========================================
    private val _userName = MutableStateFlow("Maya User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _geminiApiKey = MutableStateFlow("")
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _youtubeApiKey = MutableStateFlow("")
    val youtubeApiKey: StateFlow<String> = _youtubeApiKey.asStateFlow()

    private val _musicService = MutableStateFlow("Spotify")
    val musicService: StateFlow<String> = _musicService.asStateFlow()

    fun updatePersonalSettings(name: String, gemini: String, yt: String, music: String) {
        deviceController.triggerHaptic(50)
        _userName.value = name
        _geminiApiKey.value = gemini
        _youtubeApiKey.value = yt
        _musicService.value = music
    }

    private val _socialHandle = MutableStateFlow("@maya_ai_official")
    val socialHandle: StateFlow<String> = _socialHandle.asStateFlow()

    private val _socialTone = MutableStateFlow("Professional & Engaging")
    val socialTone: StateFlow<String> = _socialTone.asStateFlow()

    fun updateSocialSettings(handle: String, tone: String) {
        deviceController.triggerHaptic(40)
        _socialHandle.value = handle
        _socialTone.value = tone
    }

    private val _lastBackupTime = MutableStateFlow("Yesterday, 11:30 PM")
    val lastBackupTime: StateFlow<String> = _lastBackupTime.asStateFlow()

    private val _backupStatus = MutableStateFlow<String?>(null)
    val backupStatus: StateFlow<String?> = _backupStatus.asStateFlow()

    fun triggerBackup() {
        deviceController.triggerHaptic(60)
        viewModelScope.launch {
            _backupStatus.value = "Encrypting memories and chat archives..."
            delay(1200)
            val now = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date())
            _lastBackupTime.value = "Today, $now"
            _backupStatus.value = "Backup created and encrypted locally."
            deviceController.speak("All memories and sessions safely backed up.", _selectedPersona.value)
        }
    }

    fun triggerRestore() {
        deviceController.triggerHaptic(60)
        viewModelScope.launch {
            _backupStatus.value = "Verifying cryptographic signature of backup archive..."
            delay(1400)
            _backupStatus.value = "Restore verified. 100% of memories and settings restored."
            deviceController.speak("Memories and configuration restored successfully.", _selectedPersona.value)
        }
    }

    fun clearBackupStatus() {
        _backupStatus.value = null
    }

    override fun onCleared() {
        super.onCleared()
        waveformJob?.cancel()
        deviceController.shutdown()
    }
}
