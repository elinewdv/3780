package com.example.diabeteapp.data.dao

import Meal
import androidx.room.*


@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Query("SELECT * FROM meals WHERE userId = :userId ORDER BY date DESC")
    suspend fun getMealsByUser(userId: String): List<Meal>

    @Query("SELECT * FROM meals WHERE mealId = :mealId AND userId = :userId")
    suspend fun getMealById(mealId: Long, userId: String): Meal?

    @Delete
    suspend fun deleteMeal(meal: Meal)
}
