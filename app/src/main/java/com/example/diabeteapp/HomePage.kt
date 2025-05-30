package com.example.diabeteapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomePageScreen(navController: NavHostController, sessionManager: SessionManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()  // Remplit tout l'écran
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally  // Centre horizontalement les éléments
    ) {
        ImageType(R.drawable.logo)
        Spacer(modifier = Modifier.height(5.dp))
        Text("Welcome", color = Color(0xFF2264FF), style = MaterialTheme.typography.headlineLarge,)
        Spacer(modifier = Modifier.height(5.dp))
        Text("Quote",color = Color(0xFF2264FF))
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = { navController.navigate(SignPage) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF))
        ) {
            Text(text = "Next", color = Color.White)

        }
    }
}


