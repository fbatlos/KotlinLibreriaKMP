package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.projects.BaseDeDatos.model.Libro

actual open class UiStateViewModel actual constructor(): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    actual val isLoading: StateFlow<Boolean> = _isLoading

    private val _textError = MutableStateFlow("")
    actual  val textError: StateFlow<String> = _textError

    private val _showDialog = MutableStateFlow(false)
    actual  val showDialog: StateFlow<Boolean> = _showDialog

    actual fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    actual  fun setTextError(error: String) {
        _textError.value = error
    }

    actual  fun setShowDialog(show: Boolean) {
        _showDialog.value = show
    }
}
