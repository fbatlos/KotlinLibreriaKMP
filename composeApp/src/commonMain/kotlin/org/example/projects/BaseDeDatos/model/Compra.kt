package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Compra(
    val usuarioName: String,
    val items: List<ItemCompra>
)

@Serializable
data class ItemCompra(
    val libro: Libro,
    val cantidad: Int
)
