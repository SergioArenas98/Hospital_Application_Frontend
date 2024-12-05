package com.sejuma.hospitalapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                NavHost(navController = navController, startDestination = "nurseLoginScreen") {
                    composable("nurseLoginScreen") {
                        NurseLoginScreen(navController = navController)
                    }
                    composable("homeScreen") {
                        HomeScreen(navController = navController)
                    }
                }
            }
        }
    }
}
