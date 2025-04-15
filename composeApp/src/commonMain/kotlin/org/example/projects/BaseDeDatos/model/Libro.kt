package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    var titulo: String? = null,
    var autores: List<String> = emptyList(),
    var precio: Double? = null,
    var moneda: String? = null,
    var imagen: String? = null,
    var enlaceEbook: String? = null,
    var isbn13: String? = null,
    var categorias: List<String> = emptyList(),
    var stock: Stock


)