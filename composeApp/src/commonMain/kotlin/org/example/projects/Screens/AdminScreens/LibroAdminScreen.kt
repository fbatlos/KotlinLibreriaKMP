package org.example.projects.Screens.AdminScreens

import AppColors
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.BaseDeDatos.model.Stock
import org.example.projects.BaseDeDatos.model.TipoStock
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.LibrosMostrar.ImagenDesdeUrl
import org.example.projects.ViewModel.AdminViewModel.LibrosAdminViewModel
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.InicioViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

@Composable
fun LibrosAdminScreen(
    librosViewModel: LibrosViewModel,
    navController: Navegator,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    uiViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel,
    librosAdminViewModel: LibrosAdminViewModel
) {
    val libros by librosViewModel.libros.collectAsState(emptyList())
    val query by librosViewModel.query.collectAsState()

    val showDialogError by uiViewModel.showDialog.collectAsState()
    val textError by uiViewModel.textError.collectAsState()
    val isLoading by uiViewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var libroToDelete by remember { mutableStateOf<Libro?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var libroToEdit by remember { mutableStateOf<Libro?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(libros.isEmpty()) {
        librosViewModel.fetchLibros()
    }

    val filteredLibros by remember {
        derivedStateOf {
            libros.filter { libro ->
                val coincideBusqueda = query.isEmpty() || libro.titulo!!.contains(query, ignoreCase = true) ||
                        libro.autores.any { it.contains(query, ignoreCase = true) } ||
                        libro.categorias.any { it.contains(query, ignoreCase = true) } ||
                        libro.isbn13!!.contains(query, ignoreCase = true)

                coincideBusqueda
            }
        }
    }
    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = {query -> librosViewModel.filtrarLibros(query)},
                onSearchClick = {},
                authViewModel = authViewModel,
                navController = navController,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(drawerState, navController, uiViewModel, authViewModel, sharedViewModel)
        }
    ) { padding ->
        if (filteredLibros.isEmpty() && isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AppColors.primary,
                    strokeWidth = 2.dp
                )
            }
        }else if(filteredLibros.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No se encontraron resultados",
                    color = AppColors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }else {
            Box(modifier = Modifier.fillMaxSize().background(AppColors.white)) {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredLibros) { libro ->
                        LibroCard(
                            libro = libro,
                            onEdit = {
                                libroToEdit = libro
                                showEditDialog = true
                            },
                            onDelete = {
                                libroToDelete = libro
                                showDialog = true
                                showDeleteDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    backgroundColor = AppColors.primary,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Añadir libro",
                        tint = AppColors.white
                    )
                }
            }
        }

        if (showDeleteDialog) {
            libroToDelete?.let { libro ->
                DeleteDialog(
                    title = "Eliminar libro",
                    message = "¿Seguro que quieres eliminar '${libro.titulo}'?",
                    onConfirm = {
                        showDialog = false
                        librosAdminViewModel.deleteLibro(libro._id!!)
                        showDeleteDialog = false
                    },
                    onDismiss = {
                        showDialog = false
                        showDeleteDialog = false
                    }
                )
            }
        }

        if (showEditDialog) {
            libroToEdit?.let { libro ->
                LibroEditDialog(
                    libros = libros,
                    libro = libro,
                    onSave = { updatedLibro ->
                        showEditDialog = false
                        librosAdminViewModel.updateLibro(libro._id!!, updatedLibro)
                    },
                    onDismiss = { showEditDialog = false }
                )
            }
        }

        if (showAddDialog) {
            LibroEditDialog(
                libros = libros,
                libro = Libro(
                    _id = null,
                    titulo = "",
                    descripcion = "",
                    autores = emptyList(),
                    precio = 0.0,
                    moneda = "EUR",
                    imagen = "https://tse1.mm.bing.net/th/id/OIP.tGynLioAEJgfNfvAgXieGQHaK5?rs=1&pid=ImgDetMain",
                    enlaceEbook = "",
                    isbn13 = "",
                    valoracionMedia = 0.0,
                    stock = Stock(TipoStock.AGOTADO, 0)
                ),
                onSave = { newLibro ->
                    showAddDialog = false
                    librosAdminViewModel.addLibro(newLibro)
                },
                onDismiss = { showAddDialog = false }
            )
        }
    }

    if (showDialogError) {
        ErrorDialog(
            textError = textError ?: "Error desconocido",
            onDismiss = { uiViewModel.setShowDialog(false) }
        )
    }
}

@Composable
fun LibroCard(
    libro: Libro,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = AppColors.white
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Imagen del libro
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    ImagenDesdeUrl(libro)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información principal
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = libro.titulo ?: "Sin título",
                        style = MaterialTheme.typography.h6,
                        color = AppColors.black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${libro.precio} ${libro.moneda}",
                            style = MaterialTheme.typography.subtitle1,
                            color = AppColors.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        // Indicador de stock
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (libro.stock.tipo) {
                                        TipoStock.DISPONIBLE -> AppColors.primary.copy(alpha = 0.2f)
                                        TipoStock.AGOTADO -> AppColors.error.copy(alpha = 0.2f)
                                        TipoStock.PREVENTA -> AppColors.warning.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${libro.stock.numero} ${libro.stock.tipo.name.lowercase()
                                    .replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.caption,
                                color = AppColors.black.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Botones de acción
                Column {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = AppColors.primary
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = AppColors.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            Text(
                text = libro.descripcion ?: "Sin descripción",
                style = MaterialTheme.typography.body1,
                color = AppColors.black.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Autores y categorías
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.primary.copy(alpha = 0.05f))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Autores: ${libro.autores.joinToString(", ").takeIf { it.isNotEmpty() } ?: "No especificado"}",
                    style = MaterialTheme.typography.body2,
                    color = AppColors.black.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Categorías: ${libro.categorias.joinToString(", ").takeIf { it.isNotEmpty() } ?: "No especificado"}",
                    style = MaterialTheme.typography.body2,
                    color = AppColors.black.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ISBN: ${libro.isbn13 ?: "No especificado"}",
                    style = MaterialTheme.typography.body2,
                    color = AppColors.black.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun LibroEditDialog(
    libros:List<Libro>,
    libro: Libro,
    onSave: (Libro) -> Unit,
    onDismiss: () -> Unit
) {
    var currentLibro by remember { mutableStateOf(libro.copy()) }
    var selectedTipoStock by remember { mutableStateOf(currentLibro.stock.tipo) }
    var stockNumero by remember { mutableStateOf(currentLibro.stock.numero.toString()) }
    var showCategoriasDropdown by remember { mutableStateOf(false) }
    val allCategorias by remember {
        derivedStateOf {
            libros.flatMap { it.categorias }.distinct().toMutableList()
        }
    }
    val selectedCategorias = remember { mutableStateListOf<String>().apply { addAll(libro.categorias) } }
    var newCategoria by remember { mutableStateOf("") }
    var showAddCategoriaDialog by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp,
            color = AppColors.white
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (libro._id.isNullOrEmpty()) "Añadir Libro" else "Editar Libro",
                        style = MaterialTheme.typography.h5,
                        color = AppColors.primary,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = AppColors.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campos del formulario
                OutlinedTextField(
                    value = currentLibro.titulo ?: "",
                    onValueChange = { currentLibro = currentLibro.copy(titulo = it) },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.primary,
                        cursorColor = AppColors.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = currentLibro.descripcion ?: "",
                    onValueChange = { currentLibro = currentLibro.copy(descripcion = it) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.primary,
                        cursorColor = AppColors.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = currentLibro.isbn13 ?: "",
                    onValueChange = { currentLibro = currentLibro.copy(isbn13 = it) },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.primary,
                        cursorColor = AppColors.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                AutoresField(
                    autores = currentLibro.autores,
                    onAutoresChange = { currentLibro = currentLibro.copy(autores = it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentLibro.precio.toString(),
                        onValueChange = {
                            currentLibro = currentLibro.copy(precio = it.toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Precio") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.primary,
                            cursorColor = AppColors.primary
                        )
                    )

                    OutlinedTextField(
                        value = currentLibro.moneda!!,
                        onValueChange = { currentLibro = currentLibro.copy(moneda = it) },
                        label = { Text("Moneda") },
                        modifier = Modifier.width(80.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.primary,
                            cursorColor = AppColors.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Categorías
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Chips de categorías seleccionadas
                if (selectedCategorias.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedCategorias.forEach { categoria ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = AppColors.primary.copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, AppColors.primary.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .clickable { selectedCategorias.remove(categoria) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = categoria,
                                        color = AppColors.primary,
                                        style = MaterialTheme.typography.body2
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Quitar",
                                        tint = AppColors.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Botones para gestionar categorías
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showAddCategoriaDialog = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.primary.copy(alpha = 0.1f),
                            contentColor = AppColors.primary
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Añadir categoría",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Añadir")
                    }

                    Button(
                        onClick = { showCategoriasDropdown = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.primary.copy(alpha = 0.1f),
                            contentColor = AppColors.primary
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "Seleccionar categorías",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Seleccionar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Stock
                Text(
                    text = "Stock",
                    style = MaterialTheme.typography.subtitle1,
                    color = AppColors.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selector de tipo de stock
                    var showTipoStockDropdown by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, AppColors.primary.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTipoStockDropdown = true }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedTipoStock.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                color = AppColors.black
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Seleccionar tipo de stock",
                                tint = AppColors.primary
                            )
                        }

                        DropdownMenu(
                            expanded = showTipoStockDropdown,
                            onDismissRequest = { showTipoStockDropdown = false }
                        ) {
                            TipoStock.values().forEach { tipo ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedTipoStock = tipo
                                        showTipoStockDropdown = false
                                    }
                                ) {
                                    Text(tipo.name.lowercase().replaceFirstChar { it.uppercase() })
                                }
                            }
                        }
                    }

                    // Cantidad de stock
                    OutlinedTextField(
                        value = stockNumero,
                        onValueChange = {
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                stockNumero = it
                            }
                        },
                        label = { Text("Cantidad") },
                        modifier = Modifier.width(100.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.primary,
                            cursorColor = AppColors.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.white,
                            contentColor = AppColors.primary
                        ),
                        border = BorderStroke(1.dp, AppColors.primary),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            println(stockNumero)
                            currentLibro = currentLibro.copy(
                                categorias = selectedCategorias,
                                stock = Stock(
                                    tipo = selectedTipoStock,
                                    numero = stockNumero.toIntOrNull() ?: 0
                                )
                            )
                            println(currentLibro)
                            onSave(currentLibro)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.primary,
                            contentColor = AppColors.white
                        )
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }

        // Diálogo para añadir nueva categoría
        if (showAddCategoriaDialog) {
            AlertDialog(
                onDismissRequest = { showAddCategoriaDialog = false },
                title = { Text("Añadir nueva categoría") },
                text = {
                    OutlinedTextField(
                        value = newCategoria,
                        onValueChange = { newCategoria = it },
                        label = { Text("Nombre de la categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = AppColors.primary,
                            cursorColor = AppColors.primary
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newCategoria.isNotBlank() && !allCategorias.contains(newCategoria)) {
                                allCategorias.add(newCategoria)
                                newCategoria = ""
                            }
                            showAddCategoriaDialog = false
                        },
                        enabled = newCategoria.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColors.primary,
                            contentColor = AppColors.white
                        )
                    ) {
                        Text("Añadir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddCategoriaDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = AppColors.primary
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Diálogo para seleccionar categorías existentes
        if (showCategoriasDropdown) {
            Dialog(
                onDismissRequest = { showCategoriasDropdown = false },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(0.9f).background(AppColors.white),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            allCategorias.forEach { categoria ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!selectedCategorias.contains(categoria)) {
                                                selectedCategorias.add(categoria)
                                                println(selectedCategorias)
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedCategorias.contains(categoria),
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedCategorias.add(categoria)
                                            } else {
                                                selectedCategorias.remove(categoria)
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = AppColors.primary,
                                            uncheckedColor = AppColors.primary.copy(alpha = 0.6f)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(categoria, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                        item {
                            Button(
                                onClick = { showCategoriasDropdown = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = AppColors.primary,
                                    contentColor = AppColors.white
                                )
                            ) {
                                Text("Aceptar")
                            }
                        }
                    }
                }
            )
        }
}

@Composable
fun AutoresField(
    autores: List<String>,
    onAutoresChange: (List<String>) -> Unit
) {
    var newAutor by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Autores",
            style = MaterialTheme.typography.subtitle1,
            color = AppColors.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (autores.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.primary.copy(alpha = 0.05f))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                autores.forEach { autor ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = autor,
                            modifier = Modifier.weight(1f),
                            color = AppColors.black
                        )
                        IconButton(
                            onClick = { onAutoresChange(autores - autor) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar",
                                tint = AppColors.error
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newAutor,
                onValueChange = { newAutor = it },
                label = { Text("Añadir autor") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AppColors.primary,
                    cursorColor = AppColors.primary
                )
            )
            Button(
                onClick = {
                    if (newAutor.isNotBlank()) {
                        onAutoresChange(autores + newAutor)
                        newAutor = ""
                    }
                },
                enabled = newAutor.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppColors.primary,
                    disabledBackgroundColor = AppColors.primary.copy(alpha = 0.3f)
                ),
                elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Text("Añadir", color = AppColors.white)
            }
        }
    }
}

@Composable
fun DeleteDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = AppColors.error) },
        text = { Text(message, color = AppColors.black) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.error),
                elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Text("Eliminar", color = AppColors.white)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = AppColors.primary)
            ) {
                Text("Cancelar")
            }
        },
        backgroundColor = AppColors.white
    )
}