package com.example.diabeteapp.data.dao

import androidx.room.*
import com.example.diabeteapp.data.entity.HealthTarget
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthTargetDao {
    @Query("SELECT * FROM health_targets ORDER BY createdAt ASC")
    fun getAllTargets(): Flow<List<HealthTarget>>

    @Query("SELECT * FROM health_targets WHERE isSelectedForRing = 1 LIMIT 1")
    fun getSelectedRingTarget(): Flow<HealthTarget?>

    @Query("SELECT * FROM health_targets WHERE id = :id")
    suspend fun getTargetById(id: String): HealthTarget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarget(target: HealthTarget)

    @Update
    suspend fun updateTarget(target: HealthTarget)

    @Delete
    suspend fun deleteTarget(target: HealthTarget)

    @Query("DELETE FROM health_targets WHERE id = :id")
    suspend fun deleteTargetById(id: String)

    @Query("UPDATE health_targets SET isSelectedForRing = 0")
    suspend fun clearAllRingSelections()

    @Query("UPDATE health_targets SET isSelectedForRing = 1 WHERE id = :id")
    suspend fun setRingTarget(id: String)

    @Transaction
    suspend fun updateRingSelection(targetId: String) {
        clearAllRingSelections()
        setRingTarget(targetId)
    }
}