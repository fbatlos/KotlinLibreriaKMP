package org.example.projects.Navegation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.projects.NavController.AppNavigator

class DesktopNavigator : AppNavigator {
    var currentScreen by mutableStateOf("home")

    override fun navigateTo(route: String) {
        currentScreen = route
    }

    override fun goBack() {
        currentScreen = "home"
    }
}