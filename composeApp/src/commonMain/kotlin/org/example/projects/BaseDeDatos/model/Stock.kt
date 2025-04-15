package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val tipo:TipoStock,
    val numero: Int
)
