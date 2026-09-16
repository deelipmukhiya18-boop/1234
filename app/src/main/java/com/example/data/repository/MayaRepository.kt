package com.example.data.repository

import com.example.data.db.MayaDao
import com.example.data.model.MayaMemory
import com.example.data.model.PairedSession
import com.example.data.model.PhoneAction
import kotlinx.coroutines.flow.Flow

class MayaRepository(private val dao: MayaDao) {

    val sessionFlow: Flow<PairedSession?> = dao.getPairedSession()
    val actionsFlow: Flow<List<PhoneAction>> = dao.getAllActions()
    val memoriesFlow: Flow<List<MayaMemory>> = dao.getAllMemories()
    val rulesFlow: Flow<List<com.example.data.model.MayaRule>> = dao.getAllRules()

    suspend fun initializeDefaultsIfEmpty() {
        val initialSession = PairedSession(
            id = 1,
            desktopName = "Hunter-Studio (Win 11 Pro)",
            desktopIp = "192.168.1.108:8765",
            sessionToken = "MAYA-608-LIVE-HUB",
            isConnected = true,
            lastPingMs = 12,
            agentCount = 7,
            toolsEnabledCount = 234
        )
        dao.savePairedSession(initialSession)

        // Prepopulate default persistent memories if empty
        val defaultMemories = listOf(
            MayaMemory(
                category = "Personal",
                keyTag = "Name & Identity",
                factText = "User is building advanced software projects; values fast voice execution and minimal keyboard friction."
            ),
            MayaMemory(
                category = "Work & Project",
                keyTag = "Ongoing Work",
                factText = "Working on Maya 6.0 desktop suite & Android Phone Link integration with Hermes agent."
            ),
            MayaMemory(
                category = "Daily Habits",
                keyTag = "Morning Briefing",
                factText = "Scheduled morning briefing macro at 08:40 AM with weather, pending PRs, and channel analytics."
            ),
            MayaMemory(
                category = "Finance",
                keyTag = "Expense Tracking",
                factText = "Auto-monitors UPI transaction receipts and monthly bills from inbox and SMS alerts."
            )
        )
        defaultMemories.forEach { dao.insertMemory(it) }

        // Prepopulate sample phone actions
        val initialActions = listOf(
            PhoneAction(
                actionType = "OTP_READ",
                title = "HDFC Bank NetBanking OTP",
                details = "Extracted OTP: 739201 for INR 4,250 transfer. Spoken to Desktop Maya.",
                source = "Desktop Maya",
                status = "Executed"
            ),
            PhoneAction(
                actionType = "APP_LAUNCH",
                title = "Launch Instagram",
                details = "Remote command received from PC HUD: 'Open Instagram on my phone'.",
                source = "Desktop Maya",
                status = "Executed"
            ),
            PhoneAction(
                actionType = "DUET_CALL",
                title = "P2P Duet Voice Call",
                details = "Desktop Maya and Phone Maya synchronized acoustic duplex channel.",
                source = "Phone Maya",
                status = "Completed"
            ),
            PhoneAction(
                actionType = "TELEMETRY",
                title = "Battery & Health Broadcast",
                details = "Battery 84% (Charging), Wi-Fi Link 5GHz, Low Latency 12ms.",
                source = "Phone Link",
                status = "Transferred"
            )
        )
        initialActions.forEach { dao.insertAction(it) }
    }

    suspend fun saveSession(session: PairedSession) = dao.savePairedSession(session)

    suspend fun insertAction(action: PhoneAction) = dao.insertAction(action)

    suspend fun clearActions() = dao.clearAllActions()

    suspend fun insertMemory(memory: MayaMemory) = dao.insertMemory(memory)

    suspend fun deleteMemory(memory: MayaMemory) = dao.deleteMemory(memory)

    suspend fun insertRule(rule: com.example.data.model.MayaRule) = dao.insertRule(rule)

    suspend fun deleteRule(rule: com.example.data.model.MayaRule) = dao.deleteRule(rule)

    suspend fun updateRule(rule: com.example.data.model.MayaRule) = dao.updateRule(rule)
}
