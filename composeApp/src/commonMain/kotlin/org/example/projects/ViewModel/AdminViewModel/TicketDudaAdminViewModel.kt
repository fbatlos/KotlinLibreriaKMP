package org.example.projects.ViewModel.AdminViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Ticket
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

class TicketDudaAdminViewModel(
    private val uiViewModel: UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    private var _allTickets = MutableStateFlow<List<Ticket>?>(null)
    val allTickets = _allTickets

    fun gatAllTickets(){
        viewModelScope.launch {
            uiViewModel.setLoading(true)
            try {
                _allTickets.value = API.apiService.allTicket(sharedViewModel.token.value!!)
                println(_allTickets.value)
            } catch (e: Exception) {
                uiViewModel.setTextError("Error al añadir: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

}