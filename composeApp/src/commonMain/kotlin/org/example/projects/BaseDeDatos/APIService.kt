package org.example.projects.BaseDeDatos

// commonMain/kotlin/com/example/actapp/BaseDeDatos/APIService.kt


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.http.auth.*
import org.example.projects.BaseDeDatos.DTO.UsuarioDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioRegisterDTO
import org.example.projects.BaseDeDatos.model.AuthResponse
import org.example.projects.BaseDeDatos.model.Avatar
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.BaseDeDatos.model.Direccion
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Ticket
import org.example.projects.BaseDeDatos.model.Valoracion

interface APIService {

    suspend fun postLogin(usuarioLoginDTO: UsuarioLoginDTO): HttpResponse
    suspend fun postRegister(usuario: UsuarioRegisterDTO): AuthResponse
    suspend fun getUsuario(token: String): UsuarioDTO
    suspend fun addDireccion(token: String, direccion: Direccion): String
    suspend fun deleteDireccion(token: String, direccion: Direccion)

    suspend fun getMiAvatar(idAvatar: String, token: String): Avatar
    suspend fun getAllAvatares(token: String): List<Avatar>
    suspend fun updateUsuarioAvatar(idNuevoAvatar:String,token: String):String

    suspend fun listarLibros(categoria:String?,autor: String?): List<Libro>
    suspend fun filtrarLibros(token: String,query:String): List<Libro>

    suspend fun addLibroFavorito(token: String,idLibro: String): HttpResponse
    suspend fun removeLibroFavorito(token: String,idLibro: String): HttpResponse
    suspend fun getLibrosfavoritos(token: String): MutableList<String>

    suspend fun getValoraciones(idLibro: String):List<Valoracion>
    suspend fun addValoracion(valoracion: Valoracion, token: String):String
    suspend fun removeValoracion(valoracionId:String, token: String):String
    suspend fun getMisValoraciones(token: String):List<Valoracion>

    suspend fun getCesta(token: String):MutableList<ItemCompra>
    suspend fun addCesta(token: String, itemCompra: ItemCompra):String
    suspend fun updateCesta(token: String,itemsCompra:List<ItemCompra>):String
    suspend fun removeAllCesta(token: String): HttpResponse
    suspend fun removeLibroCesta(token: String, idLibro: String):HttpResponse

    suspend fun checkout(compra: Compra,token: String) : Map<String, String>
    suspend fun actualizarStock(compra: Compra, token: String):String
    suspend fun obtenerEstadoPago(sessionId:String,token: String):Map<String, String>

    suspend fun addTicket(compra: Compra,token: String): Map<String, Boolean>
    suspend fun getTicketCompra(token:String):MutableList<Compra>

    suspend fun addTicketDuda(ticket: Ticket,token: String):Ticket

    //ADMIN

    suspend fun addLibro(libro: Libro,token: String):Boolean
    suspend fun putLibro(libroID:String,libro: Libro,token: String):Boolean
    suspend fun deleteLibro(libroID: String,token: String):Boolean

    suspend fun allCompras(token: String):List<Compra>
    suspend fun allTicket(token: String):List<Ticket>
}