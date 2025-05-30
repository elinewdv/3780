package com.example.diabeteapp

import FoodViewModelFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diabeteapp.data.FoodRepository
import com.example.diabeteapp.data.api.RetrofitInstance
import com.example.diabeteapp.data.database.DatabaseProvider
import com.example.diabeteapp.ui.theme.DiabeteAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable object SignPage
@Serializable object SignInPage
@Serializable object SignUpPage
@Serializable object HomePage
@Serializable object TargetPage
@Serializable object FoodRegistrationPage
@Serializable object AdvisePage
@Serializable object UserProfilePage

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialisation des dépendances
        val foodRepository = FoodRepository(
            RetrofitInstance.apiService,
            DatabaseProvider.getDatabase(this).foodItemDao()
        )

        viewModel = ViewModelProvider(
            this,
            FoodViewModelFactory(application)
        )[FoodViewModel::class.java]

        // Lancement du test API après un court délai
        lifecycleScope.launch {
            delay(2000) // Délai pour laisser l'UI s'initialiser
            viewModel.manualApiTest()
        }

        setContent {
            DiabeteAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Observer les résultats du test API
                val apiTestResult by viewModel.apiTestResult.collectAsStateWithLifecycle()
                apiTestResult?.let { result ->
                    LaunchedEffect(result) {
                        Toast.makeText(
                            this@MainActivity,
                            result,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                // Fonction pour gérer les changements d'authentification
                val onAuthChange: (Boolean) -> Unit = { isAuthenticated ->
                    if (isAuthenticated) {
                        navController.navigate(HomePage) {
                            popUpTo(SignPage) { inclusive = true }
                        }
                    } else {
                        navController.navigate(SignPage) {
                            popUpTo(HomePage) { inclusive = true }
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        if (!isPrincipalPage(currentDestination)) {
                            TopBar(navController)
                        }
                    },
                    bottomBar = {
                        if (!isPrincipalPage(currentDestination)) {
                            BottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = HomePage,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<SignPage> {
                            SignPageScreen(navController)
                        }
                        composable<SignInPage> {
                            SignInPageScreen(navController, onAuthChange)
                        }
                        composable<SignUpPage> {
                            SignUpPageScreen(navController)
                        }
                        composable<HomePage> {
                            HomePageScreen(navController)
                        }
                        composable<TargetPage> {
                            TargetPageScreen()
                        }
                        composable<FoodRegistrationPage> {
                            FoodRegistrationScreen(
                                viewModel = viewModel,
                                userId = 1L,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable<AdvisePage> {
                            AdvisePageScreen()
                        }
                        composable<UserProfilePage> {
                            UserProfilePageScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFF2264FF)
        ),
        title = {
            Text(
                "Jumper",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(HomePage) }) {
                ImageType(R.drawable.logo_blue)
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(UserProfilePage) }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.height(120.dp),
        containerColor = Color(0xFF2264FF),
        contentColor = Color.White,
    ) {
        NavigationBarItem(
            icon = { ImageType(R.drawable.target) },
            selected = false,
            onClick = { navController.navigate(TargetPage) }
        )
        NavigationBarItem(
            icon = { ImageType(R.drawable.apple) },
            selected = false,
            onClick = { navController.navigate(FoodRegistrationPage) }
        )
        NavigationBarItem(
            icon = { ImageType(R.drawable.lumous) },
            selected = false,
            onClick = { navController.navigate(AdvisePage) }
        )
    }
}

@Composable
fun ImageType(logoName: Int) {
    Image(
        painter = painterResource(logoName),
        contentDescription = "Image",
        modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
            .aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun isPrincipalPage(currentDestination: NavDestination?): Boolean {
    return currentDestination?.hasRoute<HomePage>() == true ||
            currentDestination?.hasRoute<SignPage>() == true ||
            currentDestination?.hasRoute<UserProfilePage>() == true ||
            currentDestination?.hasRoute<SignInPage>() == true ||
            currentDestination?.hasRoute<SignUpPage>() == true
}