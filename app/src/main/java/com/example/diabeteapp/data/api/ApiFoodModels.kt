package com.example.diabeteapp.data.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFoodResponse(
    val foods: List<ApiFood>
)


@JsonClass(generateAdapter = true)
data class ApiFood(
    val foodId: String?,
    val foodName: String?,
    val latinName: String?,
    val calories: NutrientEntry?,
    val energy: NutrientEntry?,
    val constituents: List<NutrientConstituent>?,
    val portions: List<Portion>?,
    val ediblePart: EdiblePart?,
    val uri: String?,
    val foodGroupId: String?
)

@JsonClass(generateAdapter = true)
data class NutrientEntry(
    val quantity: Float?,
    val unit: String?
)

@JsonClass(generateAdapter = true)
data class NutrientConstituent(
    val nutrientId: String?,
    val quantity: Float?, // Champ rendu nullable
    val unit: String?
)

@JsonClass(generateAdapter = true)
data class Portion(
    val portionName: String?,
    val quantity: Float?, // Champ rendu nullable
    val unit: String?
)

@JsonClass(generateAdapter = true)
data class EdiblePart(
    val percent: Int?
)