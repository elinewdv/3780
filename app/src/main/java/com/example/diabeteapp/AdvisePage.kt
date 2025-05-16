package com.example.diabeteapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdvisePageScreen() {
    val tips = listOf(
        "Monitor your blood sugar levels regularly.",
        "Eat balanced meals with low sugar and carbs.",
        "Exercise at least 30 minutes daily.",
        "Stay hydrated and avoid sugary drinks.",
        "Take medications as prescribed.",
        "Get regular check-ups with your doctor.",
        "Manage stress through relaxation techniques."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Diabetes Tips",
            color = Color(0xFF2264FF),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        LazyColumn {
            items(tips) { tip ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF1FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = tip,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
