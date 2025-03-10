package com.shriram.customerfeedback.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shriram.customerfeedback.screens.FeedbackScreen
import com.shriram.customerfeedback.screens.RegisterScreen


sealed class Screen(val route: String) {
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object Home: Screen("home")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination){
        composable(Screen.Login.route){
            LoginScreen(navController)
        }
        composable(Screen.Register.route){
            RegisterScreen(navController)
        }
        composable(Screen.Home.route){
            FeedbackScreen(navController)
        }
    }
}