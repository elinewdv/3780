package com.example.diabeteapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.diabeteapp.data.database.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInPageScreen(
    navController: NavHostController,
    onAuthSuccess: (Boolean) -> Unit,
    sessionManager: SessionManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val color = Color(0xFF2264FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo et autres éléments UI...

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Email Input
        InputField(
            value = email,
            onValueChange = { email = it },
            label = "E-mail",

            )

        Spacer(modifier = Modifier.height(10.dp))

        // Password Input
        InputField(
            value = password,
            onValueChange = { password = it },
            label = "Password",

        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please fill all fields"
                    return@Button
                }

                isLoading = true
                scope.launch {
                    try {
                        val db = AppDatabase.getDatabase(context)
                        val user = db.userDao().getUserByEmail(email)

                        if (user == null) {
                            errorMessage = "User not found. Please sign up."
                        } else if (!PasswordHasher.verifyPassword(password, user.password)) {
                            errorMessage = "Invalid password"
                        } else {
                            onAuthSuccess(true)
                            sessionManager.createSession(user.userId)
                            navController.navigate(TargetPage) {
                                popUpTo(SignPage) { inclusive = true }
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = "Login failed: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Sign In", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = { navController.navigate(SignUpPage) }) {
            Text("Don't have an account? Sign Up", color = color)
        }
    }
}