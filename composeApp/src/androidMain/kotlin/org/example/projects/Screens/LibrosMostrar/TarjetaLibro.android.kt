package org.example.projects.Screens.LibrosMostrar

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
actual fun ImagenDesdeUrl(libro: Libro) {
    Image(
        painter = rememberAsyncImagePainter(libro.imagen?:""),
        contentDescription = "Portada de ${libro.titulo}",
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
        contentScale = ContentScale.Crop
    )
}