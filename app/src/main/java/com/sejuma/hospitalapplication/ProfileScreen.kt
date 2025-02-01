package com.sejuma.hospitalapplication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.sejuma.hospitalapplication.viewmodel.LoginMessageUiState
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel
//import com.sejuma.hospitalapplication.viewmodel.NurseViewModel
import com.sejuma.hospitalapplication.viewmodel.RemoteViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel,
    remoteViewModel: RemoteViewModel
) {
    // 🔥 Escuchar el estado de loginMessageUiState
    val loginState by remoteViewModel.loginMessageUiState.collectAsState()



    LaunchedEffect(loginState) {
        Log.d("ProfileScreen", "Recomposing UI with login state: $loginState")
    }


    // Extraer nurseId si el estado es Success
    val nurseId = when (val state = loginState) {
        is LoginMessageUiState.Success -> {
            state.loginMessage.nurseId
        }
        else -> null
    }
    Log.d("ProfileScreen", "Nurse ID: $nurseId")


    Log.d("ProfileScreen", "LoginState: $loginState")

    if (loginState is LoginMessageUiState.Success) {
        Log.d("ProfileScreen", "Nurse ID: ${(loginState as LoginMessageUiState.Success).loginMessage.nurseId}")
    }


    // Estado de eliminación
    val deleteState by remoteViewModel.deleteNurseState
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

        Log.d("ProfileScreen", "Logged in nurse ID: $nurseId")


        // Botón de eliminar enfermera
        if (nurseId != null) {
            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = {
                    Log.d("DeleteButton", "Deleting nurse with ID: $nurseId")
                    remoteViewModel.deleteNurse(nurseId)
                }
            ) {
                Text(text = "Delete Account")
            }
        }

        // Mensaje de estado de eliminación
        deleteState?.let {
            Text(
                text = if (it) "Nurse deleted successfully" else "Failed to delete nurse",
                modifier = Modifier.padding(top = 16.dp),
                color = if (it) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red,
                fontSize = 16.sp
            )
        }




    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val nurseViewModel = NurseViewModel()
    val remoteViewModel = RemoteViewModel()
    ProfileScreen(navController = navController, nurseViewModel = nurseViewModel, remoteViewModel = remoteViewModel)
}
