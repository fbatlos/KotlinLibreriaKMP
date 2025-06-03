package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.*
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.TipoStock
import org.example.projects.BaseDeDatos.model.Valoracion
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.LibrosMostrar.TarjetaLibro
import org.example.projects.ViewModel.UiStateViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.*

//TODO LOS FILTROS NO SON DEL MISMO TAMAÑO
@Composable
fun LibrosScreen(
    navController: Navegator,
    uiStateViewModel: UiStateViewModel,
    authViewModel: AuthViewModel,
    librosViewModel: LibrosViewModel,
    carritoViewModel: CarritoViewModel,
    sharedViewModel: SharedViewModel
) {
    val isLoading by uiStateViewModel.isLoading.collectAsState()
    val textError by uiStateViewModel.textError.collectAsState()
    val showDialog by uiStateViewModel.showDialog.collectAsState()
    val allLibros by librosViewModel.libros.collectAsState() // Todos los libros
    val query by librosViewModel.query.collectAsState()
    librosViewModel.filtrarLibros("")
    val librosFavoritos by librosViewModel.librosFavoritos.collectAsState()

    var tipoStockFiltro by remember { mutableStateOf<TipoStock?>(null) }
    var categoriaFiltro by remember { mutableStateOf<String?>(null) }

    val categoriasDisponibles by remember {
        derivedStateOf {
            allLibros.flatMap { it.categorias }.distinct()
        }
    }

    var precioMinFiltro by remember { mutableStateOf<Double?>(null) }
    var precioMaxFiltro by remember { mutableStateOf<Double?>(null) }


    val filteredLibros by remember {
        derivedStateOf {
            allLibros.filter { libro ->
                val coincideBusqueda = query.isEmpty() || libro.titulo!!.contains(query, ignoreCase = true) ||
                        libro.autores.any { it.contains(query, ignoreCase = true) } ||
                        libro.categorias.any { it.contains(query, ignoreCase = true) } ||
                        libro.isbn13!!.contains(query, ignoreCase = true)

                val coincideStock = tipoStockFiltro == null || libro.stock.tipo == tipoStockFiltro
                val coincideCategoria = categoriaFiltro == null || libro.categorias.contains(categoriaFiltro)
                val coincidePrecioMin = precioMinFiltro == null || (libro.precio ?: 0.0) >= precioMinFiltro!!
                val coincidePrecioMax = precioMaxFiltro == null || (libro.precio ?: 0.0) <= precioMaxFiltro!!

                coincideBusqueda && coincideStock && coincideCategoria && coincidePrecioMin && coincidePrecioMax
            }
        }
    }

    if (allLibros.isEmpty()){
        librosViewModel.fetchLibros()
    }
    if (librosFavoritos.isEmpty()){
        librosViewModel.loadFavoritos()
    }

    LayoutPrincipal(
        headerContent = { drawerState,scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { query -> librosViewModel.filtrarLibros(query) },
                onSearchClick = {},
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = {drawerState->
            MenuBurger(drawerState,navController, uiStateViewModel,sharedViewModel = sharedViewModel)
        }
    ) {paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            FiltrarLibros(
                tipoStockFiltro = tipoStockFiltro,
                onSelectedTipoStock = { tipoStockFiltro = it },
                precioMinFiltro = precioMinFiltro,
                addPrecioMin = { precioMinFiltro = it.toDoubleOrNull() },
                precioMaxFiltro = precioMaxFiltro,
                addPrecioMax = { precioMaxFiltro = it.toDoubleOrNull() },
                categoriaFiltro = categoriaFiltro,
                categoriasDisponibles = categoriasDisponibles,
                onSelectedCategoria = { categoriaFiltro = it }
            )
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading || allLibros.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AppColors.primary
                    )
                }
                else if(filteredLibros.isEmpty() && (tipoStockFiltro != null || categoriaFiltro != null || precioMaxFiltro != null || precioMinFiltro != null)){
                    Text(
                        text = "No se encontraron resultados",
                        color = AppColors.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else {
                    LibrosGrid(
                        libros = filteredLibros,
                        librosViewModel = librosViewModel,
                        uiStateViewModel = uiStateViewModel,
                        textError = if (filteredLibros.isEmpty() && query.isNotEmpty()) "No se encontraron resultados" else textError,
                        navigator = navController
                    )
                }

                if (showDialog) {
                    ErrorDialog(textError = textError) {
                        uiStateViewModel.setShowDialog(it)
                    }
                }
            }
        }
    }
}

@Composable
fun LibrosGrid(libros: List<Libro>,librosViewModel: LibrosViewModel,uiStateViewModel: UiStateViewModel,textError: String, navigator: Navegator) {

    val librosFavoritos by librosViewModel.librosFavoritos.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (libros.isNullOrEmpty()) {
            Text(
                text = textError,
                color = AppColors.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(libros) { libro ->
                    TarjetaLibro(
                        libro = libro,
                        onFavoritoClick = { add -> cambiarListaFavoritos(add, librosViewModel, libro._id) },
                        librosFavoritos = librosFavoritos,
                        librosViewModel = librosViewModel,
                        navController = navigator
                    )
                }
            }
        }
        uiStateViewModel.setLoading(false)
    }
}

@Composable
fun FiltrarLibros(
    tipoStockFiltro: TipoStock?,
    onSelectedTipoStock: (TipoStock?) -> Unit,
    categoriaFiltro: String?,
    categoriasDisponibles: List<String>,
    onSelectedCategoria: (String?) -> Unit,
    precioMinFiltro: Double?,
    addPrecioMin: (String) -> Unit,
    precioMaxFiltro: Double?,
    addPrecioMax: (String) -> Unit
) {
    var minPrecioText by remember { mutableStateOf(precioMinFiltro?.toString() ?: "") }
    var maxPrecioText by remember { mutableStateOf(precioMaxFiltro?.toString() ?: "") }

    var errorPrecio by remember { mutableStateOf("") }


    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            FiltroTipoStock(tipoStockFiltro, onSelected = onSelectedTipoStock)
        }

        item {
            FiltroCategoria(categoriaFiltro, categoriasDisponibles, onSelectedCategoria)
        }

        item {
            OutlinedTextField(
                value = minPrecioText,
                onValueChange = {
                    if (it.all { c -> c.isDigit() || c == '.' }) {
                        minPrecioText = it
                        addPrecioMin(it)
                    }
                },
                label = { Text("Min €", color = AppColors.primary) },
                modifier = Modifier.width(100.dp).height(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = errorPrecio.isNotEmpty(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AppColors.primary,
                    unfocusedBorderColor = AppColors.primary.copy(alpha = 0.6f),
                    cursorColor = AppColors.primary,
                    textColor = AppColors.primary,
                    errorBorderColor = MaterialTheme.colors.error
                ),
                textStyle = MaterialTheme.typography.body1.copy(color = AppColors.primary)
            )
        }

        item {
            OutlinedTextField(
                value = maxPrecioText,
                onValueChange = {
                    if (it.all { c -> c.isDigit() || c == '.' }) {
                        maxPrecioText = it
                        addPrecioMax(it)
                    }
                },
                label = { Text("Max €" , color = AppColors.primary) },
                modifier = Modifier.width(100.dp).height(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AppColors.primary,
                    unfocusedBorderColor = AppColors.primary.copy(alpha = 0.6f),
                    cursorColor = AppColors.primary,
                    textColor = AppColors.primary
                ),
                textStyle = MaterialTheme.typography.body1.copy(color = AppColors.primary)
            )
        }
    }

    if (minPrecioText.toDoubleOrNull() != null && maxPrecioText.toDoubleOrNull() != null &&
        minPrecioText.toDouble() > maxPrecioText.toDouble()) {
        Text(
            "El precio mínimo no puede ser mayor que el máximo",
            color = AppColors.error,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }

}





@Composable
fun FiltroTipoStock(selectedTipo: TipoStock?, onSelected:(TipoStock?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            modifier = Modifier.height(50.dp),
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.primary,
                contentColor = AppColors.white
        ),
        shape = RoundedCornerShape(12.dp)
        ) {
            Text(selectedTipo?.name ?: "Stock")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                onSelected(null)
                expanded = false
            }) { Text("Todos") }
            TipoStock.values().forEach { tipo ->
                DropdownMenuItem(onClick = {
                    onSelected(tipo)
                    expanded = false
                }) {
                    Text(tipo.name)
                }
            }
        }
    }
}

@Composable
fun FiltroCategoria(
    selectedCategoria: String?,
    categoriasDisponibles: List<String>,
    onSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            modifier = Modifier.height(50.dp),
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.primary,
                contentColor = AppColors.white
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(selectedCategoria ?: "Categoría")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                onSelected(null)
                expanded = false
            }) { Text("Todas") }

            categoriasDisponibles.distinct().forEach { categoria ->
                DropdownMenuItem(onClick = {
                    onSelected(categoria)
                    expanded = false
                }) {
                    Text(categoria)
                }
            }
        }
    }
}


fun cambiarListaFavoritos(add: Boolean, viewModel: LibrosViewModel, idLibro: String) {
    if (add) {
        viewModel.addLibroFavorito(idLibro)
    } else {
        viewModel.removeLibroFavorito(idLibro)
    }
    viewModel.updateFavoritos(add, idLibro)
}