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

expect fun openUrl(url: String)

//todo a lo mejor no se usa
expect fun registerDeepLinkHandler(onDeepLinkReceived: (String) -> Unit)


class CarritoViewModel(
    private val uiStateViewModel:UiStateViewModel,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _items = MutableStateFlow<List<ItemCompra>>(emptyList())
    val items: StateFlow<List<ItemCompra>> = _items

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

    }

    fun actualizarCantidad(libro: LibroDTO, nuevaCantidad: Int) {
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

    fun eliminarLibro(libro: LibroDTO) {
        _items.update { current ->
            current.filterNot { it.libro == libro }
        }
    }

    fun checkout(compra: Compra, token: String) {
        uiStateViewModel.setLoading(true)
        viewModelScope.launch {

            val response = API.apiService.checkout(compra, token)
            withContext(Dispatchers.Main) {
                _sessionUrl.value = response["url"]
                _sessionId.value = response["sessionId"]
                println(response["sessionId"])
                openUrl(_sessionUrl.value!!)
            }
        }
        uiStateViewModel.setLoading(false)
    }

    fun verEstadoPago(pagado:Boolean){
        if (pagado){
            //TODO API PARA CREAR EL TICKET
            _items.value = emptyList()
            addTicketCompra()
        }
    }

    fun addTicketCompra(){
        viewModelScope.launch {

        }
    }

    val total: StateFlow<Double> = _items.map { items ->
        items.sumOf { it.libro.precio?.toDouble()?.times(it.cantidad) ?: 0.0 }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
}
