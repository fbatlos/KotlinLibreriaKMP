package org.example.projects.Utils

import com.es.aplicacion.dto.LibroDTO
import org.example.projects.BaseDeDatos.model.Libro
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun castearLibroToLibroDTO(libro: Libro):LibroDTO{
        return LibroDTO(libro._id,libro.titulo ?: "",libro.precio ?: 0.0,libro.moneda ?: "eur")
    }

    fun formatearFecha(fechaNormal: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = parser.parse(fechaNormal)
            formato.format(date)
        } catch (e: Exception) {
            fechaNormal
        }
    }
}