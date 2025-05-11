package com.es.aplicacion.dto

import kotlinx.serialization.Serializable

@Serializable
data class LibroDTO (
    val _id: String?,
    val titulo: String,
    val precio: Double,
    val moneda: String
)