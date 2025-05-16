package com.example.diabeteapp
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String, // ISO 8601 string ou utilise un TypeConverter pour `Date`
    val diabetesType: String // "Type 1", "Type 2", etc.
)
