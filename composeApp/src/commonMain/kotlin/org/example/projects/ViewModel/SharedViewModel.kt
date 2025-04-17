package org.example.projects.ViewModel

import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro
import javax.management.Query

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
    val token: StateFlow<String?>
    val query : StateFlow<String>
    val libros: StateFlow<List<Libro>>
    val librosFavoritos: StateFlow<List<String>>

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
    fun getToken(token: String)

    fun getLibros(librosList: List<Libro>)

    fun loginEnable(username: String, contrasenia: String): Boolean

    fun filtrarLibros(query: String)

    fun loadFavoritos(token: String)

    fun updateFavoritos(add: Boolean, libroId: String)
}