package com.example.diabeteapp

import android.os.Bundle
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diabeteapp.ui.theme.DiabeteAppTheme
import kotlinx.serialization.Serializable

@Serializable
class SignPage

@Serializable
class SignInPage

@Serializable
class HomePage

@Serializable
class TargetPage

@Serializable
class FoodRegistrationPage

@Serializable
class AdvisePage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiabeteAppTheme {
                val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
                val classeLargeur = windowSizeClass.windowWidthSizeClass
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    topBar = {
                        if (currentDestination?.hasRoute<HomePage>() != true) {
                    TopBar(navController)}
                }, bottomBar = {
                        if (currentDestination?.hasRoute<HomePage>() != true) {BottomBar(navController)}
                }
                ) { innerPadding ->

                    NavHost(
                        navController = navController, startDestination = HomePage(),
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<SignPage> {
                            SignPageScreen(navController)
                        }
                        composable<HomePage> {
                            HomePageScreen(navController)
                        }
                        composable<TargetPage> {
                            TargetPageScreen()
                        }
                        composable<FoodRegistrationPage> {
                            FoodPageScreen()
                        }
                        composable<AdvisePage> {
                            AdvisePageScreen()
                        }
                        composable<SignInPage> {
                            SignInPageScreen(navController)
                        }

                    }
                }
        }
    }
}
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar( navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFF2264FF)
        ),
        title = {
                Text(
                    "Fatty's",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color.White
                )

            },
        navigationIcon = {
            IconButton(onClick = {
                TODO()
            }) {
                ImageType(R.drawable.logo_blue)
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(SignPage())
            }) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Home page",
                    modifier = Modifier
                        .size(28.dp),
                    tint = Color.White
                )
            }
        },


        )
}

@Composable
fun BottomBar(navController: NavController){
    NavigationBar(
        modifier = Modifier
            .height(120.dp),
        containerColor = Color(0xFF2264FF),
        contentColor = Color.White,

        ) {
        NavigationBarItem(
            icon = {
                ImageType(R.drawable.target)
            },
            selected = false,
            onClick = { navController.navigate(TargetPage())})
        NavigationBarItem(
            icon = {
                ImageType(R.drawable.apple)
            },
            selected = false,
            onClick = { navController.navigate(FoodRegistrationPage()) })
        NavigationBarItem(
            icon = {
                ImageType(R.drawable.lumous)
            },
            selected = false,
            onClick = { navController.navigate(AdvisePage()) })
}}

@Composable
fun ImageType(logoName: Int) {
    Image(
        painter = painterResource(logoName),  // Image locale dans drawable
        contentDescription = "Description de l'image",
        modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
            .aspectRatio(1f),
        contentScale = ContentScale.Crop
    )
}

