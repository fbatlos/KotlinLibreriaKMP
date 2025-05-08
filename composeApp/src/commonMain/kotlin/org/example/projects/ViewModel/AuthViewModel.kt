package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro

expect class AuthViewModel(
      uiStateViewModel: UiStateViewModel,
      sharedViewModel: SharedViewModel
): ViewModel {
    val username: StateFlow<String>
    val email: StateFlow<String>
    val contrasenia: StateFlow<String>
    val municipio: StateFlow<String>
    val provincia: StateFlow<String>
    val cesta : StateFlow<MutableList<ItemCompra>>
    val isLoginEnable: StateFlow<Boolean>

    fun onLogChange(username: String, contrasenia: String)
    fun onRegisterChange(
        username: String,
        contrasenia: String,
        municipio: String,
        provincia: String,
        email: String
    )

    fun fetchUsuario(username: String)

    fun loginEnable(username: String, contrasenia: String): Boolean

    fun fetchLogin(username: String, password: String, callback: (Boolean) -> Unit)

    fun fetchCesta(token: String)

}
