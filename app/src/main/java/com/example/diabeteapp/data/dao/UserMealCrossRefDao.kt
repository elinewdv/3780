package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.UserMealCrossRef

@Dao
interface UserMealCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMealCrossRef(crossRef: UserMealCrossRef)
}
