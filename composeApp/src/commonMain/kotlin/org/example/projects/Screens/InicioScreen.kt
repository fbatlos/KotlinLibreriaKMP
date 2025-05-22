package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Valoracion
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
    sharedViewModel: SharedViewModel
) {
    librosViewModel.loadFavoritos()
    librosViewModel.fetchLibros()

    val libros by librosViewModel.libros.collectAsState()
    val librosFavoritos by librosViewModel.librosFavoritos.collectAsState()
    val usuarioLogueado = sharedViewModel.token.value != null

    val recomendaciones = if (usuarioLogueado && librosFavoritos.isNotEmpty()) {
        libros.filter { it._id in librosFavoritos }
    } else {
        libros.shuffled().take(5)
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
            MenuBurger(drawerState, navController)
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recomendaciones) { libro ->
                        LibroSugeridoItem(
                            libro = libro,
                            onClick = {
                                librosViewModel.putLibroSelected(libro)
                                navController.navigateTo(AppRoutes.LibroDetalles)
                            }
                        )
                    }
                }
            }

            // Eventos destacados
            item {
                Text(
                    text = "Eventos destacados",
                    style = MaterialTheme.typography.h6,
                    color = AppColors.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Si en el futuro quieres meter aquí los eventos:
            // items(eventos) { evento ->
            //     EventoCard(evento)
            // }
        }
    }
}


@Composable
fun LibroCard(libro: Libro) {

    /*val valoracionesLibro = valoraciones.filter { it.libroid == libro._id }
    val puntuacionPromedio = if (valoracionesLibro.isNotEmpty()) {
        valoracionesLibro.map { it.valoracion }.average()
    } else {
        0.0
    }

     */

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(end = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = libro.titulo!!, style = MaterialTheme.typography.subtitle1)
            Text(text = "Autor: ${libro.autores}", style = MaterialTheme.typography.body2)
            Text(text = "Categoría: ${libro.categorias}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            /*
            Text(
                text = "Puntuación: ${"%.1f".format(puntuacionPromedio)} ⭐",
                style = MaterialTheme.typography.body2,
                color = AppColors.grey
            )*/
        }
    }
}
