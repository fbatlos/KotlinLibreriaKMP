package org.example.projects.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.actapp.componentes_login.ErrorDialog
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.*
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.LibrosMostrar.TarjetaLibro
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel
import org.example.projects.ViewModel.LibrosViewModel


@Composable
fun BarraDeBusqueda(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar libros...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun LibrosScreen(
    navigator: Navegator,
    uiStateViewModel: UiStateViewModel,
    librosViewModel: LibrosViewModel
) {
    val isLoading by uiStateViewModel.isLoading.collectAsState()
    val textError by uiStateViewModel.textError.collectAsState()
    val showDialog by uiStateViewModel.showDialog.collectAsState()

    val allLibros by librosViewModel.libros.collectAsState() // Todos los libros
    val query by librosViewModel.query.collectAsState()
    val scope = CoroutineScope(Dispatchers.IO)

    librosViewModel.loadFavoritos()
    librosViewModel.fetchLibros()

    val filteredLibros by remember {
        derivedStateOf {
            if (query.isEmpty()) {
                allLibros
            } else {
                allLibros.filter { libro ->
                    libro.titulo!!.contains(query, ignoreCase = true) ||
                            libro.autores.any { it.contains(query, ignoreCase = true) } ||
                            libro.categorias.any { it.contains(query, ignoreCase = true) }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BarraDeBusqueda(
            onSearch = { query ->
                librosViewModel.filtrarLibros(query)
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                scope.async(Dispatchers.IO) {
                    delay(300)
                    uiStateViewModel.setLoading(false)
                }
                LibrosGrid(
                    libros = filteredLibros,
                    librosViewModel = librosViewModel,
                    uiStateViewModel = uiStateViewModel,
                    showDialog = showDialog,
                    textError = if (filteredLibros.isEmpty() && query.isNotEmpty()) "No se encontraron resultados" else textError,
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun LibrosGrid(libros: List<Libro>, librosViewModel: LibrosViewModel,uiStateViewModel: UiStateViewModel, showDialog: Boolean,textError: String, navigator: Navegator) {

    val librosFavoritos by librosViewModel.librosFavoritos.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (libros.isNullOrEmpty()) {
            Text(
                text = textError,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(libros) { libro ->
                    TarjetaLibro(
                        libro = libro,
                        onFavoritoClick = { add -> cambiarListaFavoritos(add, librosViewModel, libro._id) },
                        librosFavoritos = librosFavoritos,
                        navigator = navigator
                    )
                }
            }
        }
        uiStateViewModel.setLoading(false)

        ErrorDialog(showDialog = showDialog, textError = textError){
            uiStateViewModel.setShowDialog(it)
        }
    }
}


fun cambiarListaFavoritos(add: Boolean, viewModel: LibrosViewModel, idLibro: String) {
    if (add) {
        viewModel.addLibroFavorito(idLibro)
    } else {
        viewModel.removeLibroFavorito(idLibro)
    }

    viewModel.updateFavoritos(add, idLibro)
}