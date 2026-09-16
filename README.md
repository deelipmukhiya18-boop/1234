# Maya AI Companion — Android Application

A voice-first AI companion Android application built with **Jetpack Compose**, **Kotlin**, **Room Database**, **Google Gemini AI**, and deep **Android System Automation**.

---

## 🌟 Key Features

- **🎙️ Voice-First Live Assistant**:
  - Powered by Google Gemini 2.5 Flash for high-speed voice and chat answers.
  - Multi-persona support (**Maya Original**, **JARVIS Mode**, **Friday Intelligence**, **Ghost Cyberpunk**, **Hindi Companion**).
  - Real-time acoustic pitch, rate, and speech synthesis via Android Text-to-Speech (TTS).

- **📲 Full Android Device Automation**:
  - **Phone Dialer & Calls**: Direct dialer launch and contact calls.
  - **WhatsApp & SMS Messaging**: Pre-filled quick chats and message composers.
  - **Camera & Torch**: One-tap camera launch, flashlight toggle, and emergency strobe beacon.
  - **Alarm & Timers**: System alarm clock and countdown timer automation.
  - **Media & Volume Controls**: Spotify, YouTube playback, volume adjustment, and instant mute.
  - **System Settings**: Wi-Fi, Bluetooth, Display, Sound settings, and Vibrate/Normal ringer toggles.
  - **Google Web Search**: Hands-free voice searches and web lookups.
  - **Calendar Scheduling**: Direct event creation and reminder scheduling.
  - **Email Client**: One-tap email composer for Gmail and default clients.

- **💻 PC ⇄ Phone Link (Desktop HUD Remote)**:
  - 6-digit pairing code & QR synchronization.
  - Real-time status reporting: Device battery, Wi-Fi latency, charging state, active tools (234 tools across 7 agents).
  - Remote Phone Control HUD grid with live sync history.

- **🛡️ Security & Privacy Suites**:
  - **Touch Guard**: Anti-theft motion detector with siren, vibration alert, and PIN lock.
  - **Emergency SOS**: Multi-tap SOS alert with location coordinates and emergency beacon.
  - **Memory Vault**: Persistent local memories across sessions using Room Database.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Repository pattern
- **Local Persistence**: Android Room Database with SQLite & KSP
- **Networking & API**: Retrofit, OkHttp, Moshi, Google Gemini API
- **Hardware Integration**: Camera2 Manager, AudioManager, Telephony, SensorManager, Vibrator

---

## 🚀 Setup & Installation (Android Studio / GitHub)

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/maya-android.git
cd maya-android
```

### 2. Configure API Key
Create a `.env` file in the project root directory (based on `.env.example`):
```env
GEMINI_API_KEY=your_actual_gemini_api_key_here
```
> Get a free API key from [Google AI Studio](https://aistudio.google.com/).

### 3. Open and Run in Android Studio
1. Open **Android Studio** (Ladybug / Meerkat or newer recommended).
2. Select **Open** and choose the `maya-android` project directory.
3. Allow Gradle to sync dependencies.
4. Select your connected device or Android Virtual Device (AVD with API 24+).
5. Click **Run (`Shift + F10`)**.

### 4. Build APK via Command Line
```bash
# Debug APK:
./gradlew assembleDebug

# Output APK will be located at:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
