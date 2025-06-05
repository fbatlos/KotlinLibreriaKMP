package org.example.projects

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navigator
import org.example.projects.Screens.CarritoScreen
import org.example.projects.Screens.InicioScreen
import org.example.projects.Screens.LibroDetailScreen
import org.example.projects.Screens.LibrosScreen
import org.example.projects.Screens.LoginScreen
import org.example.projects.Screens.MiPerfilScreen
import org.example.projects.Screens.MisValoracionesScreen
import org.example.projects.Screens.RegisterScreen
import org.example.projects.Screens.TicketCompraScreen
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.InicioViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

fun main() = application {
    val navigator = remember { Navigator() }
    val uiStateViewModel = remember { UiStateViewModel() }
    val sharedViewModel = remember { SharedViewModel() }
    val authViewModel = remember { AuthViewModel(uiStateViewModel, sharedViewModel) }
    val libroViewModel = remember { LibrosViewModel(uiStateViewModel, sharedViewModel) }
    val carritoViewModel = remember { CarritoViewModel(uiStateViewModel,authViewModel,sharedViewModel) }
    val inicioViewModel = remember { InicioViewModel() }

    Window(onCloseRequest = ::exitApplication, title = "LeafRead" , icon = BitmapPainter(useResource("logo_libreria.png", ::loadImageBitmap))) {
        MaterialTheme {
            when (val currentScreen = navigator.getCurrentRoute()) {
                is AppRoutes.Login -> LoginScreen(
                    modifier = Modifier,
                    navController = navigator,
                    authViewModel = authViewModel,
                    uiStateViewModel = uiStateViewModel,
                    carritoViewModel = carritoViewModel,
                    sharedViewModel = sharedViewModel
                )
                is AppRoutes.LibroLista -> LibrosScreen(
                    navController = navigator,
                    uiStateViewModel = uiStateViewModel,
                    authViewModel = authViewModel,
                    librosViewModel = libroViewModel,
                    carritoViewModel = carritoViewModel,
                    sharedViewModel = sharedViewModel
                )
                is AppRoutes.LibroDetalles -> {
                    LibroDetailScreen(
                        navController = navigator,
                        authViewModel = authViewModel,
                        librosViewModel = libroViewModel,
                        carritoViewModel = carritoViewModel,
                        sharedViewModel = sharedViewModel,
                        uiStateViewModel = uiStateViewModel
                    )
                }
                is AppRoutes.Registro -> {
                    RegisterScreen(
                        modifier = Modifier,
                        carritoViewModel = carritoViewModel,
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navigator,
                        uiStateViewModel = uiStateViewModel
                    )
                }

                is AppRoutes.Carrito -> {
                    CarritoScreen(
                        navController = navigator,
                        uiViewModel = uiStateViewModel,
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel,
                        librosViewModel = libroViewModel,
                        carritoViewModel = carritoViewModel
                    )
                }

                is AppRoutes.HistorialCompra -> {
                    TicketCompraScreen(
                        navController = navigator,
                        carritoViewModel = carritoViewModel,
                        authViewModel = authViewModel,
                        uiViewModel = uiStateViewModel,
                        sharedViewModel = sharedViewModel,
                        librosViewModel = libroViewModel
                    )
                }

                AppRoutes.Inicio -> {
                    InicioScreen(
                        carritoViewModel = carritoViewModel,
                        authViewModel = authViewModel,
                        uiViewModel = uiStateViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navigator,
                        librosViewModel = libroViewModel,
                        inicioViewModel = inicioViewModel
                    )
                }
                AppRoutes.MiPerfil -> {
                    MiPerfilScreen(
                        carritoViewModel = carritoViewModel,
                        authViewModel = authViewModel,
                        uiViewModel = uiStateViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navigator,
                        librosViewModel = libroViewModel
                    )
                }
                AppRoutes.MisValoraciones -> {
                    MisValoracionesScreen(
                        carritoViewModel = carritoViewModel,
                        authViewModel = authViewModel,
                        uiViewModel = uiStateViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navigator,
                        librosViewModel = libroViewModel
                    )
                }
            }
        }
    }
}