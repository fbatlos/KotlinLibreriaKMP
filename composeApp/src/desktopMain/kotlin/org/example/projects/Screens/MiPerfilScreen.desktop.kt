package org.example.projects.Screens

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image.Companion.makeFromEncoded
import androidx.compose.ui.graphics.asImageBitmap
import org.jetbrains.skia.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO


actual fun convertByteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap {
    val base64String = String(byteArray, Charsets.UTF_8)
    val cleanBase64 = base64String.substringAfterLast(",")
    val decodedBytes = Base64.getDecoder().decode(cleanBase64)
    val bufferedImage = ImageIO.read(ByteArrayInputStream(decodedBytes))
        ?: throw IllegalArgumentException("No se pudo decodificar la imagen")
    return bufferedImage.toComposeImageBitmap()
}
