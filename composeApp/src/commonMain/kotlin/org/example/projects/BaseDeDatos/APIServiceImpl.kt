package org.example.projects.BaseDeDatos

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.ErrorAPI.ApiException
import org.example.projects.BaseDeDatos.ErrorAPI.AuthException
import org.example.projects.BaseDeDatos.model.AuthResponse
import org.example.projects.BaseDeDatos.model.Libro

class APIServiceImpl(private val client: HttpClient) : APIService {
    override suspend fun postLogin(usuarioLoginDTO: UsuarioLoginDTO): HttpResponse {
        return client.post("usuarios/login") {
            contentType(ContentType.Application.Json)
            setBody(usuarioLoginDTO)
        }
    }

    override suspend fun postRegister(usuario: UsuarioRegisterDTO): AuthResponse {
        return client.post("usuarios/register") {
            contentType(ContentType.Application.Json)
            setBody(usuario)
        }.body()
    }

    override suspend fun listarLibros(): List<Libro> {
        val response = client.get("libros")

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.status.description}",
                response.status
            )
        }
    }

    override suspend fun filtrarLibros(token: String,query:String): List<Libro> {
        val response = client.get("libros/buscar?query=$query") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.status.description}",
                response.status
            )
        }
    }

    override suspend fun addLibroFavorito(token: String, idLibro: String): HttpResponse {
        val response = client.post("usuarios/favoritos/$idLibro") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.status.description}", response.status) //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.status.description}",
                response.status
            )
        }
    }

    override suspend fun removeLibroFavorito(token: String, idLibro: String): HttpResponse {
        val response = client.delete("usuarios/favoritos/$idLibro") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.status.description}", response.status) //No debería saltar nunca
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.status.description}", response.status)
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.status.description}",
                response.status
            )
        }
    }

    override suspend fun getLibrosfavoritos(token: String): MutableList<String> {
        val response = client.get("usuarios/favoritos") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.status.description}", response.status) //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.status.description}",
                response.status
            )
        }
    }
}