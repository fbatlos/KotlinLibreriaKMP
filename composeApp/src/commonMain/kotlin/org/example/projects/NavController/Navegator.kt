package org.example.projects.NavController

//interfaz para que todos tengan que realizar sus funciones según su forma
interface Navegator {
    fun navigateTo(route: AppRoutes)
    fun popBackStack()
    fun getCurrentRoute(): AppRoutes
}