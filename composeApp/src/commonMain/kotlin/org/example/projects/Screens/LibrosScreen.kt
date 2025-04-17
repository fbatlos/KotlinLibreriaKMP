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
import org.example.projects.NavController.AppNavigator
import org.example.projects.Screens.LibrosMostrar.TarjetaLibro
import org.example.projects.ViewModel.SharedViewModel



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
    navigator: AppNavigator,
    viewModel: SharedViewModel
) {
    val token = viewModel.token.value ?: ""
    val isLoading by viewModel.isLoading.collectAsState()
    val textError by viewModel.textError.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val allLibros by viewModel.libros.collectAsState() // Todos los libros
    val query by viewModel.query.collectAsState()
    val scope = CoroutineScope(Dispatchers.IO)


    val isFiltering = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onIsLoading(true)
        try {
            viewModel.loadFavoritos(token)
            viewModel.getLibros(API.apiService.listarLibros(token))
        } catch (e: Exception) {
            viewModel.textErrorChange("Error al cargar libros: ${e.message}")
        } finally {
            viewModel.onIsLoading(false)
        }
    }

    val filteredLibros = remember(allLibros, query) {
        isFiltering.value = true
        val result = if (query.isEmpty()) {
            allLibros
        } else {
            allLibros.filter { libro ->
                libro.titulo!!.contains(query, ignoreCase = true) ||
                        libro.autores.any { it.contains(query, ignoreCase = true) } ||
                        libro.categorias.any { it.contains(query, ignoreCase = true) }
            }
        }
        result
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BarraDeBusqueda(
            onSearch = { query ->
                viewModel.filtrarLibros(query)
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
                    viewModel.onIsLoading(false)
                }
                LibrosGrid(
                    libros = filteredLibros,
                    viewModel = viewModel,
                    showDialog = showDialog,
                    textError = if (filteredLibros.isEmpty() && query.isNotEmpty()) "No se encontraron resultados" else textError
                )
            }
        }
    }
}

@Composable
fun LibrosGrid(libros: List<Libro>, viewModel: SharedViewModel, showDialog: Boolean,textError: String) {

    val librosFavoritos by viewModel.librosFavoritos.collectAsState()

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
                        onFavoritoClick = { add -> cambiarListaFavoritos(add, viewModel, libro._id) },
                        token = viewModel.token.value,
                        librosFavoritos = librosFavoritos
                    )
                }
            }
        }
        viewModel.onIsLoading(false)

        ErrorDialog(showDialog = showDialog, textError = textError){
            viewModel.onShowDialog(it)
        }
    }
}


fun cambiarListaFavoritos(add: Boolean, viewModel: SharedViewModel, idLibro: String) {
    val scope = CoroutineScope(Dispatchers.IO)
    scope.async {
        if (add) {
            API.apiService.addLibroFavorito(viewModel.token.value!!, idLibro)
        } else {
            API.apiService.removeLibroFavorito(viewModel.token.value!!, idLibro)
        }

        viewModel.updateFavoritos(add, idLibro)
    }
}