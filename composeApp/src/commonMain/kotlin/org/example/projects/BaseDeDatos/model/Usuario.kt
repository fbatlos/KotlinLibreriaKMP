package org.example.projects.BaseDeDatos.model


data class Usuario(
    val _id : String?,
    val username: String,
    val password: String,
    val email: String,
    val roles: String? = "USER",
    var direccion: MutableList<Direccion>,
    val librosfav: MutableList<String>,
    val cesta:MutableList<Libro>,
    val avatar:String
)