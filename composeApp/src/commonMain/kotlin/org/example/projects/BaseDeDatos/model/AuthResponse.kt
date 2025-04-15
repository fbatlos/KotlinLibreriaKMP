package org.example.projects.BaseDeDatos.model

import org.example.projects.BaseDeDatos.DTO.UsuarioDTO


data class AuthResponse(
    val token: String,
    val user: UsuarioDTO
)
