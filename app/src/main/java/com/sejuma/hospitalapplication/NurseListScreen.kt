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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sejuma.hospitalapplication.viewmodel.NurseViewModel
import com.sejuma.hospitalapplication.model.Nurse

@Composable
fun NurseListScreen(onBackPressed: () -> Unit, nurseViewModel: NurseViewModel = viewModel()) {

    // Obtengo la lista de enfermeros de LiveData y lo observo con observeAsState()
    val nurses = nurseViewModel.nurses.observeAsState(initial = listOf())

    Column(modifier = Modifier.fillMaxSize()) {
        // Botón para regresar al menú principal
        Button(
            onClick = onBackPressed,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Back to Menu")
        }

        // Lista de enfermeras usando LazyColumn
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(nurses.value) { nurse ->
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
