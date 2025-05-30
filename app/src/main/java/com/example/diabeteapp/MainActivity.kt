package com.example.diabeteapp

import FoodViewModelFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.material.icons.filled.ArrowBack
@Serializable object MealHistoryPage
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
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialisation des dépendances
        sessionManager = SessionManager(this)
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
            delay(2000)
            viewModel.manualApiTest()
        }

        setContent {
            DiabeteAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }

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

                // Gestion de la navigation initiale
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        val userId = sessionManager.getCurrentUserId()
                        val db = DatabaseProvider.getDatabase(this@MainActivity)
                        val user = userId?.let { db.userDao().getUserById(it) }

                        if (user?.isProfileComplete == true) {
                            navController.navigate(HomePage) {
                                popUpTo(0)
                            }
                        } else {
                            navController.navigate(UserProfilePage) {
                                popUpTo(0)
                            }
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        if (!isPrincipalPage(currentDestination)) {
                            TopBar(navController, sessionManager)
                        }
                    },
                    bottomBar = {
                        if (!isPrincipalPage(currentDestination) && isLoggedIn) {
                            BottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) HomePage else SignPage,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<SignPage> {
                            SignPageScreen(navController)
                        }
                        composable<SignInPage> {
                            SignInPageScreen(
                                navController = navController,
                                onAuthSuccess = { loggedIn ->
                                    if (loggedIn) {
                                        navController.navigate(TargetPage) {
                                            popUpTo(SignPage) { inclusive = true }
                                        }
                                    }
                                },
                                sessionManager = sessionManager
                            )
                        }
                        composable<SignUpPage> {
                            SignUpPageScreen(navController)
                        }
                        composable<HomePage> {
                            if (isLoggedIn) {
                                HomePageScreen(navController, sessionManager)
                            } else {
                                navController.navigate(SignPage) {
                                    popUpTo(0)
                                }
                            }
                        }
                        composable<TargetPage> {
                            TargetPageScreen()
                        }
                        composable<MealHistoryPage> {
                            if (isLoggedIn) {
                                MealHistoryScreen(
                                    viewModel = viewModel,
                                    userId = sessionManager.getCurrentUserId() ?: "",
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            } else {
                                navController.navigate(SignPage) {
                                    popUpTo(0)
                                }
                            }
                        }
                        composable<FoodRegistrationPage> {
                            if (isLoggedIn) {
                                FoodRegistrationScreen(
                                    viewModel = viewModel,
                                    userId = sessionManager.getCurrentUserId() ?: "",
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            } else {
                                navController.navigate(SignPage) {
                                    popUpTo(0)
                                }
                            }
                        }
                        composable<AdvisePage> {
                            AdvisePageScreen()
                        }
                        composable<UserProfilePage> {
                            if (isLoggedIn) {
                                UserProfilePageScreen(navController, sessionManager)
                            } else {
                                navController.navigate(SignPage) {
                                    popUpTo(0)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, sessionManager: SessionManager) {
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
            IconButton(onClick = {
                if (sessionManager.isLoggedIn()) {
                    navController.navigate(UserProfilePage)
                } else {
                    navController.navigate(SignInPage)
                }
            }) {
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

// Les autres composables (BottomBar, ImageType, isPrincipalPage) restent inchangés

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
            icon = { ImageType(R.drawable.logo_blue) }, // Ajouter une icône d'historique dans vos ressources
            selected = false,
            onClick = { navController.navigate(MealHistoryPage) }
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
            currentDestination?.hasRoute<SignInPage>() == true ||
            currentDestination?.hasRoute<SignUpPage>() == true
}