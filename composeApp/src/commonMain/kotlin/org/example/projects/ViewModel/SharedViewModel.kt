package org.example.projects.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro
import javax.management.Query

class SharedViewModel : dev.icerock.moko.mvvm.viewmodel.ViewModel(){

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    fun setToken(token: String) {
        _token.value = token
    }

    fun limpiar(){
        _token.value = null
    }
}