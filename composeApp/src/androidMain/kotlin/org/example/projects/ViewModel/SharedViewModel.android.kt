package org.example.projects.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


actual class SharedViewModel actual constructor(): dev.icerock.moko.mvvm.viewmodel.ViewModel(){

    private val _token = MutableStateFlow<String?>(null)
    actual val token: StateFlow<String?> = _token

    actual fun setToken(token: String) {
        _token.value = token
    }

}