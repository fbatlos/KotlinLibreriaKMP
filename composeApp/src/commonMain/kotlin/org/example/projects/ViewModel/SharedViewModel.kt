package org.example.projects.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.Utils.Utils
import javax.management.Query

class SharedViewModel : dev.icerock.moko.mvvm.viewmodel.ViewModel(){

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _isAdmin = MutableStateFlow<Boolean>(false)
    val isAdmin = _isAdmin

    fun setToken(token: String) {
        _token.value = token
    }

    fun comprobarAdmin(){
        _isAdmin.value = Utils.isUserAdmin(_token.value!!)
        println("Admin ? = ${_isAdmin.value}")
    }

    fun limpiar(){
        _token.value = null
    }
}