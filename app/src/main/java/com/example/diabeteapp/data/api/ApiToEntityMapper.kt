package com.example.diabeteapp.data.api

import FoodItem

fun ApiFood.toFoodItem(defaultPortionG: Float = 100f): FoodItem {
    // Helper function pour gérer les valeurs nullable des constituents
    fun getNutrientValue(nutrientId: String): Float {
        return this.constituents
            ?.find { it.nutrientId == nutrientId }
            ?.quantity
            ?: 0f // Fallback à 0 si null ou non trouvé
    }

    return FoodItem(
        foodId = this.foodId?: "",
        name = this.foodName?: "",
        latinName = this.latinName ?: "", // Fallback pour String nullable
        foodGroupId = this.foodGroupId?: "",
        defaultPortionG = defaultPortionG,
        selectedPortionG = defaultPortionG,
        energyKcal = this.calories?.quantity ?: 0f,
        energyKj = this.energy?.quantity ?: 0f,
        proteins = getNutrientValue("Protein"),
        fats = getNutrientValue("Fett"),
        carbohydrates = getNutrientValue("Karbo"),
        fiber = this.constituents?.find { it.nutrientId == "Fiber" }?.quantity, // Peut rester nullable si FoodItem le permet
        ediblePartPercent = this.ediblePart?.percent ?: 100,
        uri = this.uri ?: "" // Fallback pour String nullable
    )
}