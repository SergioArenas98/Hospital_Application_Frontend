package com.sejuma.hospitalapplication

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.platform.LocalContext
import com.sejuma.hospitalapplication.model.Nurse
import com.sejuma.hospitalapplication.viewmodel.GetNurseMessageUiState
import com.sejuma.hospitalapplication.viewmodel.RemoteMessageUiState
import com.sejuma.hospitalapplication.viewmodel.RemoteViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    remoteViewModel: RemoteViewModel
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        var nurse by remember { mutableStateOf<Nurse?>(null) }
        val context = LocalContext.current
        val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val nurseId = sharedPref.getInt("nurse_id", 0) // Retorna 0 si no encuentra el valor

        var nurseUpdated : Nurse
        var name by remember { mutableStateOf("") }
        var user by remember { mutableStateOf("") }
        val password by remember { mutableStateOf("") }
        var imageFile by remember { mutableStateOf("") }


        if (nurseId != 0) {
            Log.d("IDPASS", "The id passed is $nurseId")

            // Llamada a la función para obtener los datos del enfermero
            LaunchedEffect(nurseId) {
                remoteViewModel.getNurseById(nurseId)
            }

            // Observar el estado de la UI
            val getNurseMessageUiState by remoteViewModel.getNurseMessageUiState.collectAsState()

            // Title text
            Text(
                text = "Profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
            )

            // Mostrar información según el estado de la petición
            when (getNurseMessageUiState) {
                is GetNurseMessageUiState.Loading -> {
                    Text(
                        text = "Loading profile...",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is GetNurseMessageUiState.Success -> {
                    nurse = (getNurseMessageUiState as GetNurseMessageUiState.Success).getNurseMessage

                    nurse?.let {
                        Text(
                            text = "Name: ${it.name}",
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "User: ${it.user}",
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                is GetNurseMessageUiState.Error -> {
                    Text(
                        text = "Error loading profile.",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }



            //Update
// Campos editables
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )






            Button(modifier = Modifier
                .padding(top = 20.dp, start = 8.dp),
                onClick = { nurse?.let {
                    val updatedNurse = it.copy(name = name, user = user, password = password, imageFile = imageFile)
                    remoteViewModel.updateNurse(nurseId, updatedNurse)
                    navController.navigate("profileScreen")
                } }) {
                Text(
                    text = stringResource(id = R.string.updateButton),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }



            //Delete
            Button(modifier = Modifier
                .padding(top = 20.dp, start = 8.dp),
                onClick = { remoteViewModel.deleteNurse(nurseId)
                    navController.navigate("nurseLoginScreen") }) {
                Text(
                    text = stringResource(id = R.string.deleteButton),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }





            // Button to go back to main menu
            Button(modifier = Modifier
                .padding(top = 20.dp, start = 8.dp),
                onClick = { navController.navigate("homeScreen") }) {
                Text(
                    text = stringResource(id = R.string.backToMenuButton),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
            }




        }
    }
}

