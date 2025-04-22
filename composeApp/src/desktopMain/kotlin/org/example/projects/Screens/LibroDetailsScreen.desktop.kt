package org.example.projects.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource


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
                Text("Error al cargar la imagen", color = Color.Red)
            }
        }
    }
}