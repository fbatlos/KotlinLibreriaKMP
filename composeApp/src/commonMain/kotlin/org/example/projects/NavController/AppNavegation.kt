package org.example.projects.NavController

interface AppNavigator {
    fun navigateTo(route: String)
    fun goBack()
}