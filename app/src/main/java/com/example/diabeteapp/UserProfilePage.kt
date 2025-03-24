package com.example.diabeteapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
fun UserProfilePageScreen(navController: NavHostController) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedDiabetesType by remember { mutableStateOf("") }

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

        Spacer(modifier = Modifier.height(10.dp))

        // Disclaimer Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD99900)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Warning, contentDescription = "Warning", tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("DISCLAIMER", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Diabetes Type Selection
        Text("Diabetes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2264FF))

        Row {
            DiabetesCheckbox("Type 1", selectedDiabetesType) { selectedDiabetesType = it }
            DiabetesCheckbox("Type 2", selectedDiabetesType) { selectedDiabetesType = it }
            DiabetesCheckbox("Type 3", selectedDiabetesType) { selectedDiabetesType = it }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Height Input
        InputField(value = height, onValueChange = { height = it }, label = "Height", unit = "cm")

        Spacer(modifier = Modifier.height(10.dp))

        // Weight Input
        InputField(value = weight, onValueChange = { weight = it }, label = "Weight", unit = "kg")

        Spacer(modifier = Modifier.height(10.dp))

        // Gender Selection
        Text("Gender", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2264FF))

        Row {
            GenderCheckbox("M", selectedGender) { selectedGender = it }
            GenderCheckbox("F", selectedGender) { selectedGender = it }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm Button
        Button(
            onClick = { navController.navigate(TargetPage()) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2264FF))
        ) {
            Text(text = "Confirm", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// Composable pour les champs de texte
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String, unit: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontWeight = FontWeight.Bold, color = Color.White) },
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        Spacer(modifier = Modifier.width(8.dp))
        Text(unit, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}


@Composable
fun DiabetesCheckbox(text: String, selected: String, onSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = selected == text,
            onCheckedChange = { onSelected(if (it) text else "") },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2264FF))
        )
        Text(text, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.width(10.dp))
    }
}


@Composable
fun GenderCheckbox(text: String, selected: String, onSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = selected == text,
            onCheckedChange = { onSelected(if (it) text else "") },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2264FF))
        )
        Text(text, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.width(10.dp))
    }
}
