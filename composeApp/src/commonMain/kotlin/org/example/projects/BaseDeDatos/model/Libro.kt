package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    val _id : String,
    var titulo: String? = null,
    var descripcion: String? = null,
    var autores: List<String> = emptyList(),
    var precio: Double? = null,
    var moneda: String? = null,
    var imagen: String? = null,
    var enlaceEbook: String? = null,
    var isbn13: String? = null,
    var categorias: List<String> = emptyList(),
    val valoracionMedia: Double? = 0.0,
    var stock: Stock
)