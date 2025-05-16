package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.FoodItem

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(item: FoodItem)

    @Query("SELECT * FROM food_items")
    suspend fun getAllFoodItems(): List<FoodItem>

    @Query("SELECT * FROM food_items WHERE foodId = :id")
    suspend fun getFoodItemById(id: String): FoodItem?

    @Delete
    suspend fun deleteFoodItem(item: FoodItem)
}
