package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/APIService.kt


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.Model.AuthResponse
import org.example.projects.BaseDeDatos.Model.Tarea

interface APIService {
    suspend fun postLogin(usuarioLoginDTO: UsuarioLoginDTO): HttpResponse
    suspend fun postRegister(usuario: UsuarioRegisterDTO): AuthResponse
    suspend fun getTareas(authHeader: String): List<Tarea>
    suspend fun putTareas(authHeader: String, id: String, tarea: Tarea): Tarea
}