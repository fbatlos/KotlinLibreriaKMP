package org.example.projects.BaseDeDatos.DTO

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioInterfazDTO(
    val username: String,
    val rol: String?
)