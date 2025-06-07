package org.example.projects.Screens.LibrosMostrar

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.TipoStock.AGOTADO
import org.example.projects.BaseDeDatos.model.TipoStock.DISPONIBLE
import org.example.projects.BaseDeDatos.model.TipoStock.PREVENTA
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.ViewModel.*

@Composable
expect fun ImagenDesdeUrl(libro: Libro, modifier: Modifier = Modifier)

@Composable
fun TarjetaLibro(
    libro: Libro,
    modifier: Modifier = Modifier,
    onFavoritoClick: (Boolean) -> Unit,
    librosFavoritos: List<String>,
    librosViewModel: LibrosViewModel,
    navController: Navegator
) {
    val esFavorito = librosFavoritos.contains(libro._id)

    LaunchedEffect(libro._id) {
        librosViewModel.fetchValoraciones(libro._id!!)
    }

    Box(
        modifier = modifier
            .width(110.dp)
            .height(230.dp)
            .padding(6.dp)
    ) {
        // Card principal
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    librosViewModel.putLibroSelected(libro)
                    navController.navigateTo(AppRoutes.LibroDetalles)
                },
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Imagen
                ImagenDesdeUrl(
                    libro = libro,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(90.dp)
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                // Botón favorito
                IconButton(
                    onClick = { onFavoritoClick(!esFavorito) },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(26.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (esFavorito) Color.Red else Color.Gray
                    )
                }

                // Título
                Text(
                    text = libro.titulo ?: "Sin título",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Precio
                Text(
                    text = "${libro.precio ?: "N/A"} ${libro.moneda ?: ""}",
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        // Circulito de stock arriba a la derecha
        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-6).dp, y = 6.dp)
                .background(
                    color = when (libro.stock.tipo) {
                        DISPONIBLE -> AppColors.primary
                        AGOTADO -> AppColors.error
                        PREVENTA -> AppColors.warning
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (libro.stock.tipo) {
                    DISPONIBLE -> "S"
                    AGOTADO -> "X"
                    PREVENTA -> "P"
                },
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Valoración media justo al lado arriba izquierda
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 5.dp)
                .offset(x = (6).dp, y = (-6).dp)
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = libro.valoracionMedia.toString() + " ⭐",
                fontSize = 11.sp,
                color = AppColors.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
