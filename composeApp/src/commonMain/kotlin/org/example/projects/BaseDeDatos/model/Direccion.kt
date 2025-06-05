package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Direccion(
    val calle: String,
    val num: String,
    val municipio: String,
    val provincia: String
)
