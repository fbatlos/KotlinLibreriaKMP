package org.example.projects

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
import org.example.projects.NavController.Navigator
import org.example.projects.Screens.LibroDetailScreen
import org.example.projects.Screens.Login
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
                val navHostController = rememberNavController()
                val navigator = remember { Navigator(navHostController) }
                val uiStateViewModel = remember { UiStateViewModel() }
                val sharedViewModel = remember { SharedViewModel() }
                val authViewModel = remember { AuthViewModel(uiStateViewModel, sharedViewModel) }
                val libroViewModel = remember { LibrosViewModel(uiStateViewModel, sharedViewModel) }


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        NavHost(
                            navController = navHostController,
                            startDestination = AppRoutes.Login.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(AppRoutes.Login.route) {
                                Login(
                                    modifier = Modifier,
                                    navController = navigator,
                                    authViewModel = authViewModel,
                                    uiStateViewModel = uiStateViewModel,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                            composable(AppRoutes.LibroLista.route) {
                                LibrosScreen(
                                    navigator = navigator,
                                    uiStateViewModel = uiStateViewModel,
                                    librosViewModel = libroViewModel
                                )
                            }
                            composable(
                                route = AppRoutes.LibroDetail.route ,
                                arguments = listOf(
                                    navArgument("libroJson") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val libroJson = URLDecoder.decode(
                                    backStackEntry.arguments?.getString("libroJson") ?: "",
                                    "UTF-8"
                                )

                                val libro = runCatching {
                                    libroJson.toLibro()
                                }.getOrElse {
                                    // Manejar error, quizás navegar de vuelta
                                    null
                                }

                                if (libro != null) {
                                    LibroDetailScreen(
                                        libro = libro,
                                        onBackPressed = { navigator.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}