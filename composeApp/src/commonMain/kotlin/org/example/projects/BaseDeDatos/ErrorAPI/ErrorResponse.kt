package org.example.projects.BaseDeDatos.ErrorAPI

@kotlinx.serialization.Serializable
data class ErrorResponse(
    val message: String,
    val uri: String
)
