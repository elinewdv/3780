package com.example.diabeteapp

import androidx.room.Entity

@Entity(
    tableName = "user_meal_cross_ref",
    primaryKeys = ["userId", "mealId"]
)
data class UserMealCrossRef(
    val userId: String,
    val mealId: String
)
