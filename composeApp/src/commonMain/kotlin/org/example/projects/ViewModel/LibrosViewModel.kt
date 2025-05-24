package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Valoracion

class LibrosViewModel (
    private val uiStateViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros

    private val _librosFavoritos = MutableStateFlow<List<String>>(emptyList())
    val librosFavoritos: StateFlow<List<String>> = _librosFavoritos

    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    private val _librosSugeridosCategorias = MutableStateFlow<List<Libro>>(emptyList())
    val librosSugeridosCategorias: StateFlow<List<Libro>> = _librosSugeridosCategorias

    private val _librosSelected = MutableStateFlow<Libro?>(null)
    val libroSelected: StateFlow<Libro?> = _librosSelected

    private val _valoraciones = MutableStateFlow<List<Valoracion>?>(null)
    val valoraciones: StateFlow<List<Valoracion>?> = _valoraciones

    private val _mediaValoracionesPorLibro = MutableStateFlow<Map<String, Double>>(emptyMap())
    val mediaValoracionesPorLibro: StateFlow<Map<String, Double>> = _mediaValoracionesPorLibro


    fun fetchLibros() {
        uiStateViewModel.setLoading(true)
        viewModelScope.launch {
            try {
                val result = API.apiService.listarLibros(null, null)
                _libros.value = result
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al cargar libros: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }
    }

    fun filtrarLibros(query: String) {
        _query.value = query
    }

    fun loadFavoritos() {
        if (sharedViewModel.token.value != null) {
            viewModelScope.launch {
                try {
                    val favoritos = API.apiService.getLibrosfavoritos(sharedViewModel.token.value!!)
                    _librosFavoritos.value = favoritos
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error al cargar libros: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun updateFavoritos(add: Boolean, libroId: String) {
        _librosFavoritos.value = if (add) {
            _librosFavoritos.value + libroId
        } else {
            _librosFavoritos.value.filter { it != libroId }
        }
    }

     fun addLibroFavorito(idLibro: String) {
         if (sharedViewModel.token.value != null) {
             viewModelScope.launch {
                 try {
                     API.apiService.addLibroFavorito(sharedViewModel.token.value!!, idLibro)
                 } catch (e: Exception) {
                     uiStateViewModel.setTextError("Error al añadir libros favoritos: ${e.message}")
                     uiStateViewModel.setShowDialog(true)
                 }
             }
         }
    }

    fun removeLibroFavorito(idLibro: String) {
        if (sharedViewModel.token.value != null) {
            viewModelScope.launch {
                try {

                    API.apiService.removeLibroFavorito(sharedViewModel.token.value!!, idLibro)
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error al eliminar libros favoritos: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun getLibrosByCategorias(categoria: String) {
        viewModelScope.launch { // Usa el viewModelScope de Moko-MVVM
            uiStateViewModel.setLoading(true)
            try {
                val result = API.apiService.listarLibros(categoria = categoria, null)
                _librosSugeridosCategorias.value = result
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al cargar libros: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }
    }


    fun putLibroSelected(libro: Libro) {
        _librosSelected.value = libro
    }


     fun fetchValoraciones(idLibro: String) {
         viewModelScope.launch {
             try {
                 val valoraciones = API.apiService.getValoraciones(idLibro)
                 val media = if (valoraciones.isNotEmpty()) {
                     valoraciones.map { it.valoracion }.average()
                 } else 0.0
                _valoraciones.value = valoraciones
                 _mediaValoracionesPorLibro.value = _mediaValoracionesPorLibro.value + (idLibro to media)

             } catch (e: Exception) {
                 uiStateViewModel.setTextError("Error al obtener valoraciones: ${e.message}")
                 uiStateViewModel.setShowDialog(true)
             }
         }
    }

    fun addValoracion(
        valoracion: Valoracion
    ){
        uiStateViewModel.setLoading(true)
        viewModelScope.launch {
            try {
                API.apiService.addValoracion(valoracion, sharedViewModel.token.value!!)
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al añadir valoración: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            } finally {
                uiStateViewModel.setLoading(false)
            }
        }
    }
}