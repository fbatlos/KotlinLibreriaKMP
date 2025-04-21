package org.example.projects.NavController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.projects.NavController.Navigator

class Navigator: Navegator{
    private var currentScreen by mutableStateOf<AppRoutes>(AppRoutes.Login)

    override fun navigateTo(route: AppRoutes) {
        currentScreen = route
    }

    override fun popBackStack() {
        // Implementación simple para desktop
        currentScreen = when (currentScreen) {
            is AppRoutes.LibroLista -> AppRoutes.LibroLista
            else -> AppRoutes.Login
        }
    }

    override fun getCurrentRoute(): AppRoutes = currentScreen
}