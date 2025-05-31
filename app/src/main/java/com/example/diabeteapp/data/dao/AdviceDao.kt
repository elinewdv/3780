package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.Advice

@Dao
interface AdviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdvice(advice: Advice)

    @Query("SELECT * FROM advices")
    suspend fun getAllAdvices(): List<Advice>

    @Query("SELECT * FROM advices WHERE adviceId = :id")
    suspend fun getAdviceById(id: String): Advice?

    @Delete
    suspend fun deleteAdvice(advice: Advice)
}
