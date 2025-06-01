package com.example.diabeteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diabeteapp.data.entity.HealthTarget
import com.example.diabeteapp.data.repository.HealthTargetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthTargetViewModel(
    private val repository: HealthTargetRepository
) : ViewModel() {

    val allTargets = repository.getAllTargets()
    val selectedRingTarget = repository.getSelectedRingTarget()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun addTarget(target: HealthTarget) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.insertTarget(target)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTarget(target: HealthTarget) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateTarget(target)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTarget(target: HealthTarget) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteTarget(target)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setRingTarget(targetId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.setRingTarget(targetId)
            } finally {
                _isLoading.value = false
            }
        }
    }
}