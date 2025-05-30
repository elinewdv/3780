package com.example.diabeteapp.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "diabeteapp-database"
            )
                .fallbackToDestructiveMigration(true) // Ajouté pour le développement
                .build()
            INSTANCE = instance
            instance
        }
    }
}