package com.example.diabeteapp.data

import com.example.diabeteapp.data.api.FoodApiService
import FoodItem
import FoodItemDao
import android.util.Log
import com.example.diabeteapp.data.api.ApiFoodResponse
import com.example.diabeteapp.data.api.toFoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FoodRepository(
    private val apiService: FoodApiService,
    private val foodItemDao: FoodItemDao
) {
    suspend fun searchAndSaveFoods(query: String): List<FoodItem> {
        val apiResponse = apiService.searchFoods(query)
        val foodItems = apiResponse.foods.map { it.toFoodItem() }


        foodItemDao.insertAll(foodItems)

        return foodItems
    }

        suspend fun forceApiCall(): ApiFoodResponse {
            return withContext(Dispatchers.IO) {
                apiService.getAllFoods()
            }
        }
    suspend fun searchLocally(query: String): List<FoodItem> {
        return foodItemDao.searchLocally(query)
    }
    suspend fun insertFoodItems(foodItems: List<FoodItem>) {
        withContext(Dispatchers.IO) {
            foodItemDao.insertAll(foodItems)
        }
    }
}