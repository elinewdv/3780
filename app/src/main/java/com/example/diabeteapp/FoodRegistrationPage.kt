package com.example.diabeteapp

import FoodItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRegistrationScreen(
    viewModel: FoodViewModel,
    userId: String,
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
    var showDeleteConfirmation by remember { mutableStateOf<FoodItem?>(null) }
    var showCustomFoodDialog by remember { mutableStateOf(false) }

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
            text = "Create a new meal",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nom du repas
        OutlinedTextField(
            value = mealName,
            onValueChange = { viewModel.setMealName(it) },
            label = { Text("Give your Meal a name !") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Barre de recherche et bouton personnalisé
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchFoods(it)
                },
                label = { Text("Search for a food") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { showCustomFoodDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = color.copy(alpha = 0.2f),
                    contentColor = color
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = "Custom")
            }
        }

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
                text = "Queries Results",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(vertical = 8.dp)
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
                text = "Food in the meal",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(vertical = 8.dp)
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
                        onRemove = { showDeleteConfirmation = foodItem },
                        color = color
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add food to your meal !",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
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
                text = if (isSaving) "Registering.." else "Register your meal !",
                color = Color.White
            )
        }
    }

    // Dialog de confirmation de suppression
    showDeleteConfirmation?.let { foodToDelete ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Food ?") },
            text = { Text("Are you sure you want to delete ${foodToDelete.name} from meal ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeFoodFromMeal(foodToDelete)
                        showDeleteConfirmation = null
                    }
                ) {
                    Text("Confirm", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel", color = color)
                }
            }
        )
    }

    // Dialog pour aliments personnalisés
    if (showCustomFoodDialog) {
        CustomFoodDialog(
            onDismiss = { showCustomFoodDialog = false },
            onConfirm = { customFood ->
                viewModel.addFoodToMeal(customFood)
                showCustomFoodDialog = false
            },
            color = color
        )
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
private fun SearchResultItem(
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
                    contentDescription = "Add",
                    tint = color
                )
            }
        }
    }
}

@Composable
private fun SelectedFoodItem(
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
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            // Contrôle de portion avec Slider
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text("Portion (g): ${portion.toInt()}", fontSize = 14.sp)
                Slider(
                    value = portion,
                    onValueChange = { onPortionChange(it) },
                    valueRange = 10f..500f,
                    steps = 49,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = color,
                        activeTrackColor = color
                    )
                )
            }
        }
    }
}

@Composable
private fun NutritionPreview(
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
                text = "Nutritional summary",
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
private fun NutrientItem(name: String, value: String, unit: String) {
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
private fun NutritionSummaryDialog(
    nutritionSummary: NutritionSummary,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Meal successfully registered !",
                textAlign = TextAlign.Center,
                color = Color(0xFF2264FF)
            )
        },
        text = {
            Column {
                Text(
                    text = "Summary of the Meal :",
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

@Composable
private fun CustomFoodDialog(
    onDismiss: () -> Unit,
    onConfirm: (FoodItem) -> Unit,
    color: Color
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var fiber by remember { mutableStateOf("") }
    var portion by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a personnalized food", color = color) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name of the food*") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories (kcal)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = portion,
                        onValueChange = { portion = it },
                        label = { Text("Portion (g)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    OutlinedTextField(
                        value = proteins,
                        onValueChange = { proteins = it },
                        label = { Text("Proteins (g)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Glides (g)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it },
                        label = { Text("Lipids (g)*") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = fiber,
                        onValueChange = { fiber = it },
                        label = { Text("Fibres (g)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val portionValue = portion.toFloatOrNull() ?: 100f
                    val foodItem = FoodItem(
                        foodId = "custom_${System.currentTimeMillis()}",
                        name = name,
                        latinName = "", // Champ vide pour les aliments personnalisés
                        foodGroupId = "custom", // Groupe spécial pour les aliments personnalisés
                        defaultPortionG = portionValue,
                        selectedPortionG = portionValue,
                        energyKcal = calories.toFloatOrNull() ?: 0f,
                        energyKj = (calories.toFloatOrNull() ?: 0f) * 4.184f, // Conversion kcal -> kJ
                        proteins = proteins.toFloatOrNull() ?: 0f,
                        fats = fats.toFloatOrNull() ?: 0f,
                        carbohydrates = carbs.toFloatOrNull() ?: 0f,
                        fiber = fiber.toFloatOrNull(),
                        ediblePartPercent = 100, // On suppose 100% comestible
                        uri = null // Pas d'URI pour les aliments personnalisés
                    )
                    onConfirm(foodItem)
                },
                enabled = name.isNotBlank() &&
                        calories.isNotBlank() &&
                        portion.isNotBlank() &&
                        proteins.isNotBlank() &&
                        carbs.isNotBlank() &&
                        fats.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = color)
            }
        }
    )
}