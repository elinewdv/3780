package com.example.diabeteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val email: String,
    val username: String,
    val password: String,
    val age: String,
    val diabetesType: String
)