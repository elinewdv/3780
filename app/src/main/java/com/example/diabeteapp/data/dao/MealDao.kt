package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<Meal>

    @Query("SELECT * FROM meals WHERE mealId = :id")
    suspend fun getMealById(id: String): Meal?

    @Delete
    suspend fun deleteMeal(meal: Meal)
}
