package com.example.diabeteapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Embedded
import FoodItem
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.diabeteapp.MealFoodCrossRef

// Classe de données pour remplacer le Pair
data class FoodWithPortion(
    @Embedded val foodItem: FoodItem,
    val customPortionG: Float
)

@Dao
interface MealFoodCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: MealFoodCrossRef)

    @Query("DELETE FROM meal_food_cross_ref WHERE mealId = :mealId")
    suspend fun deleteCrossRefsForMeal(mealId: Long)

    @Query("""
        SELECT food_items.*, meal_food_cross_ref.customPortionG 
        FROM meal_food_cross_ref
        INNER JOIN food_items ON meal_food_cross_ref.foodId = food_items.foodId
        WHERE meal_food_cross_ref.mealId = :mealId
    """)
    suspend fun getFoodsForMeal(mealId: Long): List<FoodWithPortion>
}