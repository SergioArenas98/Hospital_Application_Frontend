package com.sejuma.hospitalapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sejuma.hospitalapplication.model.LoginRequest
import com.sejuma.hospitalapplication.model.Nurse
import com.sejuma.hospitalapplication.model.RegisterRequest
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

    @POST("nurse/new")
    suspend fun register(@Body registerRequest: RegisterRequest): Nurse
}

class RemoteViewModel : ViewModel() {

    private val _remoteMessageUiState = MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Loading)
    var remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    private val _loginMessageUiState = MutableStateFlow<LoginMessageUiState>(LoginMessageUiState.Loading)
    var loginMessageUiState: StateFlow<LoginMessageUiState> = _loginMessageUiState

    val connection = Retrofit.Builder()
        .baseUrl("http://192.168.2.1:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val remoteService = connection.create(RemoteNurseInterface::class.java)

    // Get all Nurses
    fun getRemoteNurses() {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteMessageUiState.Loading
            try {
                Log.d("GetNurses", "Fetching nurses from server...")
                val response = remoteService.getRemoteNurses()
                Log.d("GetNurses", "Successfully fetched nurses: $response")
                _remoteMessageUiState.value = RemoteMessageUiState.Success(response)
            } catch (e: Exception) {
                Log.e("GetNurses", "Error fetching nurses: ${e.message}", e)
                _remoteMessageUiState.value = RemoteMessageUiState.Error
            }
        }
    }

    // Login
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginMessageUiState.value = LoginMessageUiState.Loading
            try {
                Log.d("Login", "Attempting to log in user: $username")
                val loginRequest = LoginRequest(user = username, password = password)
                val nurse = remoteService.login(loginRequest)
                Log.d("Login", "Login successful: $nurse")
                _loginMessageUiState.value = LoginMessageUiState.Success(nurse)
            } catch (e: retrofit2.HttpException) {
                Log.e("Login", "HTTP error: ${e.code()} - ${e.message}", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            } catch (e: java.net.UnknownHostException) {
                Log.e("Login", "Network error: Unable to resolve host", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            } catch (e: Exception) {
                Log.e("Login", "Unexpected error: ${e.message}", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            }
        }
    }

    // Registro
    fun register(name: String, username: String, password: String) {
        viewModelScope.launch {
            _loginMessageUiState.value = LoginMessageUiState.Loading
            try {
                Log.d("Registro", "Attempting to log in user: $username")
                val registerRequest = RegisterRequest(name = name, user = username, password = password)
                val nurse = remoteService.register(registerRequest)
                Log.d("Registro", "Login successful: $nurse")
                _loginMessageUiState.value = LoginMessageUiState.Success(nurse)
            } catch (e: retrofit2.HttpException) {
                Log.e("Registro", "HTTP error: ${e.code()} - ${e.message}", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            } catch (e: java.net.UnknownHostException) {
                Log.e("Registro", "Network error: Unable to resolve host", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            } catch (e: Exception) {
                Log.e("Registro", "Unexpected error: ${e.message}", e)
                _loginMessageUiState.value = LoginMessageUiState.Error
            }
        }
    }
}