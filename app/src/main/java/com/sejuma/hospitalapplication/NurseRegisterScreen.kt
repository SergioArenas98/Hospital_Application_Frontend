package com.sejuma.hospitalapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sejuma.hospitalapplication.model.Nurse
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel

@Composable
fun NurseRegisterScreen(navController: NavHostController, nurseViewModel: NurseViewModel = viewModel()){

    var name by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageRes by remember { mutableStateOf(0) }
    var registerSuccess by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val nurses by nurseViewModel.nurses.observeAsState(emptyList())

    fun validateCredentials(user: String): Boolean {
        return !nurses.any{it.user == user}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to Register",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nurse Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text(text = "User") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Register Button
        Button(onClick = {
            registerSuccess = validateCredentials(user)
            showMessage = true
            if (registerSuccess) {
                var  newNurse: Nurse = Nurse(name, user, password, imageRes)
                nurseViewModel.addNurse(newNurse)
                navController.navigate("homeScreen")
            }
        }) {
            Text(text = stringResource(id = R.string.registerButton))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Do you already have an account? Click below to login")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.navigate("NurseLoginScreen")
        }) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NurseRegisterScreenPreview() {
    val navController = rememberNavController()
    NurseRegisterScreen(navController = navController)
}