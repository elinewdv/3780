package com.example.diabeteapp.data.dao

import FoodItem
import Meal
import androidx.room.*

// Data class to replace Pair for query results
data class FoodItemWithPortion(
    @Embedded val foodItem: FoodItem,
    val customPortionG: Float
)

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Query("SELECT * FROM meals WHERE userId = :userId ORDER BY date DESC")
    suspend fun getMealsByUser(userId: String): List<Meal>

    @Query("SELECT * FROM meals WHERE mealId = :mealId AND userId = :userId")
    suspend fun getMealById(mealId: Long, userId: String): Meal?

    @Query("""
        SELECT food_items.*, meal_food_cross_ref.customPortionG 
        FROM meal_food_cross_ref
        INNER JOIN food_items ON meal_food_cross_ref.foodId = food_items.foodId
        WHERE meal_food_cross_ref.mealId = :mealId
    """)
    suspend fun getFoodsForMeal(mealId: Long): List<FoodItemWithPortion>

    @Delete
    suspend fun deleteMeal(meal: Meal)
}