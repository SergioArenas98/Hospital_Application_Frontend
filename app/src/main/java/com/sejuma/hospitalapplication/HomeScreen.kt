package com.sejuma.hospitalapplication

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
            .fillMaxSize()
            .padding(10.dp)
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Button(onClick = {
            navController.navigate("NurseLoginScreen")
        }) {
            Text(text = "LogOut")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Hello to Hospital Application!",
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
        Button(onClick = { navController.navigate("nurseSearchScreen") }) {
            Text(
                text = stringResource(id = R.string.nurseSearchButton),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

        // Spacer
        Spacer(modifier = Modifier.height(10.dp))

        // Button to show all nurses
        Button(onClick = { navController.navigate("nurseListScreen") }) {
            Text(
                text = stringResource(id = R.string.showAllNursesButton),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
