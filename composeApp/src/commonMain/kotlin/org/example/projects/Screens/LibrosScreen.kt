package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.*
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.LibrosMostrar.TarjetaLibro
import org.example.projects.ViewModel.UiStateViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.AuthViewModel



@Composable
fun LibrosScreen(
    navController: Navegator,
    uiStateViewModel: UiStateViewModel,
    authViewModel: AuthViewModel,
    librosViewModel: LibrosViewModel
) {
    val isLoading by uiStateViewModel.isLoading.collectAsState()
    val textError by uiStateViewModel.textError.collectAsState()
    val showDialog by uiStateViewModel.showDialog.collectAsState()

    val allLibros by librosViewModel.libros.collectAsState() // Todos los libros
    val query by librosViewModel.query.collectAsState()

    val scopeSec = CoroutineScope(Dispatchers.IO)

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


    LayoutPrincipal(
        headerContent = { drawerState,scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { query -> librosViewModel.filtrarLibros(query) },
                onSearchClick = {},
                onCartClick = {},
                navController = navController,
                authViewModel = authViewModel
            )
        },
        drawerContent = {drawerState->
            MenuBurger(drawerState,navController)
        }
    ) {paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    librosViewModel.loadFavoritos()
                    librosViewModel.fetchLibros()

                    if (isLoading || allLibros.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        scopeSec.async(Dispatchers.IO) {
                            delay(300)
                            uiStateViewModel.setLoading(false)
                        }
                        LibrosGrid(
                            libros = filteredLibros,
                            librosViewModel = librosViewModel,
                            uiStateViewModel = uiStateViewModel,
                            showDialog = showDialog,
                            textError = if (filteredLibros.isEmpty() && query.isNotEmpty()) "No se encontraron resultados" else textError,
                            navigator = navController
                        )
                    }
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
                color = AppColors.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(libros) { libro ->
                    TarjetaLibro(
                        libro = libro,
                        onFavoritoClick = { add -> cambiarListaFavoritos(add, librosViewModel, libro._id) },
                        librosFavoritos = librosFavoritos,
                        navController = navigator
                    )
                }
            }
        }
        uiStateViewModel.setLoading(false)
        if (showDialog) {
            ErrorDialog(textError = textError) {
                uiStateViewModel.setShowDialog(it)
            }
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