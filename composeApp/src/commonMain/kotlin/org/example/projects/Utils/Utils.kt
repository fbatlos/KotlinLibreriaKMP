package org.example.projects.Utils

import com.es.aplicacion.dto.LibroDTO
import org.example.projects.BaseDeDatos.model.Libro

object Utils {
    fun castearLibroToLibroDTO(libro: Libro):LibroDTO{
        return LibroDTO(libro._id,libro.titulo ?: "",libro.precio ?: 0.0,libro.moneda ?: "eur")
    }

    fun comprobarToken(token:String?):Boolean{
        return !token.isNullOrBlank()
    }
}