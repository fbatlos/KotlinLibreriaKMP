package org.example.projects.NavController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.projects.NavController.Navigator

class Navigator: Navegator{
    private val backStack = mutableStateListOf<AppRoutes>(AppRoutes.LibroLista)
    private var currentScreen by mutableStateOf<AppRoutes>(AppRoutes.LibroLista)

    override fun navigateTo(route: AppRoutes) {
        currentScreen = route
    }

    override fun popBackStack() {
        currentScreen = when (currentScreen) {
            is AppRoutes.LibroDetail -> AppRoutes.LibroLista
            else -> AppRoutes.Login
        }
    }

    override fun getCurrentRoute(): AppRoutes = currentScreen
}