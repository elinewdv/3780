package com.example.diabeteapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape


data class Ingredient(val name: String)
data class Category(
    val name: String,
    val ingredients: List<Ingredient>,
    var isExpanded: Boolean = false
)

@Composable
fun FoodPageScreen() {
    val color = Color(0xFF2264FF)
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        mutableStateListOf(
            Category("Vegetables", listOf(
                Ingredient("Potato"), Ingredient("Carrot"), Ingredient("Broccoli"), Ingredient("Tomato")
            )),
            Category("Drinks", listOf(
                Ingredient("Water"), Ingredient("Juice"), Ingredient("Tea")
            )),
            Category("Fruits", listOf(
                Ingredient("Apple"), Ingredient("Banana"), Ingredient("Grapes")
            ))
        )
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

        // search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search ingredients", color = Color.White.copy(alpha = 0.7f)) },
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            categories.forEachIndexed { index, category ->
                item {
                    CategoryBlock(
                        category = category,
                        color = color,
                        searchQuery = searchQuery,
                        onToggleExpand = {
                            categories[index] = category.copy(isExpanded = !category.isExpanded)
                        },
                        onAdd = {
                            // handle added ingredients here...
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBlock(
    category: Category,
    color: Color,
    searchQuery: String,
    onToggleExpand: () -> Unit,
    onAdd: (Ingredient) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(category.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Icon(
                imageVector = if (category.isExpanded)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = color
            )
        }

        if (category.isExpanded) {
            category.ingredients
                .filter { it.name.contains(searchQuery, ignoreCase = true) }
                .forEach { ingredient ->
                    IngredientRow(ingredient, color, onAdd)
                }
        }
    }
}

@Composable
fun IngredientRow(ingredient: Ingredient, color: Color, onAdd: (Ingredient) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = ingredient.name, fontSize = 16.sp, color = Color.Black)
        IconButton(onClick = { onAdd(ingredient) }) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = color)
        }
    }
}
