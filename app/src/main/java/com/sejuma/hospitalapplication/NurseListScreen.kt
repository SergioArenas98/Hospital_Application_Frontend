package com.sejuma.hospitalapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Nurse(
    val name: String,
    val user: String,
    val password: String,
)

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

@Composable
fun NurseListScreen(onBackPressed: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) { // Corregido: Usar Modifier directamente
        // Botón para regresar al menú principal
        Button(
            onClick = onBackPressed,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back to Menu")
        }

        // Lista de enfermeras usando LazyColumn
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(nurses) { nurse ->
                NurseItem(nurse = nurse)
            }
        }
    }
}

@Composable
fun NurseItem(nurse: Nurse) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Name: ${nurse.name}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "User: ${nurse.user}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Password: ${nurse.password}", style = MaterialTheme.typography.bodySmall)
    }
}
