    package com.example.diabeteapp.data.dao

    import MealFoodCrossRef
    import androidx.room.*

    @Dao
    interface MealFoodCrossRefDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertCrossRef(crossRef: MealFoodCrossRef)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(crossRefs: List<MealFoodCrossRef>)

        @Query("SELECT * FROM MealFoodCrossRef WHERE mealId = :mealId")
        suspend fun getFoodsByMealId(mealId: Long): List<MealFoodCrossRef>

        @Query("SELECT * FROM MealFoodCrossRef WHERE foodId = :foodId")
        suspend fun getMealsByFoodId(foodId: String): List<MealFoodCrossRef>

        @Delete
        suspend fun delete(crossRef: MealFoodCrossRef)

        @Query("DELETE FROM MealFoodCrossRef WHERE mealId = :mealId")
        suspend fun deleteByMealId(mealId: Long)
    }