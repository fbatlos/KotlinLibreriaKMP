package org.example.projects

import android.os.Bundle
import android.util.Log
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
import org.example.projects.NavController.Navigator
import org.example.projects.Screens.LibroDetailScreen
import org.example.projects.Screens.LoginScreen
import org.example.projects.Utils.LibroSerializer.toLibro
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val navigator = remember { Navigator(navController) }
                val uiStateViewModel = remember { UiStateViewModel() }
                val sharedViewModel = remember { SharedViewModel() }
                val authViewModel = remember { AuthViewModel(uiStateViewModel, sharedViewModel) }
                val libroViewModel = remember { LibrosViewModel(uiStateViewModel, sharedViewModel) }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = AppRoutes.LibroLista.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // Pantalla de Login
                            composable(AppRoutes.Login.route) {
                                LoginScreen(
                                    modifier = Modifier,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    uiStateViewModel = uiStateViewModel
                                )
                            }

                            // Pantalla de Lista de Libros
                            composable(AppRoutes.LibroLista.route) {
                                LibrosScreen(
                                    navController = navigator,
                                    uiStateViewModel = uiStateViewModel,
                                    authViewModel = authViewModel,
                                    librosViewModel = libroViewModel
                                )
                            }

                            // Pantalla de Detalle de Libro (No se si ahora furula)
                            composable(
                                route = "libroDetail?libroJson={libroJson}",
                                arguments = listOf(
                                    navArgument("libroJson") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                                )
                            ) { backStackEntry ->
                                val libroJson = backStackEntry.arguments?.getString("libroJson") ?: ""
                                val libro = libroJson.toLibro()

                                LibroDetailScreen(
                                    libro = libro,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    librosViewModel = libroViewModel
                                )
                            }


                        }
                    }
                )
            }
        }
    }
}