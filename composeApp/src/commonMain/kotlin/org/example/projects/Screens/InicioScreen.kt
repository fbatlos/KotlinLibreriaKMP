package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.*

@Composable
fun InicioScreen(
    librosViewModel: LibrosViewModel,
    navController: Navegator,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    uiViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel,
    inicioViewModel: InicioViewModel
) {
    val libros by librosViewModel.libros.collectAsState()
    val librosFavoritos by librosViewModel.librosFavoritos.collectAsState()


    val usuarioLogueado = sharedViewModel.token.value != null
    val isLoading by uiViewModel.isLoading.collectAsState()

    val recomendaciones by inicioViewModel.recomendaciones.collectAsState()
    val librosCategoriaAleatoria by inicioViewModel.librosCategoria.collectAsState()
    val categoriaName by inicioViewModel.categoriaSeleccionada.collectAsState()
    val librosMejorValorados by inicioViewModel.librosMejorValorados.collectAsState()


    LaunchedEffect(Unit) {
        librosViewModel.loadFavoritos()
        librosViewModel.fetchLibros()
    }

    LaunchedEffect(libros) {
        if (libros.isNotEmpty()) {
            inicioViewModel.getRecomendaciones(libros, librosFavoritos, usuarioLogueado)
            inicioViewModel.getLibrosCategorias(libros)
            inicioViewModel.getLibrosMejorValorados(libros)
        }
    }


    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = {},
                onSearchClick = {},
                authViewModel = authViewModel,
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(drawerState, navController, uiViewModel, sharedViewModel)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.white)
                .padding(16.dp)
        ) {
            // Sección de bienvenida o inicio sesión
            item {
                if (!usuarioLogueado) {
                    Card(
                        backgroundColor = AppColors.primary,
                        shape = RoundedCornerShape(12.dp),
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "¡Bienvenido!",
                                style = MaterialTheme.typography.h6,
                                color = AppColors.white
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Inicia sesión para disfrutar de recomendaciones personalizadas y más.",
                                style = MaterialTheme.typography.body2,
                                color = AppColors.white
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigateTo(AppRoutes.Login) },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = AppColors.warning
                                )
                            ) {
                                Text("Iniciar Sesión", color = AppColors.primary)
                            }
                        }
                    }
                }
            }

            // Título recomendaciones
            item {
                Text(
                    text = "Recomendaciones para ti",
                    style = MaterialTheme.typography.h6,
                    color = AppColors.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            // Libros recomendados
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {
                    println(isLoading)
                    println(recomendaciones.isNullOrEmpty())
                    if (isLoading || recomendaciones.isNullOrEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp).align(Alignment.Center),
                            color = AppColors.primary,
                            strokeWidth = 4.dp
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(recomendaciones!!) { libro ->
                                LibroSugeridoItem(
                                    libro = libro,
                                    librosViewModel = librosViewModel,
                                    onClick = {
                                        librosViewModel.putLibroSelected(libro)
                                        librosViewModel.fetchValoraciones(libro._id!!)
                                        navController.navigateTo(AppRoutes.LibroDetalles)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Destacados
            item {
                Text(
                    text = "Destacados de ${categoriaName ?: ""} ",
                    style = MaterialTheme.typography.h6,
                    color = AppColors.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Libros recomendados de categorias
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {
                    if (isLoading || librosCategoriaAleatoria.isNullOrEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp).align(Alignment.Center),
                            color = AppColors.primary,
                            strokeWidth = 4.dp
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(librosCategoriaAleatoria!!) { libro ->
                                LibroSugeridoItem(
                                    libro = libro,
                                    librosViewModel = librosViewModel,
                                    onClick = {
                                        librosViewModel.putLibroSelected(libro)
                                        librosViewModel.fetchValoraciones(libro._id!!)
                                        navController.navigateTo(AppRoutes.LibroDetalles)
                                    }
                                )
                            }
                        }
                    }
                }
            }


            // Destacados
            item {
                Text(
                    text = "Libros mejor valorados ",
                    style = MaterialTheme.typography.h6,
                    color = AppColors.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Libros recomendados de categorias
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                ) {
                    println(isLoading)
                    println(librosMejorValorados.isNullOrEmpty())
                    if (isLoading || librosMejorValorados.isNullOrEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp).align(Alignment.Center),
                            color = AppColors.primary,
                            strokeWidth = 4.dp
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(librosMejorValorados!!) { libro ->
                                LibroSugeridoItem(
                                    libro = libro,
                                    librosViewModel = librosViewModel,
                                    onClick = {
                                        librosViewModel.putLibroSelected(libro)
                                        librosViewModel.fetchValoraciones(libro._id!!)
                                        navController.navigateTo(AppRoutes.LibroDetalles)
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}