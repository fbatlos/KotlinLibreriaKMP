package org.example.projects.BaseDeDatos.DTO

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLoginDTO(
    var username: String,
    var password: String
)

