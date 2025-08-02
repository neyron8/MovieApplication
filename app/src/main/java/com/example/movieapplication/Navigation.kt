package com.example.movieapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapplication.navigation.DetailsScreen
import com.example.movieapplication.navigation.LoginScreen
import com.example.movieapplication.navigation.PasswordScreen
import com.example.movieapplication.navigation.SignUpScreen
import com.example.movieapplication.navigation.SplashScreen
import com.example.movieapplication.navigation.StartScreen


@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Splash Screen") {
        composable("Splash Screen") {
            SplashScreen(navController)
        }
        composable("Start Screen") {
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
        composable("Login Screen") {
            LoginScreen(navController = navController)
        }
        composable("SignUp Screen") {
            SignUpScreen(navController = navController)
        }
        composable("Password Screen") {
            PasswordScreen(navController = navController)
        }
    }
}

