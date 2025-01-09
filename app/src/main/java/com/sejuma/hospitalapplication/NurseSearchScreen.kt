package com.sejuma.hospitalapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sejuma.hospitalapplication.model.Nurse
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel

@Composable
fun NurseSearchScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Get nurse data from ViewModel
    val nurses by nurseViewModel.nurses.observeAsState(initial = listOf())

    // Filter nurses based on search query
    val filteredNurses = nurses.filter {
        searchQuery.text.isEmpty() ||
                it.name.contains(searchQuery.text, ignoreCase = true) ||
                it.user.contains(searchQuery.text, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        // Title
        Text(
            text = "Search Nurse",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
        )

        // Search field
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(40.dp),
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        if (searchQuery.text.isEmpty()) {
                            Text(
                                text = "Insert a name or an username...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                style = TextStyle(fontSize = 14.sp),
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        // Nurse List
        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(filteredNurses) { nurse ->
                NurseItem(nurse = nurse)
            }
        }

        // Spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Button to go back to main menu
        Button(modifier = Modifier.padding(top = 8.dp),
            onClick = { navController.navigate("homeScreen") }) {
            Text(
                text = stringResource(id = R.string.backToMenuButton),
                style = TextStyle (
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun NurseItem(nurse: Nurse) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            // User image
            Image(
                painter = painterResource(id = nurse.imageRes),
                contentDescription = "Image of Nurse ${nurse.name}",
                modifier = Modifier
                    .size(64.dp)
                    .padding(start = 8.dp)
            )

            // User information
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = nurse.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "User: ${nurse.user}",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Password: ${nurse.password}",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NurseSearchScreenPreview() {
    val navController = rememberNavController()
    val nurseViewModel = NurseViewModel()
    NurseSearchScreen(navController = navController, nurseViewModel = nurseViewModel)
}
