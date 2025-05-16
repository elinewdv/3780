package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.MealFoodCrossRef

@Dao
interface MealFoodCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFoodCrossRef(crossRef: MealFoodCrossRef)
}
