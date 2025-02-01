package com.sejuma.hospitalapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @DELETE("nurse/{nurseId}")
    suspend fun deleteNurse(@Path("nurseId") id: Int): Boolean
}

class RemoteViewModel : ViewModel() {



    private val _remoteMessageUiState = MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Loading)
    var remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    private val _loginMessageUiState = MutableStateFlow<LoginMessageUiState>(LoginMessageUiState.Loading)
    var loginMessageUiState: StateFlow<LoginMessageUiState> = _loginMessageUiState

    private val _loggedInNurse = MutableStateFlow<Nurse?>(null) // 🔹 Guarda la enfermera logueada
    val loggedInNurse: StateFlow<Nurse?> = _loggedInNurse

    var deleteNurseState = mutableStateOf<Boolean?>(null)

    val connection = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
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
                Log.d("RemoteViewModel", "Updated loginMessageUiState: ${_loginMessageUiState.value}")

                _loggedInNurse.value = nurse // 🔹 Guardar enfermera logueada
                Log.d("Login", "Nurse logged in and stored: ${_loggedInNurse.value}")

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
            Log.d("Login", "Login state updated to: ${_loginMessageUiState.value}")

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
                _loggedInNurse.value = nurse // 🔹 Guardar enfermera después de registrarse
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

    //Delete
    fun deleteNurse(nurseId: Int) {
        viewModelScope.launch {
            try {
                deleteNurseState.value = null
                val response = remoteService.deleteNurse(nurseId)
                deleteNurseState.value = response

                if(response) {
                    Log.d("DeleteNurse", "Nurse with ID $nurseId deleted successfully")
                    _loggedInNurse.value = null // 🔹 Eliminar usuario logueado
                    _loginMessageUiState.value = LoginMessageUiState.Loading // Reiniciar sesión
                } else {
                    Log.e("DeleteNurse", "Failed to delete nurse with ID $nurseId")

                }

            } catch (e: Exception) {
                Log.e("DeleteNurse", "Error deleting nurse: ${e.message}", e)
                deleteNurseState.value = false
            }

        }
    }
}