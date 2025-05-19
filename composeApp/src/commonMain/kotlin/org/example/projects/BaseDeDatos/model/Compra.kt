package org.example.projects.BaseDeDatos.model

import com.es.aplicacion.dto.LibroDTO
import kotlinx.serialization.Serializable


@Serializable
data class Compra(
    val usuarioName: String,
    val items: List<ItemCompra>,
    val fechaCompra: String
)

@Serializable
data class ItemCompra(
    val libro: LibroDTO,
    val cantidad: Int
)
