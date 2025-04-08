package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/APIServiceImpl.kt
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.Model.AuthResponse
import org.example.projects.BaseDeDatos.Model.Tarea

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
    }
}