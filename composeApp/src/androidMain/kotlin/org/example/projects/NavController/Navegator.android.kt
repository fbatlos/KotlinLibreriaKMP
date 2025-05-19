package org.example.projects.NavController

import androidx.navigation.NavHostController

class Navigator(
    private val navController: NavHostController
) : Navegator{


    override fun navigateTo(route: AppRoutes) {
        when (route) {
            is AppRoutes.Login -> navController.navigate(route.route)
            is AppRoutes.LibroLista -> navController.navigate(route.route)
            is AppRoutes.LibroDetalles -> navController.navigate(route.route)
            is AppRoutes.Registro -> {}
            is AppRoutes.Carrito -> {navController.navigate(route.route)}
            AppRoutes.HistorialCompra -> {navController.navigate(route.route)}
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun getCurrentRoute(): AppRoutes {
        return navController.currentDestination?.route?.let { AppRoutes.fromString(it) } ?: AppRoutes.Login
    }
}