package org.example.projects.BaseDeDatos.model

import kotlinx.serialization.Serializable
import org.example.projects.BaseDeDatos.DTO.UsuarioInterfazDTO

@Serializable
data class AuthResponse(
    val token: String,
    val user: UsuarioInterfazDTO
)
