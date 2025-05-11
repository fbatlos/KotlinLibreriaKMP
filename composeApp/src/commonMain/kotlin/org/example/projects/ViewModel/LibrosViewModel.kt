package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro

expect class LibrosViewModel(
    uiStateViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel
) : ViewModel {
    val libros: StateFlow<List<Libro>>
    val librosFavoritos: StateFlow<List<String>>
    val librosSugeridosCategorias:StateFlow<List<Libro>>
    val libroSelected:StateFlow<Libro?>
    val query: StateFlow<String>


    fun putLibroSelected(libro: Libro)
    fun fetchLibros()
    fun filtrarLibros(query: String)
    fun loadFavoritos()
    fun updateFavoritos(add: Boolean, libroId: String)

    fun addLibroFavorito(idLibro: String)
    fun removeLibroFavorito(idLibro: String)

    fun getLibrosByCategorias(categoria:String)

}
