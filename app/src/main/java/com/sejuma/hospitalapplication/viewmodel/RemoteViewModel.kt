package com.sejuma.hospitalapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sejuma.hospitalapplication.model.LoginRequest
import com.sejuma.hospitalapplication.model.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

sealed interface RemoteMessageUiState {
    data class Success(val remoteMessage: List<Nurse>) : RemoteMessageUiState

    object Error : RemoteMessageUiState
    object Loading : RemoteMessageUiState
}

sealed interface LoginMessageUiState {
    data class Success(val loginMessage: Nurse) : LoginMessageUiState

    object Error : LoginMessageUiState
    object Loading : LoginMessageUiState
}

interface RemoteNurseInterface {
    @GET("nurse/index")
    suspend fun getRemoteNurses(): List<Nurse>

    @POST("nurse/login")
    suspend fun login(@Body loginRequest: LoginRequest): Nurse

}

class RemoteViewModel : ViewModel() {

    private val _remoteMessageUiState = MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Loading)
    var remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    private val _loginMessageUiState = MutableStateFlow<LoginMessageUiState>(LoginMessageUiState.Loading)
    var loginMessageUiState: StateFlow<LoginMessageUiState> = _loginMessageUiState

    val connection = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Get all Nurses
    fun getRemoteNurses() {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteMessageUiState.Loading
            try {
                val endPoints = connection.create(RemoteNurseInterface::class.java)
                val response = endPoints.getRemoteNurses()
                _remoteMessageUiState.value = RemoteMessageUiState.Success(response)
            } catch (e: Exception) {
                _remoteMessageUiState.value = RemoteMessageUiState.Error
            }
        }
    }

    // Login
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginMessageUiState.value = LoginMessageUiState.Loading
            try {
                val endPoints = connection.create(RemoteNurseInterface::class.java)
                val loginRequest = LoginRequest(user = username, password = password)
                val nurse = endPoints.login(loginRequest)
                _loginMessageUiState.value = LoginMessageUiState.Success(nurse)
            } catch (e: Exception) {
                Log.e("Login", "Error durante el login: ${e.message}", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            }
        }
    }
}