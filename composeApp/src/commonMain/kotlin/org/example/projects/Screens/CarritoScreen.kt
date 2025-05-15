package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.es.aplicacion.dto.LibroDTO
import kotlinx.coroutines.FlowPreview
import org.example.projects.BaseDeDatos.model.Compra
import org.example.projects.NavController.AppRoutes
import org.example.projects.ViewModel.*

fun obtenerLibro(idLibro:String? , libros:List<Libro>):Libro = libros.filter { it._id == idLibro }.first()
@Composable
fun CarritoScreen(
    navController: Navegator,
    uiStateViewModel:UiStateViewModel,
    authViewModel: AuthViewModel,
    librosViewModel:LibrosViewModel,
    sharedViewModel:SharedViewModel,

    carritoViewModel: CarritoViewModel
) {
    val items by carritoViewModel.items.collectAsState()
    val total by carritoViewModel.total.collectAsState()
    val libros by librosViewModel.libros.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    val isLoading by uiStateViewModel.isLoading.collectAsState()
    var libroToDelete by remember { mutableStateOf<LibroDTO?>(null) }

    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = {},
                onSearchClick = {},
                authViewModel = authViewModel,
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(drawerState, navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.silver)
        ) {
            if (items.isEmpty()) {
                EmptyCartView(navController)
            } else {
                // Lista de items
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(items.toList()) { (libroDto,cantidad) ->
                        CartItem(
                            libro = libroDto,
                            libros = libros,
                            cantidad = cantidad,
                            onRemove = {
                                libroToDelete = libroDto
                                showDeleteDialog = true
                            },
                            onQuantityChange = { nuevaCantidad ->
                                carritoViewModel.actualizarCantidad(libroDto, nuevaCantidad)
                            }
                        )
                        Divider(color = AppColors.greyBlue, thickness = 0.5.dp)
                    }
                }

                // Resumen de compra
                ResumenCompra(total = total, onCheckout = {
                    // Lógica para proceder al pago
                    if (sharedViewModel.token.value != null){
                        carritoViewModel.checkout(Compra(authViewModel.username.value!!,items),sharedViewModel.token.value!!)
                    }else{
                        navController.navigateTo(AppRoutes.Login)
                    }
                })
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar artículo") },
            text = { Text("¿Eliminar este libro de tu carrito?") },
            confirmButton = {
                Button(
                    onClick = {
                        libroToDelete?.let { carritoViewModel.eliminarLibro(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.error,
                        contentColor = AppColors.white
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppColors.primary
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ResumenCompra(total: Double, onCheckout: () -> Unit) {
    Surface(
        color = AppColors.white,
        elevation = 8.dp,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Resumen del pedido",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal:")
                Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Envío:")
                Text(
                    text = if (total > 50) "Gratis" else "$5.99",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = AppColors.greyBlue)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.h6)
                Text(
                    text = "$${"%.2f".format(if (total > 50) total else total + 5.99)}",
                    style = MaterialTheme.typography.h6,
                    color = AppColors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColors.primary,
                    contentColor = AppColors.white
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Proceder al pago", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CartItem(
    libro: LibroDTO,
    libros: List<Libro>,
    cantidad: Int,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val libroDetalles = obtenerLibro(idLibro = libro._id,libros)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(AppColors.white, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Imagen del libro
        ImagenLibroDetails(
            url = libroDetalles.imagen,
            contentDescription = libro.titulo,
            modifier = Modifier
                .width(80.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = libro.titulo?.replace("+", " ") ?: "Libro sin título",
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            libroDetalles.autores.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = it.joinToString(", ").replace("+", " "),
                    color = AppColors.grey
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${"%.2f".format((libro.precio?.toDouble() ?: 0.0) * cantidad)}",
                color = AppColors.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            QuantitySelector(
                cantidad = cantidad,
                onIncrement = { onQuantityChange(cantidad + 1) },
                onDecrement = { if (cantidad > 1) onQuantityChange(cantidad - 1) },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = AppColors.error
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(
    cantidad: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(1.dp, AppColors.greyBlue, RoundedCornerShape(4.dp))
            .height(36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDecrement,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Decrementar")
        }

        Text(
            text = "$cantidad",
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center,
        )

        IconButton(
            onClick = onIncrement,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Aumentar")
        }
    }
}


@Composable
private fun EmptyCartView(navController: Navegator) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito vacío",
            modifier = Modifier.size(80.dp),
            tint = AppColors.lightGrey
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu carrito de MostraLibros está vacío",
            style = MaterialTheme.typography.h6,
            color = AppColors.darkGrey,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explora nuestros libros y encuentra tu próxima lectura favorita",
            style = MaterialTheme.typography.body1,
            color = AppColors.grey,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigateTo(AppRoutes.LibroLista) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.primary,
                contentColor = AppColors.grey
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Ver catálogo")
        }
    }
}