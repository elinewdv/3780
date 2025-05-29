package com.example.diabeteapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.RemoveCircleOutline
import FoodItem
@Composable
fun FoodRegistrationScreen(
    viewModel: FoodViewModel,
    userId: Long,
    onNavigateBack: () -> Unit = {}
) {
    val color = Color(0xFF2264FF)

    // États du ViewModel
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val selectedFoods by viewModel.selectedFoods.collectAsStateWithLifecycle()
    val mealName by viewModel.mealName.collectAsStateWithLifecycle()
    val nutritionSummary by viewModel.nutritionSummary.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val saveSuccess by viewModel.saveSuccess.collectAsStateWithLifecycle()

    // États locaux
    var searchQuery by remember { mutableStateOf("") }
    var showNutritionSummary by remember { mutableStateOf(false) }

    // Gérer le succès de sauvegarde
    LaunchedEffect(saveSuccess) {
        if (saveSuccess == true) {
            showNutritionSummary = true
            viewModel.clearSaveStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête
        Text(
            text = "Créer un repas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nom du repas
        OutlinedTextField(
            value = mealName,
            onValueChange = { viewModel.setMealName(it) },
            label = { Text("Nom du repas (optionnel)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Barre de recherche
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchFoods(it)
            },
            label = { Text("Rechercher un aliment") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Indicateur de chargement de recherche
        if (isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = color)
            }
        }

        // Résultats de recherche
        if (searchResults.isNotEmpty() && searchQuery.isNotBlank()) {
            Text(
                text = "Résultats de recherche",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(searchResults) { foodItem ->
                    SearchResultItem(
                        foodItem = foodItem,
                        onAddToMeal = { viewModel.addFoodToMeal(it) },
                        color = color
                    )
                }
            }
        }

        // Aliments sélectionnés
        if (selectedFoods.isNotEmpty()) {
            Text(
                text = "Aliments dans le repas",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedFoods.toList()) { (foodItem, portion) ->
                    SelectedFoodItem(
                        foodItem = foodItem,
                        portion = portion,
                        onPortionChange = { newPortion ->
                            viewModel.updateFoodPortion(foodItem, newPortion)
                        },
                        onRemove = { viewModel.removeFoodFromMeal(foodItem) },
                        color = color
                    )
                }
            }
        }

        // Résumé nutritionnel en temps réel
        if (selectedFoods.isNotEmpty()) {
            NutritionPreview(
                nutritionSummary = nutritionSummary,
                color = color
            )
        }

        // Bouton de sauvegarde
        Button(
            onClick = { viewModel.saveMeal(userId) },
            enabled = selectedFoods.isNotEmpty() && !isSaving,
            colors = ButtonDefaults.buttonColors(containerColor = color),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (isSaving) "Sauvegarde..." else "Enregistrer le repas",
                color = Color.White
            )
        }
    }

    // Dialog de succès avec résumé nutritionnel
    if (showNutritionSummary) {
        NutritionSummaryDialog(
            nutritionSummary = nutritionSummary,
            onDismiss = {
                showNutritionSummary = false
                onNavigateBack()
            }
        )
    }
}

@Composable
fun SearchResultItem(
    foodItem: FoodItem,
    onAddToMeal: (FoodItem) -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = foodItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${foodItem.energyKcal.toInt()} kcal/100g",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { onAddToMeal(foodItem) }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Ajouter",
                    tint = color
                )
            }
        }
    }
}

@Composable
fun SelectedFoodItem(
    foodItem: FoodItem,
    portion: Float,
    onPortionChange: (Float) -> Unit,
    onRemove: () -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodItem.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(foodItem.energyKcal * portion / 100).toInt()} kcal",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = Color.Red
                    )
                }
            }

            // Contrôle de portion
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Portion (g):", fontSize = 14.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val newPortion = (portion - 10).coerceAtLeast(10f)
                            onPortionChange(newPortion)
                        }
                    ) {
                        Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Diminuer")
                    }

                    Text(
                        text = portion.toInt().toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    IconButton(
                        onClick = {
                            val newPortion = portion + 10
                            onPortionChange(newPortion)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Augmenter")
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionPreview(
    nutritionSummary: NutritionSummary,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Résumé nutritionnel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientItem("Calories", "${nutritionSummary.totalCalories.toInt()}", "kcal")
                NutrientItem("Protéines", "${nutritionSummary.totalProteins.toInt()}", "g")
                NutrientItem("Glucides", "${nutritionSummary.totalCarbohydrates.toInt()}", "g")
                NutrientItem("Lipides", "${nutritionSummary.totalFats.toInt()}", "g")
            }
        }
    }
}

@Composable
fun NutrientItem(name: String, value: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$value$unit",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun NutritionSummaryDialog(
    nutritionSummary: NutritionSummary,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Repas enregistré avec succès !",
                textAlign = TextAlign.Center,
                color = Color(0xFF2264FF)
            )
        },
        text = {
            Column {
                Text(
                    text = "Résumé nutritionnel du repas :",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text("• Calories : ${nutritionSummary.totalCalories.toInt()} kcal")
                Text("• Protéines : ${nutritionSummary.totalProteins.toInt()} g")
                Text("• Glucides : ${nutritionSummary.totalCarbohydrates.toInt()} g")
                Text("• Lipides : ${nutritionSummary.totalFats.toInt()} g")
                Text("• Fibres : ${nutritionSummary.totalFiber.toInt()} g")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = Color(0xFF2264FF))
            }
        }
    )
}