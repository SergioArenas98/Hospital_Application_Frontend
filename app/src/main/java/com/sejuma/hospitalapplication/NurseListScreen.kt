package com.sejuma.hospitalapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.sejuma.hospitalapplication.model.Nurse

@Composable
fun NurseListScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel
) {

    // Get nurse data from ViewModel
    val nurses = nurseViewModel.nurses.observeAsState(initial = listOf())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Title text
        Text(
            text = "Search Nurse",
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

        // Listing nurses using LazyColumn
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp)) {
            items(nurses.value) { nurse ->
                NurseItem(nurse = nurse)
            }
        }
    }
}

@Composable
fun NurseItem(nurse: Nurse) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Name: ${nurse.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "User: ${nurse.user}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Password: ${nurse.password}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NurseListScreenPreview() {
    val navController = rememberNavController()
    val nurseViewModel = NurseViewModel()
    NurseListScreen(navController = navController, nurseViewModel = nurseViewModel)
}
