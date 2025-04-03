package org.example.projects

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.projects.Navegation.DesktopNavigator
import org.example.projects.Screens.DetailScreen
import org.example.projects.Screens.HomeScreen

// Main.kt (desktopMain)
fun main() = application {
    val navigator = remember { DesktopNavigator() }

    Window(onCloseRequest = ::exitApplication, title = "KMP Navigation") {
        when (navigator.currentScreen) {
            "home" -> HomeScreen(navigator)
            "detail" -> DetailScreen(navigator)
        }
    }
}