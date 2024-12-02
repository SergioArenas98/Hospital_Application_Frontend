package com.sejuma.hospitalapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HospitalApplication()
            }
        }
    }
}

@Composable
fun HospitalApplication() {

    // Variable para controlar la pantalla de búsqueda
    var showNurseSearchScreen by remember { mutableStateOf(false) }
    var showNurseLoginScreen by remember { mutableStateOf(false) }
    var showNurseListScreen by remember { mutableStateOf(false) }

    // var showAllNurses by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        // If showNurseSearchScreen is true, show its screen
        if (showNurseSearchScreen) {
            NurseSearchScreen( onBackPressed = { showNurseSearchScreen = false })
        // If showNurseLogin is true, show its screen
        } else if (showNurseLoginScreen) {
            NurseLoginScreen( onBackPressed = { showNurseLoginScreen = false })
        // If showAllNurses is true, show its screen
        } else if (showNurseListScreen) {
            NurseListScreen( onBackPressed = { showNurseListScreen = false })
        // Show main menu if other options are false
        } else {
            Text(
                text = "Hello to Hospital Application!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 16.dp),
                style = TextStyle (
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic
                )
            )

            // Spacer
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                showNurseSearchScreen = true
            }) {
                Text(text = stringResource(id = R.string.nurseSearchButton))
            }

            // Spacer
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                showNurseLoginScreen = true
            }) {
                Text(text = stringResource(id = R.string.nurseLoginButton))
            }

            // Spacer
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                showNurseListScreen = true
            }) {
                Text(text = stringResource(id = R.string.showAllNursesButton))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScreenPreview() {
    HospitalApplication()
}