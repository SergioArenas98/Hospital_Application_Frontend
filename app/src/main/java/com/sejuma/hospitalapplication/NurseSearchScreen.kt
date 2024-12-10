package com.sejuma.hospitalapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel

@Composable
fun NurseSearchScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showResults by remember { mutableStateOf<Boolean>(false) }
    var processedQuery by remember { mutableStateOf<String>("") }

    // Get nurse data from ViewModel
    val nurses by nurseViewModel.nurses.observeAsState(emptyList())

    // Filter list of nurses based on search query
    val filteredNurses = nurses.filter {
        searchQuery.text.isNotEmpty() &&
                (it.name.contains(searchQuery.text, ignoreCase = true) ||
                        it.user.contains(searchQuery.text, ignoreCase = true))
    }

    // Limit to 3 results
    val limitedNurses = filteredNurses.take(3)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text(
            text = "Search Nurse",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp),
            style = TextStyle (
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
        )

        // Search field
        BasicTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                showResults = false},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .height(40.dp),
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    ) {
                        if (searchQuery.text.isEmpty()) {
                            Text(
                                text = "Insert a name or an username...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                style = TextStyle (
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        // Spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Search button
        Button(modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            onClick = {
            processedQuery = searchQuery.text
            showResults = true
        }) {
            Text(
                text = "Search",
                style = TextStyle (
                    fontSize = 14.sp
                )
            )
        }

        // Button to go back to main menu
        Button(modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            onClick = { navController.navigate("homeScreen") }) {
            Text(
                text = stringResource(id = R.string.backToMenuButton),
                style = TextStyle (
                    fontSize = 14.sp
                )
            )
        }

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Show results if search button was clicked
        if (showResults) {
            // If nurses were found
            if (limitedNurses.isNotEmpty()) {
                Text(text = "${limitedNurses.size} nurses found:", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))

                // Show results
                for (nurse in limitedNurses) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: ${nurse.name}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "User: ${nurse.user}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Password: ${nurse.password}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    // Spacer
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                // If no nurses were found
                Text(
                    text = "No nurse called '${searchQuery.text}' was found",
                    style = MaterialTheme.typography.labelLarge
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
