package org.example.projects.Screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory.decodeByteArray
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream


actual fun convertByteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap {
    require(byteArray.isNotEmpty()) { "El array de bytes está vacío" }

    val stringData = String(byteArray, Charsets.UTF_8)


    val cleanBase64 = stringData.substringAfterLast(",")

    val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    bitmap?: throw IllegalArgumentException("No se pudo decodificar la imagen (formato no soportado o datos corruptos)")

    return bitmap.asImageBitmap()
}