package org.example.projects.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.TipoStock
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.AppRoutes.LibroLista.route
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.LibrosViewModel

@Composable
expect fun ImagenLibroDetails(url: String?, contentDescription: String?,modifier: Modifier )

@Composable
fun LibroDetailScreen(libro: Libro, navController: Navegator, authViewModel:AuthViewModel, librosViewModel:LibrosViewModel){

    val librosSugeridos by librosViewModel.librosSugeridosCategorias.collectAsState()

    LaunchedEffect(libro.categorias) {
        libro.categorias.firstOrNull()?.let { categoria ->
            println(categoria.replace("³","").replace("+"," "))
            librosViewModel.getLibrosByCategorias(categoria.replace("³","").replace("+"," "))
        }
    }

    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = {},
                onSearchClick = {},
                onCartClick = { },
                authViewModel = authViewModel,
                navController = navController
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(drawerState, navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            // Sección de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(AppColors.greyBlue.copy(alpha = 0.1f))
            ) {
                ImagenLibroDetails(
                    url = libro.imagen,
                    contentDescription = libro.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .align(Alignment.TopCenter)
                )

                // Estado del libro (Nuevo, Popular, etc.)
                libro.stock.tipo?.let { estado ->
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(
                                color = when (estado) {
                                    TipoStock.EN_STOCK -> AppColors.success.copy(alpha = 0.9f)
                                    TipoStock.PREVENTA -> AppColors.warning.copy(alpha = 0.9f)
                                    TipoStock.AGOTADO -> AppColors.error.copy(alpha = 0.9f)
                                    else -> AppColors.primary.copy(alpha = 0.9f)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = estado.toString(),
                            color = AppColors.white,
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Contenido principal
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Título y precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = libro.titulo?.replace("+", " ") ?: "Sin título",
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.black,
                        modifier = Modifier.weight(0.7f)
                    )

                    Text(
                        text = "${libro.precio?.toString() ?: "N/A"} ${libro.moneda ?: ""}",
                        style = MaterialTheme.typography.h5,
                        color = AppColors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Autores
                libro.autores.takeIf { it.isNotEmpty() }?.let { autores ->
                    Text(
                        text = autores.joinToString(", ").replace("+", " "),
                        style = MaterialTheme.typography.subtitle1,
                        color = AppColors.black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }


                // Botón de añadir a la cesta
                Button(
                    onClick = {
                        authViewModel.addLibrocesta(libro)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppColors.white
                    ),
                    enabled = if (libro.stock.tipo == TipoStock.AGOTADO){false}else{true}
                ) {
                    Text(
                        "Añadir a la cesta",
                        modifier = Modifier.padding(4.dp)
                    )
                }

                // Descripción
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = libro.descripcion?.replace("+", " ") ?: "No hay descripción disponible",
                    style = MaterialTheme.typography.body1,
                    color = AppColors.black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Categorías relacionadas
                libro.categorias.takeIf { it.isNotEmpty() }?.let { categorias ->
                    Text(
                        text = "Te interesan estas categorías...",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Grid de categorías
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier.height(150.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        println(librosSugeridos)
                        items(librosSugeridos) { libro ->
                            LibroSugeridoItem(
                                libro = libro,
                                onClick = {
                                    navController.navigateTo(AppRoutes.LibroDetail(libro))
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

// Componente para libros relacionados
@Composable
fun LibroSugeridoItem(libro: Libro, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagenLibroDetails(
            url = libro.imagen,
            contentDescription = libro.titulo,
            modifier = Modifier
                .size(120.dp, 160.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = libro.titulo?.replace("+", " ") ?: "",
            style = MaterialTheme.typography.caption,
            color = AppColors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${libro.precio?.toString() ?: ""} ${libro.moneda ?: ""}",
            style = MaterialTheme.typography.caption,
            color = AppColors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}