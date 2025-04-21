package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro

actual class SharedViewModel actual constructor(): ViewModel(){

    private val _token = MutableStateFlow<String?>(null)
    actual val token: StateFlow<String?> = _token

    actual fun setToken(token: String) {
        _token.value = token
    }

}