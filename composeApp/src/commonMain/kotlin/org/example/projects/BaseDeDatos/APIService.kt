package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/APIService.kt


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.http.auth.*
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.model.AuthResponse
import org.example.projects.BaseDeDatos.model.Libro

interface APIService {
    suspend fun postLogin(usuarioLoginDTO: UsuarioLoginDTO): HttpResponse
    suspend fun postRegister(usuario: UsuarioRegisterDTO): AuthResponse
    suspend fun listarLibros(authHeader: String): List<Libro>
    suspend fun filtrarLibros(token: String,query:String): List<Libro>

    suspend fun addLibroFavorito(token: String,idLibro: String): HttpResponse
    suspend fun removeLibroFavorito(token: String,idLibro: String): HttpResponse
    suspend fun getLibrosfavoritos(token: String): MutableList<String>
}