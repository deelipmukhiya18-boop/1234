package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MayaMemory
import com.example.data.model.PairedSession
import com.example.data.model.PhoneAction
import kotlinx.coroutines.flow.Flow

@Dao
interface MayaDao {
    // Paired Session
    @Query("SELECT * FROM paired_sessions WHERE id = 1 LIMIT 1")
    fun getPairedSession(): Flow<PairedSession?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePairedSession(session: PairedSession)

    @Update
    suspend fun updatePairedSession(session: PairedSession)

    // Actions log
    @Query("SELECT * FROM phone_actions ORDER BY timestamp DESC")
    fun getAllActions(): Flow<List<PhoneAction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: PhoneAction)

    @Query("DELETE FROM phone_actions")
    suspend fun clearAllActions()

    // Maya Memory
    @Query("SELECT * FROM maya_memories ORDER BY createdAt DESC")
    fun getAllMemories(): Flow<List<MayaMemory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MayaMemory)

    @Delete
    suspend fun deleteMemory(memory: MayaMemory)

    // Maya Rules
    @Query("SELECT * FROM maya_rules ORDER BY createdAt ASC")
    fun getAllRules(): Flow<List<com.example.data.model.MayaRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: com.example.data.model.MayaRule): Long

    @Delete
    suspend fun deleteRule(rule: com.example.data.model.MayaRule)

    @Update
    suspend fun updateRule(rule: com.example.data.model.MayaRule)
}
