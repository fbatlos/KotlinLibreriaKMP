package org.example.projects.Utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.es.aplicacion.dto.LibroDTO
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.model.Libro
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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

    @OptIn(ExperimentalEncodingApi::class)
    fun isUserAdmin(token: String): Boolean {
        if (token.isNullOrEmpty()) return false
        val parts = token.split(".")
        if (parts.size != 3) return false

        return try {
            val payloadJson = Base64.decode(parts[1].padEnd(parts[1].length + (4 - parts[1].length % 4) % 4, '=')) // padding fix
                .decodeToString()
            if (payloadJson.contains("ROLE_ADMIN")){
                true
            }else{
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
