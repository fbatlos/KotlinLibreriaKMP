package org.example.projects

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.projects.Screens.LibrosScreen
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.NavigationHandleDeepLink
import org.example.projects.NavController.Navigator
import org.example.projects.Screens.CarritoScreen
import org.example.projects.Screens.InicioScreen
import org.example.projects.Screens.LibroDetailScreen
import org.example.projects.Screens.LoginScreen
import org.example.projects.Screens.RegisterScreen
import org.example.projects.Screens.TicketCompraScreen
import org.example.projects.Utils.LibroSerializer.toLibro
import org.example.projects.ViewModel.AppContextProvider
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContextProvider.context = applicationContext

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val navigator = remember { Navigator(navController) }
                NavigationHandleDeepLink.navigator = navigator

                val uiStateViewModel = remember { UiStateViewModel() }
                val sharedViewModel = remember { SharedViewModel() }
                val authViewModel = remember { AuthViewModel(uiStateViewModel, sharedViewModel) }
                val libroViewModel = remember { LibrosViewModel(uiStateViewModel, sharedViewModel) }
                val carritoViewModel = remember { CarritoViewModel(uiStateViewModel, sharedViewModel = sharedViewModel, authViewModel = authViewModel) }

                NavigationHandleDeepLink.carritoViewModel = carritoViewModel

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = AppRoutes.Inicio.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // Pantalla de Login
                            composable(AppRoutes.Login.route) {
                                LoginScreen(
                                    modifier = Modifier,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    uiStateViewModel = uiStateViewModel,
                                    carritoViewModel = carritoViewModel
                                )
                            }

                            // Pantalla de Lista de Libros
                            composable(AppRoutes.LibroLista.route) {
                                LibrosScreen(
                                    navController = navigator,
                                    uiStateViewModel = uiStateViewModel,
                                    authViewModel = authViewModel,
                                    librosViewModel = libroViewModel,
                                    carritoViewModel = carritoViewModel
                                )
                            }

                            // Pantalla de Detalle de Libro (No se si ahora furula)
                            composable(
                                route = AppRoutes.LibroDetalles.route,
                            ) {
                                LibroDetailScreen(
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    librosViewModel = libroViewModel,
                                    carritoViewModel = carritoViewModel,
                                    sharedViewModel = sharedViewModel,
                                    uiStateViewModel = uiStateViewModel
                                )
                            }

                            composable(AppRoutes.Carrito.route){
                                CarritoScreen(
                                    navController = navigator,
                                    uiStateViewModel = uiStateViewModel,
                                    authViewModel = authViewModel,
                                    sharedViewModel = sharedViewModel,
                                    librosViewModel = libroViewModel,
                                    carritoViewModel = carritoViewModel
                                )
                            }

                            composable(
                                route = AppRoutes.HistorialCompra.route
                            ){
                                TicketCompraScreen(
                                    navController = navigator,
                                    carritoViewModel = carritoViewModel,
                                    authViewModel = authViewModel,
                                    uiViewModel = uiStateViewModel,
                                    sharedViewModel = sharedViewModel
                                )
                            }

                            composable(
                                route = AppRoutes.Registro.route
                            ){
                                RegisterScreen(
                                    modifier = Modifier,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    uiStateViewModel = uiStateViewModel,
                                    carritoViewModel = carritoViewModel
                                )
                            }

                            composable(
                                route = AppRoutes.Inicio.route
                            ){
                                InicioScreen(
                                    librosViewModel = libroViewModel,
                                    sharedViewModel = sharedViewModel,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    carritoViewModel = carritoViewModel
                                )
                            }

                        }
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent:Intent) {
        super.onNewIntent(intent)
        AppContextProvider.handleDeepLink(intent)
    }

}