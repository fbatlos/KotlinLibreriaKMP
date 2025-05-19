package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.model.Direccion
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Stock
import org.example.projects.BaseDeDatos.model.TipoStock

class AuthViewModel (
    private val uiStateViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private var _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private var _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private var _contrasenia = MutableStateFlow<String>("")
    val contrasenia: StateFlow<String> = _contrasenia

    private var _direcciones = MutableStateFlow<MutableList<Direccion>>(mutableListOf())
    val direcciones: StateFlow<MutableList<Direccion>> = _direcciones

    private var _cesta = MutableStateFlow<MutableList<ItemCompra>>(mutableListOf())
    val cesta:StateFlow<MutableList<ItemCompra>> = _cesta

    private var _isLoginEnabled = MutableStateFlow(false)
    val isLoginEnable: StateFlow<Boolean> = _isLoginEnabled

    fun onLogChange(username: String, contrasenia: String) {
        _username.value = username
        _contrasenia.value = contrasenia
        _isLoginEnabled.value = loginEnable(username, contrasenia)
    }

    fun onRegisterChange(
        username: String,
        contrasenia: String,
        email: String
    ) {
        _username.value = username
        _contrasenia.value = contrasenia
        _email.value = email
        _isLoginEnabled.value = loginEnable(username, contrasenia)
    }

    fun loginEnable(username: String, contrasenia: String): Boolean {
        return username.isNotEmpty() && contrasenia.length >= 3
    }

    fun fetchLogin(username: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            val result = try {
                val loginResult = withContext(Dispatchers.IO) {
                    validarUsuario(username, password)
                }
                uiStateViewModel.setLoading(false)

                if (loginResult.first) {
                    sharedViewModel.setToken(loginResult.second)
                    fetchCesta(loginResult.second)
                    true
                } else {
                    uiStateViewModel.setTextError(loginResult.second)
                    uiStateViewModel.setShowDialog(true)
                    false
                }
            } catch (e: Exception) {
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
                false
            }
            callback(result)
        }
    }


    fun fetchUsuario(username: String) {
    }

     fun fetchCesta(token:String) {
        viewModelScope.launch {
            try {
                val response = API.apiService.getCesta(token)
                _cesta.value = response

            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
    }

    fun addDireccion(direccion: Direccion) {
        _direcciones.value.add(direccion)
    }
}

private suspend fun validarUsuario(username: String, password: String): Pair<Boolean, String> {
    return withContext(Dispatchers.IO) {
        try {
            val usuarioLoginDTO = UsuarioLoginDTO(username, password)
            val response = API.apiService.postLogin(usuarioLoginDTO)
            if (response.status.value == 200) {
                val tokenbd = response.body<Map<String, String>>()
                Pair(true, tokenbd["token"] ?: "Token no disponible")
            } else {
                val error = API.parseError(response)
                Pair(false, error.message)
            }
        } catch (e: Exception) {
            Pair(false, "Error: ${e.localizedMessage}")
        }
    }
}
