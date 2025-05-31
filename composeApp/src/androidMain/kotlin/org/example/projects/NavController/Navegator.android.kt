package org.example.projects.NavController

import androidx.navigation.NavHostController

class Navigator(
    private val navController: NavHostController
) : Navegator{


    override fun navigateTo(route: AppRoutes) {
        when (route) {
            AppRoutes.Login -> navController.navigate(route.route)
            AppRoutes.LibroLista -> navController.navigate(route.route)
            AppRoutes.LibroDetalles -> navController.navigate(route.route)
            AppRoutes.Registro -> {navController.navigate(route.route)}
            AppRoutes.Carrito -> {navController.navigate(route.route)}
            AppRoutes.Inicio ->{navController.navigate(route.route)}
            AppRoutes.HistorialCompra -> {navController.navigate(route.route)}
            AppRoutes.MiPerfil -> {navController.navigate(route.route)}
            AppRoutes.MisValoraciones -> {navController.navigate(route.route)}
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun getCurrentRoute(): AppRoutes {
        return navController.currentDestination?.route?.let { AppRoutes.fromString(it) } ?: AppRoutes.Login
    }
}