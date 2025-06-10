package org.example.projects.NavController

import androidx.navigation.NavHostController

class Navigator(
    private val navController: NavHostController
) : Navegator{


    override fun navigateTo(route: AppRoutes) {
        navController.navigate(route.route)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun getCurrentRoute(): AppRoutes {
        return navController.currentDestination?.route?.let { AppRoutes.fromString(it) } ?: AppRoutes.Login
    }
}