package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

open class UiStateViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _textError = MutableStateFlow("")
    val textError: StateFlow<String> = _textError

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun setTextError(error: String) {
        _textError.value = error
    }

    fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }

    fun limpiar() {
        _isLoading.value = false
        _textError.value = ""
        _showDialog.value = false
    }
}

