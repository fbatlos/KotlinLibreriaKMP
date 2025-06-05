package org.example.projects.ViewModel

import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.model.Avatar
import org.example.projects.BaseDeDatos.model.Direccion
import org.example.projects.Screens.convertByteArrayToImageBitmap
import java.util.concurrent.TimeoutException

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

    private var _imagenUsuario = MutableStateFlow<ImageBitmap?>(null)
    val imagenUsuario = _imagenUsuario

    private var _idAvatarUsuario = MutableStateFlow<String?>(null)
    val idAvatarUsuario: StateFlow<String?> = _idAvatarUsuario

    private var _listaAvatares = MutableStateFlow<List<Avatar>>(emptyList())
    val listaAvatares: StateFlow<List<Avatar>> = _listaAvatares



    private var _isLoginEnabled = MutableStateFlow(false)
    val isLoginEnable: StateFlow<Boolean> = _isLoginEnabled

    fun onLogChange(username: String, contrasenia: String) {
        _username.value = username.replace(" ","")
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

    fun fetchRegister(username: String, email: String, password: String, passwordRepeat: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            val result = try {
                val resultRegister = withContext(Dispatchers.IO) {
                    API.apiService.postRegister(UsuarioRegisterDTO(username,email,password,passwordRepeat))
                }
                uiStateViewModel.setLoading(false)
                sharedViewModel.setToken(resultRegister.token)
                //TODO HACER EL GET USARIO Y HACER LA PANTALLA DE MI USUARIO
                true

            }catch (e:TimeoutException){
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: Se excedió el tiempo de espera de la API")
                uiStateViewModel.setShowDialog(true)
                false
            }catch (e: Exception) {
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
                false
            }
            callback(result)
        }
    }


    fun fetchUsuario() {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                val usuario = API.apiService.getUsuario(sharedViewModel.token.value!!)
                _username.value = usuario.username
                _email.value = usuario.email
                _idAvatarUsuario.value = usuario.avatar

                // Cargar el avatar automáticamente cuando tenemos el ID
                if (usuario.avatar != null) {
                    fetchMiAvatar()
                }
            }catch (e:Exception){
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }finally {
                uiStateViewModel.setLoading(false)
            }
        }
    }

    fun fetchMiAvatar() {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                val avatarResponse = API.apiService.getMiAvatar(_idAvatarUsuario.value!!,sharedViewModel.token.value!!)
                val imageBytes = avatarResponse.data?.toByteArray() ?: return@launch
                _imagenUsuario.value = convertByteArrayToImageBitmap(imageBytes)
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al obtener avatar: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }

        println(_imagenUsuario.value)
    }

    fun fetchAllAvatares() {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                val avataresResponse = API.apiService.getAllAvatares(sharedViewModel.token.value!!)
                _listaAvatares.value = avataresResponse
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al obtener avatares: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }

        println(_listaAvatares.value )
    }

    fun cambiarAvatar(avatarId: String) {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                val result = API.apiService.updateUsuarioAvatar(avatarId, sharedViewModel.token.value!!)
                _idAvatarUsuario.value = avatarId
                fetchMiAvatar()
            } catch (e: Exception) {
                uiStateViewModel.setTextError(e.message.toString())
                uiStateViewModel.setShowDialog(true)
                // Manejo de errores
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }
    }




    fun addDireccion(direccion: Direccion) {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                API.apiService.addDireccion(sharedViewModel.token.value!!, direccion)
                fetchUsuario()
                uiStateViewModel.setLoading(false)
            } catch (e: Exception) {
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
    }

    fun deleteDireccion(direccion: Direccion) {
        viewModelScope.launch {
            uiStateViewModel.setLoading(true)
            try {
                API.apiService.deleteDireccion(sharedViewModel.token.value!!, direccion)
                fetchUsuario()
                uiStateViewModel.setLoading(false)
            } catch (e: Exception) {
                uiStateViewModel.setLoading(false)
                uiStateViewModel.setTextError("Error: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
    }


    fun logout(){
        _username.value = ""
        _email.value = ""
        _contrasenia.value = ""
        _direcciones.value = mutableListOf()
        _imagenUsuario.value = null
    }


    fun selectedImagenUsuario(bitmap: ImageBitmap){
        _imagenUsuario.value = bitmap
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
        }
        catch (e: TimeoutException) {
            Pair(false, "Tiempo de espera excedido")
        }
        catch (e: Exception) {
            Pair(false, "Error: ${e.localizedMessage}")
        }

    }
}
