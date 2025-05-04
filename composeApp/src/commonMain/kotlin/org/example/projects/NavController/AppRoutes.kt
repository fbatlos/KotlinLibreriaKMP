package org.example.projects.NavController

import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.Utils.LibroSerializer.toJsonString
import java.net.URLEncoder

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object LibroLista : AppRoutes("libroLista")
    object Registro:AppRoutes("registro")
    object Carrito : AppRoutes("carrito")

    data class LibroDetail(val libro: Libro) : AppRoutes("libroDetail") {
        fun createRoute(): String {
            val libroJson = URLEncoder.encode(libro.toJsonString(), "UTF-8")
            return "libroDetail?libroJson=$libroJson"
        }
    }



    companion object {
        fun fromString(route: String): AppRoutes {
            return when {
                route == "login" -> Login
                route == "libroLista" -> LibroLista
                route == "carrito" -> Carrito
                route.startsWith("libroDetail") -> LibroLista
                else -> throw IllegalArgumentException("Unknown route: $route")
            }
        }
    }
}