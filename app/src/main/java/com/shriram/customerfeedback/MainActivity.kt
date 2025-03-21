package com.shriram.customerfeedback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.shriram.customerfeedback.data.AppViewModel
import com.shriram.customerfeedback.navigation.AppNavHost
import com.shriram.customerfeedback.navigation.Screen
import com.shriram.customerfeedback.ui.theme.CustomerFeedbackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Transparent bg status bar
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {
            CustomerFeedbackTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    
    AppNavHost(
        navController = navController, 
        startDestination = Screen.Login.route,
        viewModel = viewModel
    )
}
