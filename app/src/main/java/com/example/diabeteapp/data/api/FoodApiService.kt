package com.example.diabeteapp.data.api


import retrofit2.http.GET
import retrofit2.http.Query
interface FoodApiService {
    @GET("api/en/foods.json")
    suspend fun getAllFoods(): ApiFoodResponse

    @GET("api/en/foods/search")
    suspend fun searchFoods(@Query("q") query: String): ApiFoodResponse

    companion object {
        const val BASE_URL = "https://www.matvaretabellen.no/"
    }
}