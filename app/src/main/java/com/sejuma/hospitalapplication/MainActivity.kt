package com.sejuma.hospitalapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
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
                        NurseLoginScreen(navController = navController, remoteViewModel = viewModel())
                    }
                    composable("homeScreen") {
                        HomeScreen(navController = navController)
                    }
                    composable("registerScreen") {
                        NurseRegisterScreen(navController = navController, remoteViewModel = viewModel())
                    }
                    composable("nurseSearchScreen") {
                        NurseSearchScreen(navController = navController, remoteViewModel = viewModel())
                    }
                    composable("profileScreen") {
                        ProfileScreen(navController = navController, nurseViewModel = viewModel(), remoteViewModel = viewModel())
                    }

                }
            }
        }
    }
}
