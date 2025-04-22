package org.example.projects.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import coil.transform.CircleCropTransformation
import org.example.projects.BaseDeDatos.model.Libro

@Composable
actual fun ImagenLibroDetails(url: String?, contentDescription: String?,modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .precision(Precision.AUTOMATIC)
            .allowHardware(false)
            .crossfade(500)
            .build(),
        contentDescription = "Portada del libro",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.FillWidth,
        filterQuality = FilterQuality.High
    )
}