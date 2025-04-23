package org.example.projects

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navigator
import org.example.projects.Screens.LibroDetailScreen
import org.example.projects.Screens.LibrosScreen
import org.example.projects.Screens.LoginScreen
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel


fun main() = application {
    val navigator = remember { Navigator() }
    val uiStateViewModel = remember { UiStateViewModel() }
    val sharedViewModel = remember { SharedViewModel() }
    val authViewModel = remember { AuthViewModel(uiStateViewModel, sharedViewModel) }
    val libroViewModel = remember { LibrosViewModel(uiStateViewModel, sharedViewModel) }

    Window(onCloseRequest = ::exitApplication, title = "KMP Libreria") {
        MaterialTheme {
            when (val currentScreen = navigator.getCurrentRoute()) {
                is AppRoutes.Login -> LoginScreen(
                    modifier = Modifier,
                    navController = navigator,
                    authViewModel = authViewModel,
                    uiStateViewModel = uiStateViewModel
                )
                is AppRoutes.LibroLista -> LibrosScreen(
                    navController = navigator,
                    uiStateViewModel = uiStateViewModel,
                    librosViewModel = libroViewModel
                )
                is AppRoutes.LibroDetail -> {
                    LibroDetailScreen(
                        libro = currentScreen.libro,
                       navController = navigator
                    )
                }
                is AppRoutes.Registro -> {}
            }
        }
    }
}