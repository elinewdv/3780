package com.example.diabeteapp.data.api

import FoodItem

fun ApiFood.toFoodItem(defaultPortionG: Float = 100f): FoodItem {
    return FoodItem(
        foodId = this.foodId,
        name = this.foodName,
        latinName = this.latinName,
        foodGroupId = this.foodGroupId,
        defaultPortionG = defaultPortionG,
        selectedPortionG = defaultPortionG,
        energyKcal = this.calories.quantity,
        energyKj = this.energy.quantity,
        proteins = this.constituents.find { it.nutrientId == "Protein" }?.quantity ?: 0f,
        fats = this.constituents.find { it.nutrientId == "Fett" }?.quantity ?: 0f,
        carbohydrates = this.constituents.find { it.nutrientId == "Karbo" }?.quantity ?: 0f,
        fiber = this.constituents.find { it.nutrientId == "Fiber" }?.quantity,
        ediblePartPercent = this.ediblePart.percent,
        uri = this.uri
    )
}