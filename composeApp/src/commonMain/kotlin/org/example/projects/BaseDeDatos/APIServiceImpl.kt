package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/APIServiceImpl.kt
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

    override suspend fun listarLibros(token: String): List<Libro> {
        val response = client.get("libros") {  // URL relativa a BASE_URL
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

    override suspend fun filtrarLibros(token: String,query:String): List<Libro> {
        val response = client.get("libros/buscar?query=$query") {  // URL relativa a BASE_URL
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

    /*
        override suspend fun getTareas(authHeader: String): List<Tarea> {
            return client.get("tareas/tareas") {
                headers {
                    append(HttpHeaders.Authorization, authHeader)
                }
            }.body()
        }

        override suspend fun putTareas(authHeader: String, id: String, tarea: Tarea): Tarea {
            return client.put("tareas/tarea/$id") {
                contentType(ContentType.Application.Json)
                headers {
                    append(HttpHeaders.Authorization, authHeader)
                }
                setBody(tarea)
            }.body()
        }*/
}