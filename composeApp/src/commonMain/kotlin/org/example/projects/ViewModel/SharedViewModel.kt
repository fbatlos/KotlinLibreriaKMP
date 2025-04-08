package org.example.projects.ViewModel

import kotlinx.coroutines.flow.StateFlow

expect class SharedViewModel() {
    // Reemplazamos LiveData con StateFlow diferencia importante respecto a Android
    //ya que hacermos el contrato para la separación de capas.
    val textError: StateFlow<String>
    val username: StateFlow<String>
    val email: StateFlow<String>
    val contrasenia: StateFlow<String>
    val municipio: StateFlow<String>
    val provincia: StateFlow<String>
    val isLoginEnable: StateFlow<Boolean>
    val isLoading: StateFlow<Boolean>
    val isOpen: StateFlow<Boolean>
    val showDialog: StateFlow<Boolean>

    fun onLogChange(username: String, contrasenia: String)

    fun onRegisterChange(
        username: String,
        contrasenia: String,
        municipio: String,
        provincia: String,
        email: String
    )
    fun onShowDialog(showDialog: Boolean)
    fun textErrorChange(textError: String)
    fun onIsLoading(isLoading: Boolean)
    fun onIsOpen(isOpen: Boolean)
    fun newErrorText(error: String)

    fun loginEnable(username: String, contrasenia: String): Boolean
}