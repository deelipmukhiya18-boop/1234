package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MayaMemory
import com.example.data.model.MayaRule
import com.example.data.model.PairedSession
import com.example.data.model.PhoneAction

@Database(
    entities = [PairedSession::class, PhoneAction::class, MayaMemory::class, MayaRule::class],
    version = 2,
    exportSchema = false
)
abstract class MayaDatabase : RoomDatabase() {
    abstract fun mayaDao(): MayaDao

    companion object {
        @Volatile
        private var INSTANCE: MayaDatabase? = null

        fun getDatabase(context: Context): MayaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MayaDatabase::class.java,
                    "maya_companion_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
