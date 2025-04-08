package org.example.projects

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.projects.Navegation.DesktopNavigator
import org.example.projects.Screens.DetailScreen
import org.example.projects.Screens.HomeScreen
import org.example.projects.Screens.Login
import org.example.projects.ViewModel.SharedViewModel


// Main.kt (desktopMain)
fun main() = application {
    val navigator = remember { DesktopNavigator() }
    val viewModel = remember { SharedViewModel() }

    Window(onCloseRequest = ::exitApplication, title = "KMP Libreria") {
        when (navigator.currentScreen) {
            "home" -> HomeScreen(navigator)
            "detail" -> DetailScreen(navigator)
            "login" -> Login(modifier = Modifier, navController = navigator, viewModel = viewModel)
        }
    }
}