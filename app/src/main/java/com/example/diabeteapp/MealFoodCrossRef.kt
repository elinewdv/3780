package com.example.diabeteapp

import FoodItem
import Meal
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "meal_food_cross_ref",
    primaryKeys = ["mealId", "foodId"],
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["mealId"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["foodId"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
            indices = [Index(value = ["foodId"])] // Ajoutez cette ligne
)
data class MealFoodCrossRef(
    val mealId: Long,
    val foodId: String,
    val customPortionG: Float
)