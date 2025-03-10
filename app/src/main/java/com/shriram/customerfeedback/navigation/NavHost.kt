package com.shriram.customerfeedback.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shriram.customerfeedback.data.AppViewModel
import com.shriram.customerfeedback.data.Feedback
import com.shriram.customerfeedback.screens.AdminDashboard
import com.shriram.customerfeedback.screens.FeedbackScreen
import com.shriram.customerfeedback.screens.LoginScreen
import com.shriram.customerfeedback.screens.RegisterScreen
import com.shriram.customerfeedback.screens.UserDashboard


sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Admin : Screen("admin")
    data object Feedback : Screen("feedback?feedbackId={feedbackId}") {
        fun createRoute(feedbackId: String? = null) = "feedback?feedbackId=$feedbackId"
    }
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
            UserDashboard(navController, viewModel)
        }
        composable(Screen.Admin.route) {
            AdminDashboard(navController, viewModel)
        }
        composable(
            route = Screen.Feedback.route,
            arguments = listOf(
                navArgument("feedbackId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val feedbackId = backStackEntry.arguments?.getString("feedbackId")
            val feedbackToEdit = feedbackId?.let { id ->
                if (viewModel.isAdmin) {
                    viewModel.allFeedbacksList.find { it.id == id }?.let { feedbackWithUser ->
                        Feedback(
                            id = feedbackWithUser.id,
                            userId = feedbackWithUser.userId,
                            text = feedbackWithUser.text,
                            timestamp = feedbackWithUser.timestamp
                        )
                    }
                } else {
                    viewModel.feedbackList.find { it.id == id }
                }
            }
            FeedbackScreen(navController, viewModel, feedbackToEdit)
        }
    }
}