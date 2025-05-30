package com.example.diabeteapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SignPageScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sign In Button
        Button(
            onClick = { navController.navigate(SignInPage) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sign Up Button
        Button(
            onClick = { navController.navigate(SignUpPage) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", color = Color.White)
        }
    }
}