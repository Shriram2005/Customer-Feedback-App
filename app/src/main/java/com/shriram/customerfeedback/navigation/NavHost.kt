package com.shriram.customerfeedback.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shriram.customerfeedback.data.AppViewModel
import com.shriram.customerfeedback.screens.FeedbackDashboard
import com.shriram.customerfeedback.screens.FeedbackScreen
import com.shriram.customerfeedback.screens.LoginScreen
import com.shriram.customerfeedback.screens.RegisterScreen


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Feedback : Screen("feedback")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    viewModel: AppViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController, viewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, viewModel)
        }
        composable(Screen.Home.route) {
            FeedbackDashboard(navController, viewModel)
        }
        composable(Screen.Feedback.route) {
            FeedbackScreen(navController, viewModel)
        }
    }
}