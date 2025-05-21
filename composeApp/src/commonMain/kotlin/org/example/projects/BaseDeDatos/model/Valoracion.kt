package org.example.projects.BaseDeDatos.model


import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Valoracion(
    val _id : String?,
    val libro_id : String,
    val usuarioName : String,
    val valoracion : Int,
    val comentario:String,
    val fecha : String
)