package org.example.projects.BaseDeDatos.DTO

import kotlinx.serialization.Serializable
import org.example.projects.BaseDeDatos.model.Direccion


@Serializable
data class UsuarioDTO(
    val username: String,
    val email: String,
    var direccion: MutableList<Direccion>,
    val librosfav: MutableList<String> = mutableListOf(),
    val avatar:String,
    val rol: String?
)
