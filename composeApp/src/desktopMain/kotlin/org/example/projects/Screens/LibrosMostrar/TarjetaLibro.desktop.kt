package org.example.projects.Screens.LibrosMostrar


import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.example.projects.BaseDeDatos.model.Libro

@Composable
actual fun ImagenDesdeUrl(libro: Libro) {

    val painterResource = asyncPainterResource(libro.imagen?:"")

    when (painterResource) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            Image(
                painter = painterResource.value,
                contentDescription = "Portada de ${libro.titulo}",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(80.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        is Resource.Failure -> {
            Box(modifier = Modifier.size(200.dp)) {
                // TODO: imagen alternativa o error UI
            }
        }
    }
}