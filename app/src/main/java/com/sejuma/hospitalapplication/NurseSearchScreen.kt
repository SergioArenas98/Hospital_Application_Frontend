package com.sejuma.hospitalapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sejuma.hospitalapplication.model.Nurse
import com.sejuma.hospitalapplication.viewmodel.GetNurseMessageUiState
import com.sejuma.hospitalapplication.viewmodel.RemoteMessageUiState
import com.sejuma.hospitalapplication.viewmodel.RemoteViewModel
import java.lang.reflect.Field

@Composable
fun NurseSearchScreen(
    navController: NavController,
    remoteViewModel: RemoteViewModel
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val remoteMessageUiState by remoteViewModel.remoteMessageUiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("NurseSearch", "Fetching nurses...")
        remoteViewModel.getRemoteNurses()
    }

    // Render UI based on the state
    when (remoteMessageUiState) {
        is RemoteMessageUiState.Loading -> {
            // Show loading icon
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Loading nurses...",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
        is RemoteMessageUiState.Error -> {
            // Show error message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Failed to load nurses.",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                )
            }
        }
        is RemoteMessageUiState.Success -> {
            // Aquí manejamos correctamente el éxito
            val nurses = (remoteMessageUiState as RemoteMessageUiState.Success).remoteMessage

            // Filtrar enfermeras según la búsqueda
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
                // Título
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

                // Campo de búsqueda
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

                // Lista de enfermeras
                LazyColumn(modifier = Modifier
                    .weight(1f, fill = false)
                ) {
                    items(filteredNurses) { nurse ->
                        NurseItem(nurse = nurse)
                    }
                }

                // Espaciador
                Spacer(modifier = Modifier.height(8.dp))

                // Botón para regresar al menú principal
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

        GetNurseMessageUiState.Error -> TODO()
        GetNurseMessageUiState.Loading -> TODO()
        is GetNurseMessageUiState.Success -> TODO()
    }
}

fun getResId(resName: String, c: Class<*>): Int {
    return try {
        val idField: Field = c.getDeclaredField(resName)
        idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}

@Composable
fun NurseItem(nurse: Nurse) {

    val imageResId = remember(nurse.imageFile) {
        getResId(
            nurse.imageFile.substringBeforeLast("."), // Quitar extensión
            R.drawable::class.java
        )
    }

    val painter = if (imageResId != -1) {
        painterResource(id = imageResId)
    } else {
        painterResource(id = R.drawable.placeholder_image) // Imagen por defecto si no encuentra la imagen
    }

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
                Image(
                    painter = painter,
                    contentDescription = "Image of Nurse ${nurse.name}",
                    modifier = Modifier.size(64.dp)
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