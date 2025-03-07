package com.sejuma.hospitalapplication.viewmodel

import android.content.Context
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

import retrofit2.http.PUT
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

sealed interface GetNurseMessageUiState {
    object Loading : GetNurseMessageUiState, RemoteMessageUiState
    data class Success(val getNurseMessage: Nurse) : GetNurseMessageUiState, RemoteMessageUiState
    object Error : GetNurseMessageUiState, RemoteMessageUiState
}


sealed interface DeleteMessageUiState {
    object Loading : DeleteMessageUiState, RemoteMessageUiState
    data class Success(val deleteMessage: Boolean) : DeleteMessageUiState, RemoteMessageUiState
    object Error : DeleteMessageUiState, RemoteMessageUiState
}

sealed interface UpdateMessageUiState {
    object Loading : UpdateMessageUiState, RemoteMessageUiState
    data class Success(val updateMessage: Nurse) : UpdateMessageUiState, RemoteMessageUiState
    object Error : UpdateMessageUiState, RemoteMessageUiState
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
  
    @GET("nurse/{nurseId}")
    suspend fun getNurseById(@Path("nurseId") nurseId: Int): Nurse
  
    @PUT("nurse/{nurseId}")
    suspend fun updateNurse(@Path("nurseId") nurseId: Int, @Body updatedNurse: Nurse): Nurse
}

class RemoteViewModel : ViewModel() {

    private val _remoteMessageUiState =
        MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Loading)
    var remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    private val _loginMessageUiState =
        MutableStateFlow<LoginMessageUiState>(LoginMessageUiState.Loading)
    var loginMessageUiState: StateFlow<LoginMessageUiState> = _loginMessageUiState

    private val _getNurseMessageUiState =
        MutableStateFlow<GetNurseMessageUiState>(GetNurseMessageUiState.Loading)
    var getNurseMessageUiState: StateFlow<GetNurseMessageUiState> = _getNurseMessageUiState


    private val _updateNurseUiState = MutableStateFlow<UpdateMessageUiState>(UpdateMessageUiState.Loading)
    val updateNurseUiState: StateFlow<UpdateMessageUiState> = _updateNurseUiState

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
    fun login(username: String, password: String, context: Context) {
        viewModelScope.launch {
            _loginMessageUiState.value = LoginMessageUiState.Loading
            try {
                Log.d("Login", "Attempting to log in user: $username")
                val loginRequest = LoginRequest(user = username, password = password)
                val nurse = remoteService.login(loginRequest)

                // Guardar el ID del usuario en SharedPreferences
                val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("nurse_id", nurse.nurseId)
                    apply()
                }

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
    fun register(name: String, username: String, password: String, context: Context) {
        viewModelScope.launch {
            _loginMessageUiState.value = LoginMessageUiState.Loading
            try {
                Log.d("Registro", "Attempting to log in user: $username")
                val registerRequest =
                    RegisterRequest(name = name, user = username, password = password)
                val nurse = remoteService.register(registerRequest)

                // Guardar el ID del usuario en SharedPreferences
                val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("nurse_id", nurse.nurseId)
                    apply()
                }

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


    fun getNurseById(nurseId: Int) {
        viewModelScope.launch {
            _getNurseMessageUiState.value = GetNurseMessageUiState.Loading
            try {
                Log.d("GetNurse", "Fetching nurse from server...")
                val response = remoteService.getNurseById(nurseId)
                Log.d("GetNurse", "Successfully fetched nurse: $response")
                _getNurseMessageUiState.value = GetNurseMessageUiState.Success(response)
            } catch (e: Exception) {
                Log.e("GetNurse", "Error fetching nurse: ${e.message}", e)
                _getNurseMessageUiState.value = GetNurseMessageUiState.Error
            }
        }
    }


    //Update
    fun updateNurse(nurseId: Int, updatedNurse: Nurse) {
        viewModelScope.launch {
            _updateNurseUiState.value = UpdateMessageUiState.Loading
            try {
                Log.d("UpdateNurse", "Updating nurse with ID: $nurseId...")
                val response = remoteService.updateNurse(nurseId, updatedNurse)
                Log.d("UpdateNurse", "Successfully updated nurse: $response")
                _updateNurseUiState.value = UpdateMessageUiState.Success(response)
            } catch (e: retrofit2.HttpException) {
                Log.e("UpdateNurse", "HTTP error: ${e.code()} - ${e.message}", e)
                _updateNurseUiState.value = UpdateMessageUiState.Error
            } catch (e: java.net.UnknownHostException) {
                Log.e("UpdateNurse", "Network error: Unable to resolve host", e)
                _updateNurseUiState.value = UpdateMessageUiState.Error
            } catch (e: Exception) {
                Log.e("UpdateNurse", "Unexpected error: ${e.message}", e)
                _updateNurseUiState.value = UpdateMessageUiState.Error
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