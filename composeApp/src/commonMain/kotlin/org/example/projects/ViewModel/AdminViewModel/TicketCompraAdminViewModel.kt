package org.example.projects.ViewModel.AdminViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

class TicketCompraAdminViewModel  (
    private val uiViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    private var _allCompras = MutableStateFlow<List<Compra>?>(null)
    val allCompra = _allCompras

    fun gatAllCompra(){
        viewModelScope.launch {
            uiViewModel.setLoading(true)
            try {
                _allCompras.value = API.apiService.allCompras(sharedViewModel.token.value!!)
            } catch (e: Exception) {
                uiViewModel.setTextError("Error al añadir: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

}