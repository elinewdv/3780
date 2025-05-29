package com.example.diabeteapp

import androidx.room.Entity

@Entity(
    tableName = "meal_food_cross_ref",
    primaryKeys = ["mealId", "foodId"]
)
data class MealFoodCrossRef(
    val mealId: String,
    val foodId: String,
    val portionSizeG: Float // Portion réelle consommée dans ce repas
)
