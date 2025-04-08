package org.example.projects.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

}