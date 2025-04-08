package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/API.kt
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.APIServiceImpl
// commonMain/kotlin/com/example/actapp/BaseDeDatos/ErrorAPI/ApiError.kt
import kotlinx.serialization.Serializable

object API {
    private const val BASE_URL = "http://localhost:8080/"

    // Configuración del JSON
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    private val client: HttpClient by lazy {
        HttpClient() {
            defaultRequest {
                url(BASE_URL)
            }
            install(ContentNegotiation) {
                json(jsonConfig)
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
            }
        }
    }

    val apiService: APIService by lazy {
        APIServiceImpl(client)
    }

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