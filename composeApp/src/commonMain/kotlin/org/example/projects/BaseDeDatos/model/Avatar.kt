package org.example.projects.BaseDeDatos.model

import io.ktor.websocket.Frame
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Base64

// En commonMain/kotlin
@Serializable
data class Avatar(
    @SerialName("_id")
    val id: String? = null,
    val filename: String,
    val mimeType: String,
    val data: String
)

@Serializable
data class FileData(
    @SerialName("\$binary")
    val binary: BinaryData
)

@Serializable
data class BinaryData(
    val base64: String,
    @SerialName("subType")
    val subType: String = "00"
)