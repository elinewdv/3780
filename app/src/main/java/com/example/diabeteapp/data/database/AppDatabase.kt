package com.example.diabeteapp.data.database
import FoodItem
import FoodItemDao
import Meal
import MealFoodCrossRef
import com.example.diabeteapp.data.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.diabeteapp.*

import com.example.diabeteapp.data.dao.*
import android.content.Context


@Database(
    entities = [
        User::class,
        Meal::class,
        FoodItem::class,
        Advice::class,
        UserMealCrossRef::class,
        MealFoodCrossRef::class
    ],
    version = 3
)
@TypeConverters(
    Converters::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun adviceDao(): AdviceDao
    abstract fun userMealCrossRefDao(): UserMealCrossRefDao
    abstract fun mealFoodCrossRefDao(): MealFoodCrossRefDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "diabete_database"
                ).fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}