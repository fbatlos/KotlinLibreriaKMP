package org.example.projects.NavController

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager : AppNavigator {
    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    override fun navigateTo(route: String) {
        _navigationEvents.tryEmit(route)
    }

    override fun goBack() {
        _navigationEvents.tryEmit("back")
    }
}