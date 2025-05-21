package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.TipoStock
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Utils.Utils
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel

@Composable
expect fun ImagenLibroDetails(url: String?, contentDescription: String?, modifier: Modifier)

@Composable
fun LibroDetailScreen(
    navController: Navegator,
    authViewModel: AuthViewModel,
    librosViewModel: LibrosViewModel,
    carritoViewModel: CarritoViewModel
) {
    val librosSugeridos by librosViewModel.librosSugeridosCategorias.collectAsState()
    val librosSelected by librosViewModel.libroSelected.collectAsState()
    var rating by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    LaunchedEffect(librosSelected?.categorias) {
        librosSelected?.categorias?.firstOrNull()?.let { categoria ->
            librosViewModel.getLibrosByCategorias(categoria.replace("³", "").replace("+", " "))
        }
    }

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
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(AppColors.silver)
        ) {
            // Imagen con proporción ajustada para no deformar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.65f)
                    .background(AppColors.white, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .shadow(6.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentAlignment = Alignment.Center
            ) {
                ImagenLibroDetails(
                    url = librosSelected?.imagen,
                    contentDescription = librosSelected?.titulo,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(500.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )

                librosSelected?.stock?.tipo?.let { estado ->
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                color = when (estado) {
                                    TipoStock.EN_STOCK -> AppColors.success.copy(alpha = 0.85f)
                                    TipoStock.PREVENTA -> AppColors.warning.copy(alpha = 0.85f)
                                    TipoStock.AGOTADO -> AppColors.error.copy(alpha = 0.85f)
                                    else -> AppColors.primary.copy(alpha = 0.85f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = estado.toString(),
                            color = AppColors.white,
                            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = librosSelected?.titulo?.replace("+", " ") ?: "Sin título",
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                        color = AppColors.black,
                        modifier = Modifier.weight(0.75f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${librosSelected?.precio?.toString() ?: "N/A"} ${librosSelected?.moneda ?: ""}",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                librosSelected?.autores.takeIf { it?.isNotEmpty() == true }?.let { autores ->
                    Text(
                        text = autores.joinToString(", ").replace("+", " "),
                        style = MaterialTheme.typography.subtitle2.copy(color = AppColors.grey)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = {
                        carritoViewModel.agregarLibro(
                            Utils.castearLibroToLibroDTO(
                                librosSelected ?: throw Exception("No se puede añadir un libro no existente.")
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.primary,
                        contentColor = AppColors.white
                    ),
                    enabled = librosSelected?.stock?.tipo != TipoStock.AGOTADO
                ) {
                    Text(
                        "Añadir a la cesta",
                        style = MaterialTheme.typography.button.copy(fontSize = 16.sp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = librosSelected?.descripcion?.replace("+", " ") ?: "No hay descripción disponible",
                    style = MaterialTheme.typography.body2.copy(color = AppColors.darkGrey),
                    lineHeight = 20.sp
                )

                Divider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 40.dp)
                    ,
                    color = AppColors.primary,
                    thickness = 2.dp
                )

                librosSelected?.categorias.takeIf { it?.isNotEmpty() == true }?.let { categorias ->
                    Text(
                        text = "Te interesan estas categorías...",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .height(250.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(librosSugeridos) { libro ->
                            LibroSugeridoItem(
                                libro = libro,
                                onClick = {
                                    librosViewModel.putLibroSelected(libro)
                                    navController.navigateTo(AppRoutes.LibroDetalles)
                                }
                            )
                        }
                    }
                }

                Divider(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp)
                    ,
                    color = AppColors.primary,
                    thickness = 2.dp
                )

                RatingComentario(
                    rating = rating,
                    onRatingChanged = { rating = it },
                    comentario = comentario,
                    onComentarioChanged = { comentario = it },
                    libroSelected = librosSelected,

                )
            }
        }
    }
}

@Composable
fun LibroSugeridoItem(libro: Libro, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .width(200.dp)
            .padding(5.dp)
            .shadow(2.dp)
    ){
        Column(
            modifier = Modifier
                .width(130.dp)
                .clickable(onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenLibroDetails(
                url = libro.imagen,
                contentDescription = libro.titulo,
                modifier = Modifier
                    .size(width = 130.dp, height = 180.dp)
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = libro.titulo?.replace("+", " ") ?: "",
                style = MaterialTheme.typography.caption.copy(color = AppColors.black),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )

            Text(
                text = "${libro.precio?.toString() ?: ""} ${libro.moneda ?: ""}",
                style = MaterialTheme.typography.caption.copy(
                    color = AppColors.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )
        }
    }
}

@Composable
fun RatingComentario(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    comentario: String,
    onComentarioChanged: (String) -> Unit,
    libroSelected:Libro?
) {
    var enableComentar by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Califica el producto",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            color = AppColors.black
        )

        Row {
            for (i in 1..5) {
                IconButton(onClick = { onRatingChanged(i) }) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.Star,
                        contentDescription = "Estrella $i",
                        tint = if (i <= rating) AppColors.warning else AppColors.grey,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = comentario,
            onValueChange = onComentarioChanged,
            label = { Text("Comentario") },
            placeholder = { Text("Escribe tu opinión...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = AppColors.white,
                focusedIndicatorColor = AppColors.primary,
                unfocusedIndicatorColor = AppColors.greyBlue,
                cursorColor = AppColors.primary,
                textColor = AppColors.black,
                placeholderColor = AppColors.grey,
                disabledLabelColor = AppColors.primary,
                disabledPlaceholderColor = AppColors.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (rating!=0 && comentario.length >= 4){
            enableComentar = true
        }else{
            enableComentar = false
        }

        Button(
            onClick = {

            } ,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.primary,
                contentColor = AppColors.white
            ),
            //TODO Check compra
            enabled = enableComentar
        ){
            Text("Enviar", style = MaterialTheme.typography.button.copy(fontSize = 16.sp))
        }


        Spacer(Modifier.height(10.dp))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = Modifier
                .height(250.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(librosSugeridos) { libro ->
                LibroSugeridoItem(
                    libro = libro,
                    onClick = {
                        librosViewModel.putLibroSelected(libro)
                        navController.navigateTo(AppRoutes.LibroDetalles)
                    }
                )
            }
        }

    }
}
