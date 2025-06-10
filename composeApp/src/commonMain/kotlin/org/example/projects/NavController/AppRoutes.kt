package org.example.projects.NavController

import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.Utils.LibroSerializer.toJsonString
import java.net.URLEncoder

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object MiPerfil:AppRoutes("miPerfil")
    object LibroLista : AppRoutes("libroLista")
    object Registro:AppRoutes("registro")
    object Carrito : AppRoutes("carrito")
    object LibroDetalles: AppRoutes("libroDetalles")
    object HistorialCompra: AppRoutes("historialCompra")
    object Inicio: AppRoutes("inicio")
    object MisValoraciones:AppRoutes("misValoraciones")
    object Ayuda:AppRoutes("ayuda")

    object LibrosAdmin:AppRoutes("librosAdmin")
    object CompraAdmin:AppRoutes("compraAdmin")
    object TicketDudaAdmin:AppRoutes("ticketDudaAdmin")

    companion object {
        fun fromString(route: String): AppRoutes {
            return when {
                route == "login" -> Login
                route == "libroLista" -> LibroLista
                route == "carrito" -> Carrito
                route == "libroDetalles" -> LibroDetalles
                route == "registro" -> Registro
                route == "historialCompra" -> HistorialCompra
                route == "inicio" -> Inicio
                route == "miPerfil"-> MiPerfil
                route == "misValoraciones" -> MisValoraciones
                route == "ayuda" -> Ayuda
                route == "librosAdmin" -> LibrosAdmin
                route == "compraAdmin" -> CompraAdmin
                route == "ticketDudaAdmin" -> TicketDudaAdmin
                else -> throw IllegalArgumentException("Unknown route: $route")
            }
        }
    }
}