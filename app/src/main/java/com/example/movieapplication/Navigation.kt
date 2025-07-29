package com.example.movieapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapplication.navigation.DetailsScreen
import com.example.movieapplication.navigation.SplashScreen
import com.example.movieapplication.navigation.StartScreen


@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Splash screen") {
        composable("Splash screen") {
            SplashScreen(navController)
        }
        composable("Start screen") {
            StartScreen(navController = navController)
        }
        composable("Details screen/{id}",
            arguments = listOf(
                navArgument(
                    name = "id"
                ) {
                    type = NavType.IntType
                }
            )
        ) {id->
            id.arguments?.getInt("id")?.let { id1->
                DetailsScreen(id =id1)
            }

        }
    }
}

