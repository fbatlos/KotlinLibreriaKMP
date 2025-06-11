package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Ticket

class TicketViewModel (
    private val uiViewModel: UiStateViewModel,
    private val authViewModel: AuthViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _titulo = MutableStateFlow<String>("")
    val tituloTicket: StateFlow<String> = _titulo

    private val _cuerpo = MutableStateFlow<String>("")
    val cuerpo: StateFlow<String> = _cuerpo

    private val _showTicket = MutableStateFlow<Boolean>(true)
    val showTicket: StateFlow<Boolean> = _showTicket

    fun onChangeTickt(titulo:String,cuerpo:String){
        _titulo.value = titulo
        _cuerpo.value = cuerpo
    }

    fun addTicketDuda(){
        uiViewModel.setLoading(true)
        viewModelScope.launch {
            try {
                API.apiService.addTicketDuda(Ticket(userName = authViewModel.username.value!!, titulo = _titulo.value, email = authViewModel.email.value ,cuerpo = _cuerpo.value),sharedViewModel.token.value!!)
                setShowTicket(false)
            }catch (e:Exception){
                uiViewModel.setLoading(false)
                uiViewModel.setTextError("Error: ${e.message}")
                uiViewModel.setShowDialog(true)
            }
        }
        uiViewModel.setLoading(false)
    }

    fun setShowTicket(show:Boolean){
        _showTicket.value = show
    }
}