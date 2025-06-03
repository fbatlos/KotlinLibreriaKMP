package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro

class InicioViewModel (
) : ViewModel() {
    private val _recomendaciones = MutableStateFlow<List<Libro>?>(emptyList())
    val recomendaciones: StateFlow<List<Libro>?> = _recomendaciones

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada = _categoriaSeleccionada

    private val _librosCategoria = MutableStateFlow<List<Libro>?>(null)
    val librosCategoria = _librosCategoria

    private val _librosMejorValorados =  MutableStateFlow<List<Libro>?>(null)
    val librosMejorValorados = _librosMejorValorados

    fun getRecomendaciones(libros:List<Libro>,librosFavoritos:List<String>,usuarioLogueado:Boolean){
        if (usuarioLogueado && librosFavoritos.isNotEmpty()) {
            println(librosFavoritos)
            val idLibroFavorito = librosFavoritos.shuffled().get(0)
            val librosEnFavorito = libros.filter { it._id == idLibroFavorito }
            println(librosEnFavorito)
            val categoria = librosEnFavorito.flatMap { it.categorias }.distinct().shuffled()[0]
            _recomendaciones.value = libros.filter { categoria in it.categorias }.take(8)


        } else {
            _recomendaciones.value = libros.shuffled().take(8)
        }
    }

    fun getLibrosCategorias(libros:List<Libro>){
        if (libros.isNotEmpty()) {
            _categoriaSeleccionada.value = libros.flatMap { it.categorias }.distinct().shuffled()[0]
            _librosCategoria.value =
                libros.filter { _categoriaSeleccionada.value!! in it.categorias }.take(8)
        }
    }

    fun getLibrosMejorValorados(libros: List<Libro>){
        if (libros.isNotEmpty()){
            _librosMejorValorados.value = libros.sortedByDescending { it.valoracionMedia }.take(8)
        }
    }

}