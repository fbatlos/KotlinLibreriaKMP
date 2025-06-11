package org.example.projects.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Valoracion
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.LibrosMostrar.ImagenDesdeUrl
import org.example.projects.Utils.Utils
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

//Pantalla de valoraciones para que el usuario pueda verlas y eliminarlas
@Composable
fun MisValoracionesScreen(
    navController: Navegator,
    librosViewModel: LibrosViewModel,
    uiViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel
) {
    val misValoraciones by librosViewModel.misValoraciones.collectAsState()
    val libros by librosViewModel.libros.collectAsState()

    val isLoading by uiViewModel.isLoading.collectAsState()
    val showDialog by uiViewModel.showDialog.collectAsState()
    val textError by uiViewModel.textError.collectAsState()

    val token by sharedViewModel.token.collectAsState()

    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (!navigated) {
            librosViewModel.getMisValoraciones()
            navigated = true
        }
    }

    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { },
                onSearchClick = {},
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(drawerState, navController,uiViewModel, authViewModel, sharedViewModel)
        }
    ) { paddingValues ->
        when {
            isLoading || misValoraciones == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.primary)
                }
            }

            misValoraciones.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no has hecho ninguna reseña.",
                        style = MaterialTheme.typography.h6,
                        color = AppColors.primary
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(misValoraciones!!) { valoracion ->
                        ResenaCard(
                            valoracion = valoracion,
                            librosViewModel = librosViewModel,
                            navController = navController,
                            onDeleteClick = { valoracionAEliminar ->

                                librosViewModel.deleteMiValoracion(valoracionAEliminar._id!!)
                                librosViewModel.fetchLibros()
                            }
                        )
                    }

                }
            }
        }

        if (showDialog) {
            ErrorDialog(textError = textError) {
                uiViewModel.setShowDialog(it)
            }
        }
    }
}

//Card con todo lo necesario para las valoraciones
@Composable
fun ResenaCard(
    valoracion: Valoracion,
    librosViewModel: LibrosViewModel,
    navController: Navegator,
    onDeleteClick: (Valoracion) -> Unit
) {
    val libros by librosViewModel.libros.collectAsState()
    val libro = libros.find { it._id == valoracion.libroid }

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = AppColors.white,
        elevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            libro?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            librosViewModel.putLibroSelected(libro)
                            navController.navigateTo(AppRoutes.LibroDetalles)
                        }
                ) {
                    ImagenDesdeUrl(it, modifier = Modifier.height(60.dp).padding(end = 12.dp))
                    Text(
                        text = it.titulo ?: "Sin título",
                        style = MaterialTheme.typography.subtitle1,
                        color = AppColors.primary
                    )
                }
            } ?: run {
                Text(
                    text = "Libro no encontrado",
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColors.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fecha: ${Utils.formatearFecha(valoracion.fecha)}",
                style = MaterialTheme.typography.body2,
                color = AppColors.grey
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = AppColors.primary, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Valoración: ",
                    style = MaterialTheme.typography.body2,
                    color = AppColors.darkGrey
                )
                repeat(valoracion.valoracion) {
                    Text(text = "⭐️")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valoracion.comentario,
                style = MaterialTheme.typography.body1,
                color = AppColors.darkGrey
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón eliminar
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.material.IconButton(
                    onClick = { onDeleteClick(valoracion) }
                ) {
                    androidx.compose.material.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = AppColors.error
                    )
                }
            }
        }
    }
}
