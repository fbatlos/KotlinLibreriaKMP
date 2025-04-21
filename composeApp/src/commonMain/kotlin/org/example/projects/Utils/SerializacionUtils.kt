package org.example.projects.Utils

// commonMain/kotlin/util/SerializationUtil.kt
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.model.Libro

object LibroSerializer {
    fun Libro.toJsonString(): String {
        return Json.encodeToString(this)
    }

    fun String.toLibro(): Libro {
        return Json.decodeFromString<Libro>(this)
    }
}
