package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val _id : String? = null,
    val userName: String,
    val email:String,
    var titulo:String,
    var cuerpo:String
)