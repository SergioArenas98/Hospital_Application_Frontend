package com.sejuma.hospitalapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Title text
        Text(
            text = "Profile",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            style = TextStyle (
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
        )

        // Button to go back to main menu
        Button(modifier = Modifier
            .padding(top = 20.dp, start = 8.dp),
            onClick = { navController.navigate("homeScreen") }) {
            Text(text = stringResource(id = R.string.backToMenuButton),
                style = TextStyle (
                    fontSize = 14.sp
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val nurseViewModel = NurseViewModel()
    ProfileScreen(navController = navController, nurseViewModel = nurseViewModel)
}
