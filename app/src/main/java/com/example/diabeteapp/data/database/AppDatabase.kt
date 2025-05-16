package com.example.diabeteapp.data.database
import com.example.diabeteapp.data.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.diabeteapp.*
import com.example.diabeteapp.data.dao.*

@Database(
    entities = [
        User::class,
        Meal::class,
        FoodItem::class,
        Advice::class,
        UserMealCrossRef::class,
        MealFoodCrossRef::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun adviceDao(): AdviceDao
    abstract fun userMealCrossRefDao(): UserMealCrossRefDao
    abstract fun mealFoodCrossRefDao(): MealFoodCrossRefDao
}
