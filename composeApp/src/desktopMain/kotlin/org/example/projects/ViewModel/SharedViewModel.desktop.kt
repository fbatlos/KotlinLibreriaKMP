package org.example.projects.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro

actual class SharedViewModel actual constructor() {

    private val _textError = MutableStateFlow<String>("")
    actual val textError: StateFlow<String> = _textError

    private val _username = MutableStateFlow<String>("")
    actual val username: StateFlow<String> = _username

    private val _email = MutableStateFlow<String>("")
    actual val email: StateFlow<String> = _email

    private val _contrasenia = MutableStateFlow<String>("")
    actual val contrasenia: StateFlow<String> = _contrasenia

    private val _municipio = MutableStateFlow<String>("")
    actual val municipio: StateFlow<String> = _municipio

    private val _provincia = MutableStateFlow<String>("")
    actual val provincia: StateFlow<String> = _provincia

    private val _isLoginEnabled = MutableStateFlow(false)
    actual val isLoginEnable: StateFlow<Boolean> = _isLoginEnabled

    private val _isLoading = MutableStateFlow(false)
    actual val isLoading: StateFlow<Boolean> = _isLoading

    private val _isOpen = MutableStateFlow(false)
    actual val isOpen: StateFlow<Boolean> = _isOpen

    private val _showDialog = MutableStateFlow(false)
    actual val showDialog: StateFlow<Boolean> = _showDialog

    private val _token = MutableStateFlow<String?>(null)
    actual val token: StateFlow<String?> = _token

    private val _librosFavoritos = MutableStateFlow<List<String>>(emptyList())
    actual val librosFavoritos: StateFlow<List<String>> = _librosFavoritos

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    actual val libros: StateFlow<List<Libro>> = _libros

    private val _query = MutableStateFlow<String>("")
    actual val query: StateFlow<String> = _query


    actual fun onLogChange(username: String, contrasenia: String) {
        _username.value = username
        _contrasenia.value = contrasenia
        _isLoginEnabled.value = loginEnable(username,contrasenia)
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
        _isLoginEnabled.value = loginEnable(username,contrasenia)
    }

    actual fun onShowDialog(showDialog: Boolean) {
        _showDialog.value = showDialog
    }

    actual fun textErrorChange(textError: String) {
        _textError.value = textError
    }

    actual fun onIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    actual fun onIsOpen(isOpen: Boolean) {
        _isOpen.value = isOpen
    }

    actual fun newErrorText(error: String) {
    }

    actual fun loginEnable(username: String, contrasenia: String): Boolean {
        return username.isNotEmpty() && contrasenia.length >= 3
    }



    actual fun getToken(token: String) {
        _token.value = token
    }


    actual fun getLibros(librosList: List<Libro>) {
        _libros.value = librosList
    }


    actual fun filtrarLibros(query: String) {
        _query.value = query
    }

    actual fun loadFavoritos(token: String) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.async {
            val favoritos = API.apiService.getLibrosfavoritos(token)
            _librosFavoritos.value = favoritos
        }
    }

    actual fun updateFavoritos(add: Boolean, libroId: String) {
        _librosFavoritos.value = if (add) {
            _librosFavoritos.value + libroId
        } else {
            _librosFavoritos.value.filter { it != libroId }
        }
    }

}