package com.example.diabeteapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diabeteapp.data.dao.FoodWithPortion
import java.time.format.DateTimeFormatter

@Composable
fun MealDetailsDialog(
    mealWithFoods: MealWithFoods,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(mealWithFoods.meal.name)
                Text(
                    text = mealWithFoods.meal.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        text = {
            Column {
                // Liste des aliments avec leurs portions
                Text("Food:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                mealWithFoods.foods.forEach { (food, portion) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("- ${food.name}")
                        Text("${portion.toInt()}g")
                    }
                    Text(
                        text = "${(food.energyKcal * portion / 100).toInt()} kcal",
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Résumé nutritionnel
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nutritional Summary:", fontWeight = FontWeight.Bold)

                val summary = calculateNutritionSummary(mealWithFoods.foods)
                NutritionSummaryView(summary)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

private fun calculateNutritionSummary(foods: List<FoodWithPortion>): NutritionSummary {
    var totalCalories = 0f
    var totalProteins = 0f
    var totalFats = 0f
    var totalCarbs = 0f

    foods.forEach { (food, portion) ->
        val factor = portion / 100f
        totalCalories += food.energyKcal * factor
        totalProteins += food.proteins * factor
        totalFats += food.fats * factor
        totalCarbs += food.carbohydrates * factor
    }

    return NutritionSummary(
        totalCalories = totalCalories,
        totalProteins = totalProteins,
        totalFats = totalFats,
        totalCarbohydrates = totalCarbs
    )
}

@Composable
fun NutritionSummaryView(summary: NutritionSummary) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Calories:")
            Text("${summary.totalCalories.toInt()} kcal", fontWeight = FontWeight.Bold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Proteins:")
            Text("${summary.totalProteins.toInt()}g", fontWeight = FontWeight.Bold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Glides:")
            Text("${summary.totalCarbohydrates.toInt()}g", fontWeight = FontWeight.Bold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Lipids:")
            Text("${summary.totalFats.toInt()}g", fontWeight = FontWeight.Bold)
        }
    }
}



