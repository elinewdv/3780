package com.example.diabeteapp
import FoodItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MealDetailsDialog(
    mealWithFoods: MealWithFoods,
    onDismiss: () -> Unit
) {
    // Calcul des nutriments totaux
    val (totalCalories, totalProteins, totalFats, totalCarbs) = remember(mealWithFoods) {
        var calories = 0f
        var proteins = 0f
        var fats = 0f
        var carbs = 0f

        mealWithFoods.foods.forEach { (food, portion) ->
            val factor = portion / 100f
            calories += food.energyKcal * factor
            proteins += food.proteins * factor
            fats += food.fats * factor
            carbs += food.carbohydrates * factor
        }

        Quadruple(calories, proteins, fats, carbs)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(mealWithFoods.meal.name) },
        text = {
            Column {
                // Liste des aliments
                Text("Aliments consommés:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                mealWithFoods.foods.forEach { foodWithPortion ->
                    val food = foodWithPortion.foodItem
                    val portion = foodWithPortion.customPortionG

                }

                // Résumé nutritionnel
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total nutritionnel:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    NutritionInfo("Calories", "${totalCalories.toInt()} kcal")
                    NutritionInfo("Protéines", "${totalProteins.toInt()}g")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    NutritionInfo("Glucides", "${totalCarbs.toInt()}g")
                    NutritionInfo("Lipides", "${totalFats.toInt()}g")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun NutritionInfo(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

private data class Quadruple(
    val first: Float,
    val second: Float,
    val third: Float,
    val fourth: Float
)