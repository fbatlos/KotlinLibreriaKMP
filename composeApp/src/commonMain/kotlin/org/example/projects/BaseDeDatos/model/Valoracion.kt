package org.example.projects.BaseDeDatos.model


import java.util.Date


data class Valoracion(
    val _id : String?,
    val libro_id : String,
    val usuario_id : String,
    val valoracion : Int,
    val comentario:String,
    val fecha : Date,
)