package com.example.diabeteapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPageScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var color = Color(0xFF2264FF)

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
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
//
        // Name Input
        Text("Name",color=color)
        Spacer(modifier = Modifier.height(5.dp))
        InputField(value = name, onValueChange = { name = it }, label = "Name")

        Spacer(modifier = Modifier.height(10.dp))

        // Age Group Input
        Text("Age",color=color)
        Spacer(modifier = Modifier.height(5.dp))
        InputField(value = ageGroup, onValueChange = { ageGroup = it }, label = "Age group", placeholder = "../../..")

        Spacer(modifier = Modifier.height(10.dp))

        // Email Input
        Text("E-mail",color=color)
        Spacer(modifier = Modifier.height(5.dp))
        InputField(value = email, onValueChange = { email = it }, label = "E-mail", keyboardType = KeyboardType.Email)

        Spacer(modifier = Modifier.height(10.dp))

        // Password Input
        Text("Password",color=color)
        Spacer(modifier = Modifier.height(5.dp))
        InputField(value = password, onValueChange = { password = it }, label = "Password", keyboardType = KeyboardType.Password)

        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Password Input
        Text("Confirm Password",color=color)
        Spacer(modifier = Modifier.height(5.dp))
        InputField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm password", keyboardType = KeyboardType.Password)

        Spacer(modifier = Modifier.height(20.dp))

        // Next Button
        Button(
            onClick = { navController.navigate(TargetPage()) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF))
        ) {
            Text(text = "Confirm", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontWeight = FontWeight.Bold, color = Color.White) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder, color = Color.White.copy(alpha = 0.6f)) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(56.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
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
}
