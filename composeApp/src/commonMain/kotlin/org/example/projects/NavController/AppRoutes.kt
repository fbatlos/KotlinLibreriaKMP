package org.example.projects.NavController

import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.Utils.LibroSerializer.toJsonString
import org.example.projects.Utils.LibroSerializer.toLibro
import java.net.URLDecoder
import java.net.URLEncoder

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object LibroLista : AppRoutes("libroLista")

    data class LibroDetail(val libro: Libro) : AppRoutes("libroDetail/{libroJson}") {
        fun createRoute(): String {
            val libroJson = URLEncoder.encode(libro.toJsonString(), "UTF-8")
            return "libroDetail/$libroJson"
        }
    }

    companion object {
        fun fromString(route: String): AppRoutes {
            return when {
                route == "login" -> Login
                route == "libroLista" -> LibroLista
                route.startsWith("libroDetail/") -> {
                    val libroJson = URLDecoder.decode(route.substringAfter("libroDetail/"), "UTF-8")
                    LibroDetail(libroJson.toLibro())
                }
                else -> throw IllegalArgumentException("Unknown route: $route")
            }
        }
    }
}