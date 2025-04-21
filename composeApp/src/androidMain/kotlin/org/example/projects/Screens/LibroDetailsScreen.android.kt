package org.example.projects.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.example.projects.BaseDeDatos.model.Libro

@Composable
actual fun ImagenLibroDetails(url: String?, contentDescription: String?,modifier: Modifier) {
    Image(
        painter = rememberAsyncImagePainter(url?:""),
        contentDescription = contentDescription,
        modifier = modifier
    )
}