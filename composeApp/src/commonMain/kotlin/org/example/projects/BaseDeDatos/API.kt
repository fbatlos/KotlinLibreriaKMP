package org.example.projects.BaseDeDatos

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

import javax.net.ssl.SSLContext
//Objeto api para usar en cualquier lugar
object API {
    private const val BASE_URL = "https://api-tfg.onrender.com/"

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    //Variable Criente para poder realizar peticiones seguras
    private val client: HttpClient by lazy {
        HttpClient() {
            install(ContentNegotiation) {
                json(jsonConfig)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
            }

            defaultRequest {
                header(HttpHeaders.Accept, "application/json")
                header(HttpHeaders.ContentType, "application/json")
                url(BASE_URL)
            }

            expectSuccess = false  // Permite manejar manualmente códigos de error
        }
    }

    val apiService: APIService by lazy {
        APIServiceImpl(client)
    }
    //Parseo del Error
    suspend fun parseError(response: HttpResponse): ApiError {
        return try {
            response.body<ApiError>() ?: ApiError("Unknown error")
        } catch (e: Exception) {
            ApiError("Error parsing error response: ${e.message}")
        }
    }
}


@Serializable
data class ApiError(val message: String)