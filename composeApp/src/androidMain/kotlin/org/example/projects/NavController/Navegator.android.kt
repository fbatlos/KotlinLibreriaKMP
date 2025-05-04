package org.example.projects.NavController

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Navigator(
    private val navController: NavHostController
) : Navegator{


    override fun navigateTo(route: AppRoutes) {
        when (route) {
            is AppRoutes.Login -> navController.navigate(route.route)
            is AppRoutes.LibroLista -> navController.navigate(route.route)
            is AppRoutes.LibroDetail -> {
                navController.navigate(route.createRoute())
            }
            is AppRoutes.Registro -> {}
            is AppRoutes.Cesta -> {}
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun getCurrentRoute(): AppRoutes {
        return navController.currentDestination?.route?.let { AppRoutes.fromString(it) } ?: AppRoutes.Login
    }
}