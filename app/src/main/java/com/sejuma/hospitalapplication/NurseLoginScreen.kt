package com.sejuma.hospitalapplication

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigator

@Composable
fun NurseLoginScreen(onBackPressed: () -> Unit){

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginSuccess by remember { mutableStateOf<Boolean?>(null) }

    // Nurse Class
    data class Nurse(
        val name: String,
        val user: String,
        val password: String,
    )
    // Nurse List
    val nurses = listOf(
        Nurse("Sergio", "sergio.nurse", "sergio123"),
        Nurse("David", "david.nurse", "david123"),
        Nurse("Jose", "jose.nurse", "jose123"),
        Nurse("Fiorella", "fiorella.nurse", "fiorella123"),
        Nurse("Sergio", "sergio.nurse", "sergio123"),
        Nurse("Jose", "jose.nurse", "jose123"),
        Nurse("Fiorella", "fiorella.nurse", "fiorella123"),
        Nurse("Sergio", "sergio.nurse", "sergio123"),
        Nurse("Jose", "jose.nurse", "jose123"),
        Nurse("Fiorella", "fiorella.nurse", "fiorella123"),
        Nurse("Sergio", "sergio.nurse", "sergio123"),
        Nurse("David", "david.nurse", "david123"),
        Nurse("Fiorella", "fiorella.nurse", "fiorella123")
    )


    Column(
        modifier = Modifier
            .padding(top = 100.dp)
            .padding(20.dp)
            .wrapContentSize()
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(text = "Welcome to Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)

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
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        fun validateCredentials(user: String, password: String): Boolean{
            return nurses.any { it.user == user && it.password == password }
        }

        @Composable
        fun LoginMessage(loginSuccess: Boolean) {
            if (loginSuccess) {
                Text(text = "You logged in successfully!", fontSize = 20.sp)
            } else {
                Text(text = "Invalid credentials", color = Color.Red)
            }
        }

        Button(onClick = {
            loginSuccess = validateCredentials(user, password)
        }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        loginSuccess?.let {
            LoginMessage(loginSuccess = it)
        }
    }
    Button(onClick = onBackPressed) {
        Text(text = "Back")
    }

    @Composable
    fun LoggedInView() {
        // Pantalla que se muestra después de iniciar sesión correctamente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .background(Color.Green)
        ) {
            Text(text = "Logged In", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}