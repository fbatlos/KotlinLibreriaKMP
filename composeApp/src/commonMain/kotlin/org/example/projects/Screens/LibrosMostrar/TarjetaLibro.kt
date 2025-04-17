package org.example.projects.Screens.LibrosMostrar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.projects.BaseDeDatos.model.Libro

@Composable
expect fun ImagenDesdeUrl(libro: Libro)

@Composable
fun TarjetaLibro(
    libro: Libro,
    modifier: Modifier = Modifier,
    onFavoritoClick : (Boolean) -> Unit,
    token: String?,
    librosFavoritos: List<String>
) {
    val esFavorito = librosFavoritos.contains(libro._id)

    Card(
        modifier = modifier
            .width(100.dp)  // Tamaño base adaptable
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ImagenDesdeUrl(libro)

            if (token != null) {
                IconButton(
                    onClick = {
                        onFavoritoClick(!esFavorito)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (esFavorito) Color.Red else Color.Gray
                    )
                }
            }
            // Título (truncado si es muy largo)
            Text(
                text = libro.titulo ?: "Sin título",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Precio con moneda
            Text(
                text = "${libro.precio ?: "N/A"} ${libro.moneda ?: ""}",
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}


