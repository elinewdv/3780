package com.example.diabeteapp.data.dao

import androidx.room.*
import MealFoodCrossRef

@Dao
interface MealFoodCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFoodCrossRef(crossRef: MealFoodCrossRef)

    //@Query("SELECT * FROM meal_food_cross_ref WHERE mealId = :mealId")
    //suspend fun getCrossRefsForMeal(mealId: String): List<MealFoodCrossRef>

    @Delete
    suspend fun deleteMealFoodCrossRef(crossRef: MealFoodCrossRef)
}
