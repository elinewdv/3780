package com.example.diabeteapp
import FoodItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import Meal
import com.example.diabeteapp.data.dao.MealDao
import com.example.diabeteapp.data.dao.MealFoodCrossRefDao
import MealFoodCrossRef
import com.example.diabeteapp.data.FoodRepository

class FoodViewModel(
    private val foodRepository: FoodRepository,
    private val mealDao: MealDao,
    private val mealFoodCrossRefDao: MealFoodCrossRefDao
) : ViewModel() {

    // État de recherche des aliments
    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults: StateFlow<List<FoodItem>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // État du repas en cours de création
    private val _selectedFoods = MutableStateFlow<Map<FoodItem, Float>>(emptyMap())
    val selectedFoods: StateFlow<Map<FoodItem, Float>> = _selectedFoods.asStateFlow()

    private val _mealName = MutableStateFlow("")
    val mealName: StateFlow<String> = _mealName.asStateFlow()

    // État des nutriments calculés
    private val _nutritionSummary = MutableStateFlow(NutritionSummary())
    val nutritionSummary: StateFlow<NutritionSummary> = _nutritionSummary.asStateFlow()

    // État de sauvegarde
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccess = MutableStateFlow<Boolean?>(null)
    val saveSuccess: StateFlow<Boolean?> = _saveSuccess.asStateFlow()

    init {
        // Calcul automatique des nutriments quand les aliments sélectionnés changent
        viewModelScope.launch {
            _selectedFoods.collect { foods ->
                _nutritionSummary.value = calculateNutritionSummary(foods)
            }
        }
    }

    fun searchFoods(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isSearching.value = true
                val results = foodRepository.searchAndSaveFoods(query)
                _searchResults.value = results
            } catch (e: Exception) {
                // Handle error - vous pouvez ajouter un état d'erreur si nécessaire
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun addFoodToMeal(foodItem: FoodItem, portionG: Float = 100f) {
        val updatedFoodItem = foodItem.copy(selectedPortionG = portionG)
        val currentFoods = _selectedFoods.value.toMutableMap()
        currentFoods[updatedFoodItem] = portionG
        _selectedFoods.value = currentFoods
    }

    fun removeFoodFromMeal(foodItem: FoodItem) {
        val currentFoods = _selectedFoods.value.toMutableMap()
        currentFoods.remove(foodItem)
        _selectedFoods.value = currentFoods
    }

    fun updateFoodPortion(foodItem: FoodItem, newPortionG: Float) {
        if (newPortionG <= 0) {
            removeFoodFromMeal(foodItem)
            return
        }

        val updatedFoodItem = foodItem.copy(selectedPortionG = newPortionG)
        val currentFoods = _selectedFoods.value.toMutableMap()
        currentFoods.remove(foodItem) // Remove old entry
        currentFoods[updatedFoodItem] = newPortionG // Add updated entry
        _selectedFoods.value = currentFoods
    }

    fun setMealName(name: String) {
        _mealName.value = name
    }

    fun saveMeal(userId: Long) {
        viewModelScope.launch {
            try {
                _isSaving.value = true

                // Créer le repas
                val meal = Meal(
                    name = _mealName.value.ifEmpty { "Repas du ${java.time.LocalDateTime.now()}" },
                    userId = userId
                )

                // Insérer le repas et récupérer l'ID
                mealDao.insertMeal(meal)

                // Récupérer le repas inséré pour avoir l'ID généré
                val savedMeal = mealDao.getAllMeals().lastOrNull { it.name == meal.name && it.userId == userId }

                savedMeal?.let { mealWithId ->
                    // Créer les relations aliment-repas
                    _selectedFoods.value.forEach { (foodItem, portionG) ->
                        val crossRef = MealFoodCrossRef(
                            mealId = mealWithId.mealId,
                            foodId = foodItem.foodId,
                            customPortionG = portionG
                        )
                        mealFoodCrossRefDao.insertCrossRef(crossRef)
                    }
                }

                _saveSuccess.value = true
                clearMeal()

            } catch (e: Exception) {
                _saveSuccess.value = false
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearSaveStatus() {
        _saveSuccess.value = null
    }

    fun clearMeal() {
        _selectedFoods.value = emptyMap()
        _mealName.value = ""
        _searchResults.value = emptyList()
    }

    private fun calculateNutritionSummary(foods: Map<FoodItem, Float>): NutritionSummary {
        var totalCalories = 0f
        var totalProteins = 0f
        var totalFats = 0f
        var totalCarbs = 0f
        var totalFiber = 0f

        foods.forEach { (foodItem, portionG) ->
            val factor = portionG / 100f
            totalCalories += foodItem.energyKcal * factor
            totalProteins += foodItem.proteins * factor
            totalFats += foodItem.fats * factor
            totalCarbs += foodItem.carbohydrates * factor
            totalFiber += (foodItem.fiber ?: 0f) * factor
        }

        return NutritionSummary(
            totalCalories = totalCalories,
            totalProteins = totalProteins,
            totalFats = totalFats,
            totalCarbohydrates = totalCarbs,
            totalFiber = totalFiber
        )
    }
}

data class NutritionSummary(
    val totalCalories: Float = 0f,
    val totalProteins: Float = 0f,
    val totalFats: Float = 0f,
    val totalCarbohydrates: Float = 0f,
    val totalFiber: Float = 0f
)

