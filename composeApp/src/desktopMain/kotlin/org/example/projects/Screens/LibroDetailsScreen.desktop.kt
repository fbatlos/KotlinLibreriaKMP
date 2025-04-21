package org.example.projects.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import org.example.projects.BaseDeDatos.model.Libro

@Composable
actual fun ImagenLibroDetails(url: String?, contentDescription: String?,modifier: Modifier) {
    val painterResource = asyncPainterResource(url?:"")

    when (painterResource) {
        is Resource.Loading -> {
            CircularProgressIndicator()
        }

        is Resource.Success -> {
            Image(
                painter = painterResource.value,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }

        is Resource.Failure -> {
            Box(modifier = Modifier.size(200.dp)) {
                // TODO: imagen alternativa o error UI
            }
        }
    }
}