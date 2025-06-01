package com.example.diabeteapp.data.repository

import com.example.diabeteapp.data.dao.HealthTargetDao
import com.example.diabeteapp.data.entity.HealthTarget
import kotlinx.coroutines.flow.Flow

class HealthTargetRepository(
    private val healthTargetDao: HealthTargetDao
) {
    fun getAllTargets(): Flow<List<HealthTarget>> = healthTargetDao.getAllTargets()

    fun getSelectedRingTarget(): Flow<HealthTarget?> = healthTargetDao.getSelectedRingTarget()

    suspend fun insertTarget(target: HealthTarget) = healthTargetDao.insertTarget(target)

    suspend fun updateTarget(target: HealthTarget) = healthTargetDao.updateTarget(target)

    suspend fun deleteTarget(target: HealthTarget) = healthTargetDao.deleteTarget(target)

    suspend fun deleteTargetById(id: String) = healthTargetDao.deleteTargetById(id)

    suspend fun setRingTarget(targetId: String) = healthTargetDao.updateRingSelection(targetId)

    suspend fun getTargetById(id: String): HealthTarget? = healthTargetDao.getTargetById(id)
}