package com.example.diabeteapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.Text



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInPageScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo), // Assure-toi que ce fichier existe dans res/drawable
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = "Sign In",
            fontSize = 26.sp,
            color = Color(0xFF2264FF),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(120.dp))

        // Email
        Text("E-mail",color=Color(0xFF2264FF))
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail", fontWeight = FontWeight.Bold, color = Color(0xFF2264FF)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2264FF))
                .height(56.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(0xFF2264FF),
                focusedContainerColor = Color(0xFF2264FF),
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password
        Text("PassWord",color=Color(0xFF2264FF))
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", fontWeight = FontWeight.Bold, color = Color(0xFF2264FF)) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2264FF))
                .height(56.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Done else Icons.Default.Clear,
                        contentDescription = "Toggle Password Visibility",
                        tint = Color.White
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(0xFF2264FF),
                focusedContainerColor = Color(0xFF2264FF),
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Bouton Confirm
        Button(
            onClick = { navController.navigate(HomePage()) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF))
        ) {
            Text(text = "Confirm", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
