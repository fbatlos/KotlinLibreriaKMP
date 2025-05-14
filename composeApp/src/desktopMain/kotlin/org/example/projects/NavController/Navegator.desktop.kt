package org.example.projects.NavController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import okhttp3.internal.wait
import org.example.projects.NavController.Navigator

class Navigator: Navegator{
    private var backStack by mutableStateOf<AppRoutes>(AppRoutes.LibroLista)
    private var currentScreen by mutableStateOf<AppRoutes>(AppRoutes.LibroLista)

    override fun navigateTo(route: AppRoutes) {
        backStack = currentScreen
        currentScreen = route
    }

    override fun popBackStack() {
        currentScreen = backStack
    }

    override fun getCurrentRoute(): AppRoutes = currentScreen
}