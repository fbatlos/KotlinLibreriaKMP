package org.example.projects.Utils


import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.model.Libro

object LibroSerializer {
    fun Libro.toJsonString(): String {
        return Json.encodeToString(this)
    }


    private val json = Json { ignoreUnknownKeys = true }

    fun String.toLibro(): Libro = json.decodeFromString(this)

}
