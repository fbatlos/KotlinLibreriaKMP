package org.example.projects.ViewModel

import com.es.aplicacion.dto.LibroDTO
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import java.time.LocalDateTime

expect fun openUrl(url: String)

//todo a lo mejor no se usa
expect fun registerDeepLinkHandler(onDeepLinkReceived: (String) -> Unit)


class CarritoViewModel(
    private val uiStateViewModel:UiStateViewModel,
    private val authViewModel: AuthViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _items = MutableStateFlow<List<ItemCompra>>(
        emptyList()
    )
    val items: StateFlow<List<ItemCompra>> = _items

    private val _tickets = MutableStateFlow<List<Compra>?>(null)
    val tickets: StateFlow<List<Compra>?> = _tickets

    val cestaSize: StateFlow<Int> = _items.map { items ->
        items.sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    private var _sessionUrl = MutableStateFlow<String?>(null)
    private val _sessionId = MutableStateFlow<String?>(null)

    fun agregarLibro(libro: LibroDTO) {
        _items.update { current ->
            val index = current.indexOfFirst { it.libro == libro }
            if (index != -1) {
                current.toMutableList().apply {
                    this[index] = this[index].copy(cantidad = this[index].cantidad + 1)
                }
            } else {
                current + ItemCompra(libro, 1)
            }
        }

        if (!sharedViewModel.token.value.isNullOrBlank()) {
            viewModelScope.launch {
                try {
                    API.apiService.addCesta(sharedViewModel.token.value!!, ItemCompra(libro, 1))
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun actualizarCantidad(libro: LibroDTO?, nuevaCantidad: Int?) {
        if (nuevaCantidad != null) {
            _items.update { current ->
                val index = current.indexOfFirst { it.libro == libro }
                if (index != -1) {
                    if (nuevaCantidad <= 0) {
                        current.toMutableList().apply { removeAt(index) }
                    } else {
                        current.toMutableList().apply {
                            this[index] = this[index].copy(cantidad = nuevaCantidad)
                        }
                    }
                } else {
                    current
                }
            }
        }

        if (!sharedViewModel.token.value.isNullOrBlank()){
            viewModelScope.launch {
                try {
                    API.apiService.updateCesta(sharedViewModel.token.value!!, _items.value)
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun eliminarLibro(libro: LibroDTO) {
        _items.update { current ->
            current.filterNot { it.libro == libro }
        }

        if (!sharedViewModel.token.value.isNullOrBlank()) {
            viewModelScope.launch {
                try {
                    API.apiService.removeLibroCesta(sharedViewModel.token.value!!, libro._id!!)
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun checkout(compra: Compra) {
        uiStateViewModel.setLoading(true)
        viewModelScope.launch {
            val response = API.apiService.checkout(compra, sharedViewModel.token.value!!)
            withContext(Dispatchers.Main) {
                _sessionUrl.value = response["url"]
                _sessionId.value = response["sessionId"]
                openUrl(_sessionUrl.value!!)
            }
        }
    }

    fun verEstadoPago(pagado:Boolean){
        uiStateViewModel.setLoading(false)
        if (pagado){
            actualizarStok()
            addTicketCompra()
        }
    }

    fun actualizarStok(){
        viewModelScope.launch {
            try {
               API.apiService.actualizarStock(Compra(authViewModel.username.value, _items.value,LocalDateTime.now().toString(), direccion = authViewModel.direccionSeleccionada.value!!), token = sharedViewModel.token.value!!)
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al actualizar el Stock: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
    }

    fun addTicketCompra(){
        uiStateViewModel.setLoading(true)
        viewModelScope.launch { // Usa el viewModelScope de Moko-MVVM
            try {
                API.apiService.removeAllCesta(token = sharedViewModel.token.value!!)
                delay(100)
                API.apiService.addTicket(Compra(authViewModel.username.value, _items.value,LocalDateTime.now().toString(), direccion = authViewModel.direccionSeleccionada.value!!), token = sharedViewModel.token.value!!)
                _items.value = emptyList()
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al subir el ticket: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
        uiStateViewModel.setLoading(false)
    }

    fun getTicketsCompra(){
        uiStateViewModel.setLoading(true)
        viewModelScope.launch {
            try {
                val response = API.apiService.getTicketCompra(token = sharedViewModel.token.value!!)
                _tickets.value = response.sortedByDescending { it.fechaCompra }
            } catch (e: Exception) {
                uiStateViewModel.setTextError("Error al cargar los tickets: ${e.message}")
                uiStateViewModel.setShowDialog(true)
            }
        }
        uiStateViewModel.setLoading(false)
    }

    fun getCesta(){
        if ( !sharedViewModel.token.value.isNullOrBlank()) {
            viewModelScope.launch {
                try {
                    val response = API.apiService.getCesta(token = sharedViewModel.token.value!!)
                    when{
                        _items.value.isEmpty() -> {
                            _items.value = response
                        }
                        _items.value.isNotEmpty() && response.isEmpty() ->{
                            actualizarCantidad(null,null)
                        }
                        _items.value.isNotEmpty() && response.isNotEmpty() -> {
                            _items.value.forEach {
                                response.add(it)
                            }
                            _items.value = response
                            actualizarCantidad(null,null)
                        }
                    }
                } catch (e: Exception) {
                    uiStateViewModel.setTextError("Error al cargar la cesta: ${e.message}")
                    uiStateViewModel.setShowDialog(true)
                }
            }
        }
    }

    fun limpiar(){
        _items.value = emptyList()
        _tickets.value = emptyList()
    }

    val total: StateFlow<Double> = _items.map { items ->
        items.sumOf { it.libro.precio?.toDouble()?.times(it.cantidad) ?: 0.0 }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
}
