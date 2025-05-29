package com.example.diabeteapp.data.api

data class ApiFoodResponse(
    val foods: List<ApiFood>
)

data class ApiFood(
    val foodId: String,
    val foodName: String,
    val latinName: String?,
    val calories: NutrientEntry,
    val energy: NutrientEntry,
    val constituents: List<NutrientConstituent>,
    val portions: List<Portion>,
    val ediblePart: EdiblePart,
    val uri: String?,
    val foodGroupId: String?
)

data class NutrientEntry(
    val quantity: Float,
    val unit: String
)

data class NutrientConstituent(
    val nutrientId: String,
    val quantity: Float?,
    val unit: String?
)

data class Portion(
    val portionName: String,
    val quantity: Float,
    val unit: String
)

data class EdiblePart(
    val percent: Int
)