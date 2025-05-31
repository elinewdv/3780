package com.example.diabeteapp

import Meal
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.format.DateTimeFormatter



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealHistoryScreen(
    viewModel: FoodViewModel,
    userId: String,
    onNavigateBack: () -> Unit = {}
) {
    val meals by viewModel.userMeals.collectAsStateWithLifecycle()
    val selectedMeal by viewModel.selectedMeal.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        SmallTopAppBar(
            title = { Text("Previous Meals") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (meals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No meal registered yet, Get started !")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(meals) { meal ->
                    MealHistoryItem(
                        meal = meal,
                        onClick = { viewModel.loadMealDetails(meal.mealId, userId) }
                    )
                }
            }
        }
    }


    selectedMeal?.let { mealWithFoods ->
        MealDetailsDialog(
            mealWithFoods = mealWithFoods,
            onDismiss = { viewModel.clearSelectedMeal() }
        )
    }
}

@Composable
fun MealHistoryItem(
    meal: Meal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2264FF)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = meal.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}