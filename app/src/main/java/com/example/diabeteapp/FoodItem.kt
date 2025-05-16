package com.example.diabeteapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey val foodId: String,
    val name: String,
    val energyKcal: Float,
    val carbohydratesG: Float,
    val proteinsG: Float,
    val fatsG: Float,
    val fiberG: Float,
    val portionSizeG: Float
)
