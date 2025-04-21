package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.StateFlow


expect open class UiStateViewModel() : ViewModel {
    val isLoading: StateFlow<Boolean>
    val showDialog: StateFlow<Boolean>
    val textError: StateFlow<String>


    fun setShowDialog(show: Boolean)
    fun setTextError(error: String)
    fun setLoading(loading: Boolean)
}
