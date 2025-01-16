package com.sejuma.hospitalapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sejuma.hospitalapplication.model.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

sealed interface RemoteMessageUiState {
    data class Success(val remoteMessage: List<Nurse>) : RemoteMessageUiState

    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

interface RemoteNurseInterface {
    @GET("nurse/index")
    suspend fun getRemoteNurses(): List<Nurse>
}

class RemoteViewModel : ViewModel() {

    private val _remoteMessageUiState = MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Loading)
    val remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    fun getRemoteNurses() {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteMessageUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val endPoints = connection.create(RemoteNurseInterface::class.java)
                val response = endPoints.getRemoteNurses()
                _remoteMessageUiState.value = RemoteMessageUiState.Success(response)
            } catch (e: Exception) {
                Log.d("example", "RESPONSE ERROR ${e.message}")
                _remoteMessageUiState.value = RemoteMessageUiState.Error
            }
        }
    }
}
