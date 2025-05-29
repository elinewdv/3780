package com.example.diabeteapp.data.api

interface FoodApiService {
    @GET("api/en/foods.json")
    suspend fun getFoods(): ApiFoodResponse

    companion object {
        const val BASE_URL = "https://www.matvaretabellen.no/"
    }
}

annotation class GET(val value: String)
