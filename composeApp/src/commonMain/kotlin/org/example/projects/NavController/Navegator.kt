package org.example.projects.NavController

interface Navegator {
    fun navigateTo(route: AppRoutes)
    fun popBackStack()
    fun getCurrentRoute(): AppRoutes
}