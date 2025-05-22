package org.example.projects.BaseDeDatos.model


import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.Date

@Serializable
data class Valoracion(
    val _id : String?,
    val libroid : String,
    val usuarioName : String,
    val valoracion : Int,
    val comentario:String,
    val fecha : String
)