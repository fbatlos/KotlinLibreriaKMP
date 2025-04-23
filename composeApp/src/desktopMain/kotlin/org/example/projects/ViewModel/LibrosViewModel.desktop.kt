package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro

actual class LibrosViewModel actual constructor(
    private val uiStateViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    actual val libros: StateFlow<List<Libro>> = _libros

    private val _librosFavoritos = MutableStateFlow<List<String>>(emptyList())
    actual val librosFavoritos: StateFlow<List<String>> = _librosFavoritos

    private val _query = MutableStateFlow<String>("")
    actual val query: StateFlow<String> = _query


    actual fun fetchLibros() {
        uiStateViewModel.setLoading(true)
        try {
            // Realizamos la llamada a la API para obtener los libros
            val scope = CoroutineScope(Dispatchers.IO)
            scope.async {
                val result = API.apiService.listarLibros()
                _libros.value = result
            }
        } catch (e: Exception) {
            uiStateViewModel.setTextError("Error al cargar libros: ${e.message}")
            uiStateViewModel.setShowDialog(true)
        }finally {
            uiStateViewModel.setLoading(false)
        }
    }

    actual fun filtrarLibros(query: String) {
        _query.value = query
    }

    actual fun loadFavoritos() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.async {
            val favoritos = API.apiService.getLibrosfavoritos(sharedViewModel.token.value!!)
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

    actual fun addLibroFavorito(idLibro: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        uiStateViewModel.setLoading(true)
        scope.async {
            val favorito = API.apiService.addLibroFavorito(sharedViewModel.token.value!!, idLibro)

            if (favorito.status.value != 201){
                uiStateViewModel.setTextError(favorito.body())
                uiStateViewModel.setShowDialog(true)
            }
        }
        uiStateViewModel.setLoading(false)
    }

    actual fun removeLibroFavorito(idLibro: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        uiStateViewModel.setLoading(true)
        scope.async {
            val favorito = API.apiService.removeLibroFavorito(sharedViewModel.token.value!!, idLibro)

            if (favorito.status.value != 204){
                uiStateViewModel.setTextError(favorito.body())
                uiStateViewModel.setShowDialog(true)
            }
        }
        uiStateViewModel.setLoading(false)
    }

}