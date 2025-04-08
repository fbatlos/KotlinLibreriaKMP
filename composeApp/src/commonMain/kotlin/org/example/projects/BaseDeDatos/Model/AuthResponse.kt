package org.example.projects.BaseDeDatos.Model

import kotlinx.serialization.Serializable
import org.example.projects.BaseDeDatos.DTO.UsuarioDTO

@Serializable
data class AuthResponse(
    val token: String,
    val user: UsuarioDTO
)
