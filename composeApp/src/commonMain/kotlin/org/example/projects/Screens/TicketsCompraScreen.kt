package org.example.projects.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.BaseDeDatos.model.ItemCompra
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.*
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.LibrosMostrar.ImagenDesdeUrl
import org.example.projects.Utils.Utils
import org.example.projects.ViewModel.*

@Composable
fun TicketCompraScreen(
    navController: Navegator,
    carritoViewModel: CarritoViewModel,
    authViewModel:AuthViewModel,
    uiViewModel:UiStateViewModel,
    sharedViewModel: SharedViewModel,
    librosViewModel: LibrosViewModel
) {
    val tickets by carritoViewModel.tickets.collectAsState() // Asegúrate de tener el StateFlow en tu ViewModel
    val isLoading by uiViewModel.isLoading.collectAsState()
    val libros by librosViewModel.libros.collectAsState()
    val token = sharedViewModel.token.collectAsState().value

    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (!navigated) {
            uiViewModel.setLoading(true)

            carritoViewModel.getTicketsCompra()
            navigated = true

            uiViewModel.setLoading(false)
        }
    }
    var ticketSeleccionado by remember { mutableStateOf<Compra?>(null) }


    LayoutPrincipal(
        headerContent = { drawerState,scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { },
                onSearchClick = {},
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = {drawerState->
            MenuBurger(drawerState,navController, uiViewModel,sharedViewModel)
        }
    ) {
        paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.primary)
                }
            }

            tickets.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Aún no tienes compras registradas.",
                            style = MaterialTheme.typography.h6,
                            color = AppColors.darkGrey
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigateTo(AppRoutes.LibroLista) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = AppColors.primary,
                                contentColor = AppColors.white
                            )
                        ) {
                            Text("Ir al catálogo")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    items(tickets) { compra ->
                        val sumaCompra = compra.items.sumOf { item ->
                            val libro = libros.find { it._id == item.libro._id }
                            (libro?.precio ?: 0.0) * item.cantidad
                        }

                        TicketCard(
                            compra = compra,
                            sumaCompra = sumaCompra,
                            onClick = { ticketSeleccionado = if (ticketSeleccionado == compra) null else compra }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (ticketSeleccionado == compra) {
                            Card(
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 20.dp)
                                    .shadow(2.dp)
                            ) {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    try {
                                        items(compra.items) { item ->
                                            val libro = libros.find { it._id == item.libro._id }
                                            if (libro != null) {
                                                LibroCompraItem(libro, item)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        uiViewModel.setTextError("No se encontró el libro.")
                                        uiViewModel.setShowDialog(true)
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun TicketCard(compra: Compra, sumaCompra: Double,onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = AppColors.white,
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    "Usuario: ${compra.usuarioName}",
                    color = AppColors.primary,
                    style = MaterialTheme.typography.h6
                )

                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = String.format("%.2f", sumaCompra) + " €",
                    color = AppColors.primary,
                    style = MaterialTheme.typography.h6
                )

            }
            Text("Fecha: ${Utils.formatearFecha(compra.fechaCompra)}", color = AppColors.grey, style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = AppColors.greyBlue, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            compra.items.forEach { item ->
                Text("${item.cantidad} × ${item.libro.titulo}", color = AppColors.darkGrey)
            }
        }
    }
}

@Composable
fun LibroCompraItem(libro:Libro ,itemCompra: ItemCompra) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
    ) {
        ImagenDesdeUrl(
            libro = libro,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(90.dp)
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = libro.titulo ?: "",
            style = MaterialTheme.typography.body2,
            color = AppColors.primary,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = String.format("%.2f", ((libro.precio?.times(itemCompra.cantidad)))) + "€",
            style = MaterialTheme.typography.body2,
            color = AppColors.primary,
            maxLines = 2
        )

    }
}


