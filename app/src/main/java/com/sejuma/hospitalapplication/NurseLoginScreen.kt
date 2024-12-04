package com.sejuma.hospitalapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel

@Composable
fun NurseLoginScreen(
    onBackPressed: () -> Unit,
    nurseViewModel: NurseViewModel = viewModel()
) {
    // State variables for user input
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginSuccess by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val nurses by nurseViewModel.nurses.observeAsState(emptyList())

    // Function to validate credentials
    fun validateCredentials(user: String, password: String): Boolean {
        return nurses.any { it.user == user && it.password == password }
    }
    Button(onClick = onBackPressed) {
        Text(text = "Back")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to Login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text(text = "UserName") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            loginSuccess = validateCredentials(user, password)
            showMessage = true
        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showMessage) {
            if (loginSuccess) {
                Text(
                    text = "You logged in successfully!",
                    fontSize = 18.sp,
                    color = Color.Green
                )
            } else {
                Text(
                    text = "Invalid credentials",
                    fontSize = 18.sp,
                    color = Color.Red
                )
            }
        }
    }

}
