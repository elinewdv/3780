package com.example.diabeteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advices")
data class Advice(
    @PrimaryKey val adviceId: String,
    val title: String,
    val content: String,
    val dateAdded: String
)
