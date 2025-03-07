package com.sejuma.hospitalapplication

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome again!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        )

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Button to go to nurse search screen
        Button(onClick = { navController.navigate("profileScreen") }) {
            Text(
                text = stringResource(id = R.string.profileButton),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Button to show all nurses
        Button(onClick = { navController.navigate("nurseSearchScreen") }) {
            Text(
                text = stringResource(id = R.string.showNursesButton),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

        // Spacer
        Spacer(modifier = Modifier.height(100.dp))

        // Button to logout
        Button(onClick = { navController.navigate("NurseLoginScreen") }) {
            Text(
                text = stringResource(id = R.string.logoutButton),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}