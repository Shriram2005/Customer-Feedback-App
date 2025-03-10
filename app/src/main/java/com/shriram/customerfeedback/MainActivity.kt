package com.shriram.customerfeedback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.shriram.customerfeedback.navigation.AppNavHost
import com.shriram.customerfeedback.navigation.Screen
import com.shriram.customerfeedback.ui.theme.CustomerFeedbackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    AppNavHost(navController = navController, Screen.Login.route)
}
