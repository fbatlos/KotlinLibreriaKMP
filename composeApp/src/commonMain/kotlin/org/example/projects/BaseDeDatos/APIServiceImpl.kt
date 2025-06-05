package org.example.projects.BaseDeDatos

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.DTO.UsuarioDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.ErrorAPI.ApiException
import org.example.projects.BaseDeDatos.ErrorAPI.AuthException
import org.example.projects.BaseDeDatos.ErrorAPI.ErrorResponse
import org.example.projects.BaseDeDatos.model.AuthResponse
import org.example.projects.BaseDeDatos.model.Avatar
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.BaseDeDatos.model.Direccion
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Valoracion
import java.net.URLEncoder

class APIServiceImpl(private val client: HttpClient) : APIService {

    override suspend fun postLogin(usuarioLoginDTO: UsuarioLoginDTO): HttpResponse {
        val response = client.post("usuarios/login") {
            contentType(ContentType.Application.Json)
            setBody(usuarioLoginDTO)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}",
            )
        }
    }

    override suspend fun postRegister(usuario: UsuarioRegisterDTO): AuthResponse {
        val response =  client.post("usuarios/register") {
            setBody(usuario)
        }

        return when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}",
            )
        }
    }

    override suspend fun getUsuario(token: String): UsuarioDTO {
        val response = client.get("usuarios/usuario"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun addDireccion(token: String, direccion: Direccion): String {
        val response = client.put("usuarios/direccion") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(direccion)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException("Error ${response.status.value}: ${response.body<ErrorResponse>().message}")
        }
    }


    override suspend fun deleteDireccion(token: String, direccion: Direccion) {
        val response = client.delete("usuarios/direccion") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(direccion)
        }

        when (response.status) {
            HttpStatusCode.NoContent -> {} // todo ok, no hay body
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException("Error ${response.status.value}: ${response.body<ErrorResponse>().message}")
        }
    }


    override suspend fun getMiAvatar(idAvatar: String, token: String): Avatar {
        val response = client.get("avatar/miAvatar/${idAvatar}") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                return response.body()
            }
            HttpStatusCode.NotFound -> throw ApiException("Avatar no encontrado")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun getAllAvatares(token: String): List<Avatar> {
        val response = client.get("avatar/allAvatares") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                return response.body()
            }

                else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun updateUsuarioAvatar(idNuevoAvatar: String, token: String): String {
        val response = client.put("usuarios/avatar") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(idNuevoAvatar)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException("Error ${response.status.value}: ${response.body<ErrorResponse>().message}")
        }
    }


    override suspend fun listarLibros(categoria: String?,autor:String?): List<Libro> {
        val query = mutableListOf<String>().apply {
            if (!categoria.isNullOrBlank()) {
                add("categoria=${URLEncoder.encode(categoria, "UTF-8")}")
            }
            if (!autor.isNullOrBlank()) {
                add("autor=${URLEncoder.encode(autor, "UTF-8")}")
            }
        }

        val url = if (query.isNotEmpty()) {
            "libros?${query.joinToString("&")}"
        } else {
            "libros"
        }

        val response = client.get(url)

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
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
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun addLibroFavorito(token: String, idLibro: String): HttpResponse {
        val response = client.post("usuarios/favoritos/$idLibro") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun removeLibroFavorito(token: String, idLibro: String): HttpResponse {
        val response = client.delete("usuarios/favoritos/$idLibro") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
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
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun getValoraciones(idLibro: String): List<Valoracion> {
        val response = client.get("valoracion/$idLibro")

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun addValoracion(valoracion: Valoracion, token: String): String {
        val response = client.post("valoracion/add") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(valoracion)
        }

        return when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.Conflict -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun removeValoracion(valoracionId: String, token: String): String {
        val response = client.delete("valoracion/eliminar/${valoracionId}"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun getMisValoraciones(token: String): List<Valoracion> {
        val response = client.get("valoracion/mis-valoraciones"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }


    override suspend fun getCesta(token: String): MutableList<ItemCompra> {
        val response = client.get("usuarios/cesta") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun addCesta(token: String,  itemCompra: ItemCompra): String {
        val response = client.post("usuarios/cesta") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(itemCompra)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun updateCesta(token: String, itemsCompra: List<ItemCompra>): String {
        val response = client.put("usuarios/cesta") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(itemsCompra)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun removeAllCesta(token: String): HttpResponse {
        val response = client.delete("usuarios/cesta") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun removeLibroCesta(token: String, idLibro: String): HttpResponse {
        val response = client.delete("usuarios/cesta/$idLibro") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}")
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun checkout(compra: Compra,token: String): Map<String, String> {
        val response = client.post("/compra/checkout") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(compra)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.NotFound -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun actualizarStock(compra: Compra, token: String): String {
        val response = client.post("/compra/actualizar-stock"){
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(compra)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.InternalServerError -> throw ApiException("Error ${response.body<ErrorResponse>().message}, ${response.body<String>()}")
            HttpStatusCode.Unauthorized -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun obtenerEstadoPago(sessionId: String,token: String): Map<String, String> {
        val response = client.post("/compra/estado/$sessionId"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.InternalServerError -> throw ApiException("Error ${response.body<ErrorResponse>().message}, ${response.body<String>()}")
            HttpStatusCode.Unauthorized -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun addTicket(compra: Compra,token: String): Map<String, Boolean> {
        val response = client.post("/compra/ticket") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(compra)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.BadRequest -> throw ApiException("Error ${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

    override suspend fun getTicketCompra(token: String): MutableList<Compra> {
        val response = client.get("/compra/tickets") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw AuthException("Token inválido")
            HttpStatusCode.BadRequest -> throw ApiException("${response.body<ErrorResponse>().message}") //No debería saltar nunca
            else -> throw ApiException(
                "Error ${response.status.value}: ${response.body<ErrorResponse>().message}"
            )
        }
    }

}