package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paired_sessions")
data class PairedSession(
    @PrimaryKey val id: Int = 1,
    val desktopName: String = "Hunter-Rig (Windows 11)",
    val desktopIp: String = "192.168.1.142:8765",
    val sessionToken: String = "MAYA-608-X7K",
    val isConnected: Boolean = true,
    val lastPingMs: Long = 18,
    val agentCount: Int = 7,
    val toolsEnabledCount: Int = 234,
    val pairedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "phone_actions")
data class PhoneAction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val actionType: String, // "OTP_READ", "APP_LAUNCH", "DUET_CALL", "TELEMETRY", "EXPENSE", "VOICE_REMINDER"
    val title: String,
    val details: String,
    val source: String, // "Desktop Maya", "Phone Maya", "Voice Command"
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Executed"
)

@Entity(tableName = "maya_memories")
data class MayaMemory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "Personal", "Work & Project", "Finance", "Daily Habits"
    val factText: String,
    val keyTag: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "maya_rules")
data class MayaRule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ruleText: String,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class MayaPersona(
    val title: String,
    val tag: String,
    val description: String,
    val speechPitch: Float,
    val speechRate: Float
) {
    MAYA("Maya", "Warm & Familiar", "Warm, witty, intuitive companion with conversational tone.", 1.15f, 1.05f),
    FRIDAY("Friday", "Crisp & Professional", "Crisp, concise, precision-focused executive voice assistant.", 1.0f, 1.15f),
    VENOM("Venom", "Symbiote & Blunt", "Deep-voiced, fiercely loyal, blunt symbiote persona.", 0.65f, 0.95f)
}
