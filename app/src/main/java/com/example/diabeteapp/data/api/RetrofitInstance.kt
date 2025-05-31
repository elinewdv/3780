package com.example.diabeteapp.data.api

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()) // Utilisez addLast pour Kotlin
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FoodApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: FoodApiService by lazy {
        retrofit.create(FoodApiService::class.java)
    }
}