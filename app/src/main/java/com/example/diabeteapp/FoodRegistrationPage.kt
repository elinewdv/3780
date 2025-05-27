package com.example.diabeteapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

// Add new serializable classes for navigation
@Serializable
data class MealSelectionPage(val dummy: String = "")

@Serializable
data class FoodRegistrationWithMealPage(val mealType: String)

// JSON data classes to match your JSON structure
@Serializable
data class FoodGroupJson(
    val foodGroupId: String,
    val name: String,
    val parentId: String? = null
)

@Serializable
data class FoodGroupsData(
    val foodGroups: List<FoodGroupJson>,
    val locale: String
)

// Enum for meal types
enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack");

    companion object {
        fun fromString(value: String): MealType {
            return values().find { it.name == value } ?: BREAKFAST
        }
    }
}

data class FoodGroup(
    val foodGroupId: String,
    val name: String,
    val parentId: String? = null,
    val isExpanded: Boolean = false
)

data class Ingredient(val name: String)
data class Category(
    val name: String,
    val ingredients: List<Ingredient>,
    var isExpanded: Boolean = false
)

// This replaces your existing FoodPageScreen() to work with navigation
@Composable
fun MealSelectionScreen(navController: androidx.navigation.NavController) {
    val color = Color(0xFF2264FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Meal Type",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Meal type buttons
        MealType.values().forEach { mealType ->
            MealTypeButton(
                mealType = mealType,
                color = color,
                onClick = {
                    navController.navigate(FoodRegistrationWithMealPage(mealType.name))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MealTypeButton(
    mealType: MealType,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mealType.displayName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

// Keep your original FoodPageScreen() intact for existing navigation
@Composable
fun FoodPageScreen() {
    val context = LocalContext.current
    val color = Color(0xFF2264FF)
    var searchQuery by remember { mutableStateOf("") }
    var foodGroups by remember { mutableStateOf<List<FoodGroup>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load food groups from JSON file
    LaunchedEffect(Unit) {
        try {
            val jsonString = loadJsonFromRaw(context, R.raw.food)
            val foodGroupsData = Json.decodeFromString<FoodGroupsData>(jsonString)
            foodGroups = foodGroupsData.foodGroups.map { jsonGroup ->
                FoodGroup(
                    foodGroupId = jsonGroup.foodGroupId,
                    name = jsonGroup.name,
                    parentId = jsonGroup.parentId,
                    isExpanded = false
                )
            }
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error loading food data: ${e.message}"
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Food Registration",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search food items", color = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = color,
                focusedContainerColor = color,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = color)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val mainGroups = foodGroups.filter { it.parentId == null }
                    // Sort groups: categories with subcategories first, then leaf categories
                    val sortedMainGroups = mainGroups.sortedWith { group1, group2 ->
                        val hasSubGroups1 = foodGroups.any { it.parentId == group1.foodGroupId }
                        val hasSubGroups2 = foodGroups.any { it.parentId == group2.foodGroupId }

                        when {
                            hasSubGroups1 && !hasSubGroups2 -> -1 // group1 has subs, group2 doesn't -> group1 first
                            !hasSubGroups1 && hasSubGroups2 -> 1  // group1 doesn't have subs, group2 does -> group2 first
                            else -> group1.name.compareTo(group2.name) // both same type -> alphabetical
                        }
                    }

                    items(sortedMainGroups) { mainGroup ->
                        FoodGroupBlock(
                            foodGroup = mainGroup,
                            allGroups = foodGroups,
                            color = color,
                            searchQuery = searchQuery,
                            onToggleExpand = { groupId ->
                                foodGroups = foodGroups.map { group ->
                                    if (group.foodGroupId == groupId) {
                                        group.copy(isExpanded = !group.isExpanded)
                                    } else group
                                }
                            },
                            onAdd = { groupName ->
                                // Handle added food items here...
                                println("Added item from group: $groupName")
                            }
                        )
                    }
                }
            }
        }
    }
}

// New FoodPageScreen with meal type parameter for the new flow
@Composable
fun FoodPageScreenWithMeal(
    mealType: MealType,
    navController: androidx.navigation.NavController
) {
    val context = LocalContext.current
    val color = Color(0xFF2264FF)
    var searchQuery by remember { mutableStateOf("") }
    var foodGroups by remember { mutableStateOf<List<FoodGroup>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load food groups from JSON file
    LaunchedEffect(Unit) {
        try {
            val jsonString = loadJsonFromRaw(context, R.raw.food)
            val foodGroupsData = Json.decodeFromString<FoodGroupsData>(jsonString)
            foodGroups = foodGroupsData.foodGroups.map { jsonGroup ->
                FoodGroup(
                    foodGroupId = jsonGroup.foodGroupId,
                    name = jsonGroup.name,
                    parentId = jsonGroup.parentId,
                    isExpanded = false
                )
            }
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error loading food data: ${e.message}"
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = color
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Food Registration",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = mealType.displayName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = color.copy(alpha = 0.7f)
                )
            }

            // Empty space to balance the layout
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search food items", color = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = color,
                focusedContainerColor = color,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = color)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val mainGroups = foodGroups.filter { it.parentId == null }
                    // Sort groups: categories with subcategories first, then leaf categories
                    val sortedMainGroups = mainGroups.sortedWith { group1, group2 ->
                        val hasSubGroups1 = foodGroups.any { it.parentId == group1.foodGroupId }
                        val hasSubGroups2 = foodGroups.any { it.parentId == group2.foodGroupId }

                        when {
                            hasSubGroups1 && !hasSubGroups2 -> -1 // group1 has subs, group2 doesn't -> group1 first
                            !hasSubGroups1 && hasSubGroups2 -> 1  // group1 doesn't have subs, group2 does -> group2 first
                            else -> group1.name.compareTo(group2.name) // both same type -> alphabetical
                        }
                    }

                    items(sortedMainGroups) { mainGroup ->
                        FoodGroupBlock(
                            foodGroup = mainGroup,
                            allGroups = foodGroups,
                            color = color,
                            searchQuery = searchQuery,
                            onToggleExpand = { groupId ->
                                foodGroups = foodGroups.map { group ->
                                    if (group.foodGroupId == groupId) {
                                        group.copy(isExpanded = !group.isExpanded)
                                    } else group
                                }
                            },
                            onAdd = { groupName ->
                                // Handle added food items here...
                                println("Added item from $groupName to ${mealType.displayName}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// Function to load JSON from raw resources
fun loadJsonFromRaw(context: android.content.Context, resourceId: Int): String {
    return try {
        context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        throw IOException("Could not read JSON from raw resources", ioException)
    }
}

@Composable
fun FoodGroupBlock(
    foodGroup: FoodGroup,
    allGroups: List<FoodGroup>,
    color: Color,
    searchQuery: String,
    onToggleExpand: (String) -> Unit,
    onAdd: (String) -> Unit
) {
    val hasSubGroups = allGroups.any { it.parentId == foodGroup.foodGroupId }
    val subGroups = allGroups.filter { it.parentId == foodGroup.foodGroupId }

    // Filter groups based on search query
    val shouldShow = searchQuery.isEmpty() ||
            foodGroup.name.contains(searchQuery, ignoreCase = true) ||
            subGroups.any { it.name.contains(searchQuery, ignoreCase = true) }

    if (!shouldShow) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (hasSubGroups) {
                        onToggleExpand(foodGroup.foodGroupId)
                    } else {
                        onAdd(foodGroup.name)
                    }
                }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                foodGroup.name,
                fontSize = 16.sp,
                fontWeight = if (hasSubGroups) FontWeight.Bold else FontWeight.Normal,
                color = color
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!hasSubGroups) {
                    // Add button for leaf categories (no subcategories)
                    IconButton(onClick = { onAdd(foodGroup.name) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = color)
                    }
                } else {
                    // Expand/collapse arrow for categories with subcategories
                    IconButton(onClick = { onToggleExpand(foodGroup.foodGroupId) }) {
                        Icon(
                            imageVector = if (foodGroup.isExpanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = if (foodGroup.isExpanded) "Collapse" else "Expand",
                            tint = color
                        )
                    }
                }
            }
        }

        // Show subcategories if expanded
        if (foodGroup.isExpanded && hasSubGroups) {
            subGroups.forEach { subGroup ->
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    FoodGroupBlock(
                        foodGroup = subGroup,
                        allGroups = allGroups,
                        color = color.copy(alpha = 0.8f),
                        searchQuery = searchQuery,
                        onToggleExpand = onToggleExpand,
                        onAdd = onAdd
                    )
                }
            }
        }
    }
}