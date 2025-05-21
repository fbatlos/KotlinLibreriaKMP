package org.example.projects.BaseDeDatos.DTO


import kotlinx.serialization.Serializable
import org.example.projects.BaseDeDatos.model.Direccion

@Serializable
data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String? = "USER"
)