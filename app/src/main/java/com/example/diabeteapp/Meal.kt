package com.example.diabeteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey val mealId: String,
    val dateTime: String // ou `Date` + TypeConverter
)
