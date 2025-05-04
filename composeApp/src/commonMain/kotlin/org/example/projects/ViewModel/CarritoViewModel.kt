package org.example.projects.ViewModel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.projects.BaseDeDatos.model.Libro

class CarritoViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<Libro, Int>>(emptyMap())
    val items: StateFlow<Map<Libro, Int>> = _items

    private var _cestaSize = MutableStateFlow(0)
    val cestaSize: StateFlow<Int> = _cestaSize

    fun agregarLibro(libro: Libro) {
        _items.update { current ->
            current.toMutableMap().apply {
                this[libro] = (this[libro] ?: 0) + 1
                _cestaSize.value += 1
            }
        }
    }

    fun actualizarCantidad(libro: Libro, nuevaCantidad: Int) {
        _items.update { current ->
            if (nuevaCantidad <= 0) {
                current - libro
            } else {
                current.toMutableMap().apply {
                    this[libro] = nuevaCantidad
                    _cestaSize.value -= 1
                }
            }
        }

        _items.value.values.map { _cestaSize.value = it }
    }

    fun eliminarLibro(libro: Libro) {
        _items.update { it - libro }
        _cestaSize.value = _items.value.size
    }

    val total: StateFlow<Double> = _items.map { items ->
        items.entries.sumOf { (libro, cantidad) ->
            (libro.precio?.toDouble() ?: 0.0) * cantidad
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
}