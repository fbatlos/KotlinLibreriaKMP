package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Stock
import org.example.projects.BaseDeDatos.model.TipoStock

actual class AuthViewModel actual constructor(
    private val uiStateViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private var _username = MutableStateFlow<String>("")
    actual val username: StateFlow<String> = _username

    private var _email = MutableStateFlow<String>("")
    actual val email: StateFlow<String> = _email

    private var _contrasenia = MutableStateFlow<String>("")
    actual val contrasenia: StateFlow<String> = _contrasenia

    private var _municipio = MutableStateFlow<String>("")
    actual val municipio: StateFlow<String> = _municipio

    private var _provincia = MutableStateFlow<String>("")
    actual val provincia: StateFlow<String> = _provincia

    private var _cesta = MutableStateFlow<MutableList<ItemCompra>>(mutableListOf())
    actual val cesta:StateFlow<MutableList<ItemCompra>> = _cesta

    private var _isLoginEnabled = MutableStateFlow(false)
    actual val isLoginEnable: StateFlow<Boolean> = _isLoginEnabled

    actual fun onLogChange(username: String, contrasenia: String) {
        _username.value = username
        _contrasenia.value = contrasenia
        _isLoginEnabled.value = loginEnable(username, contrasenia)
    }

    actual fun onRegisterChange(
        username: String,
        contrasenia: String,
        municipio: String,
        provincia: String,
        email: String
    ) {
        _username.value = username
        _contrasenia.value = contrasenia
        _municipio.value = municipio
        _provincia.value = provincia
        _email.value = email
        _isLoginEnabled.value = loginEnable(username, contrasenia)
    }

    actual fun loginEnable(username: String, contrasenia: String): Boolean {
        return username.isNotEmpty() && contrasenia.length >= 3
    }

    actual fun fetchLogin(username: String, password: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                uiStateViewModel.setLoading(true)
            }
            val result = try {
                val loginResult = validarUsuario(username, password)

                withContext(Dispatchers.Main) {
                    uiStateViewModel.setLoading(false)
                }

                if (loginResult.first) {
                    withContext(Dispatchers.Main) {
                        sharedViewModel.setToken(loginResult.second)
                    }
                    fetchCesta(loginResult.second)
                    true
                } else {
                    withContext(Dispatchers.Main) {
                        uiStateViewModel.setTextError(loginResult.second)
                        uiStateViewModel.setShowDialog(true)
                    }
                    false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiStateViewModel.setLoading(false)
                    uiStateViewModel.setTextError("Error: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
                false
            }
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }


    actual fun fetchUsuario(username: String) {
    }

    actual fun fetchCesta(token:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = API.apiService.getCesta(token)
                _cesta.value = response

            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
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