package org.example.projects.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.AppRoutes.LibroLista.route
import org.example.projects.Utils.LibroSerializer.toLibro
import java.awt.SystemColor.text
import java.net.URLDecoder

@Composable
expect fun ImagenLibroDetails(url: String?, contentDescription: String?,modifier: Modifier )

@Composable
fun LibroDetailScreen(libro: Libro,onBackPressed: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        ImagenLibroDetails(
            url = libro.imagen,
            contentDescription = libro.titulo,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = libro.titulo ?: "Sin título",
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Precio
        Text(
            text = "${libro.precio?.toString() ?: "N/A"} ${libro.moneda ?: ""}",
            style = MaterialTheme.typography.h3,
            color = Color.Green
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Autores
        libro.autores.takeIf { it.isNotEmpty() }?.let { autores ->
            Text(
                text = autores.joinToString(", "),
                style = MaterialTheme.typography.subtitle2,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Descripción
        Text(
            text = libro.descripcion ?: "No hay descripción disponible",
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categorías
        libro.categorias.takeIf { it.isNotEmpty() }?.let { categorias ->
            Text(
                text = "Te interesan estas categorías...",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
    }
}