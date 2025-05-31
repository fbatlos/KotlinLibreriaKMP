package org.example.projects.Screens.CommonParts

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.*

@Composable
fun LayoutPrincipal(
    headerContent: @Composable (DrawerState, CoroutineScope) -> Unit,
    drawerContent: @Composable (DrawerState) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent(drawerState) }
    ) {
        Scaffold(
            topBar = { headerContent(drawerState, scope) },
            content = content
        )
    }
}

@Composable
fun HeaderConHamburguesa(
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSearch: (String) -> Unit,
    navController: Navegator,
    authViewModel:AuthViewModel,
    carritoViewModel: CarritoViewModel,
    mostrarCarrito: Boolean = true
) {
    var query by remember { mutableStateOf("") }
    val cestaSize by carritoViewModel.cestaSize.collectAsState()

    //TODO AÑADIR CABEZERA DE TODAS LAS PESTAÑAS
    TopAppBar(
        title = {
            when (navController.getCurrentRoute()) {
                is AppRoutes.LibroLista -> {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            onSearch(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        placeholder = {
                            Text("Buscar libros...", color = AppColors.lightGrey.copy(alpha = 0.7f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = AppColors.lightGrey
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            backgroundColor = AppColors.primary.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                else -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "LeafRead",
                            color = AppColors.lightGrey,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.weight(1f))

                        IconButton(onClick = { navController.navigateTo(AppRoutes.LibroLista) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar libros",
                                tint = AppColors.lightGrey
                            )
                        }
                    }
                }
            }
        },
        backgroundColor = AppColors.primary,
        contentColor = Color.White,
        elevation = 4.dp,
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = AppColors.lightGrey
                )
            }
        },
        actions = {
            if (mostrarCarrito) {
                IconButton(onClick = {
                    println(AppRoutes.Carrito)
                    navController.navigateTo(AppRoutes.Carrito)
                } ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito" , tint = AppColors.lightGrey)
                }

                if (cestaSize > 0) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(AppColors.error),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (cestaSize > 9) "9+" else "${cestaSize}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MenuBurger(
    drawerState: DrawerState,
    navController: Navegator,
    uiViewModel:UiStateViewModel,
    sharedViewModel: SharedViewModel
){
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .background(MaterialTheme.colors.surface)
    ) {
        // Encabezado del menú
        Text(
            text = "LeafRead",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold,
                color = AppColors.primary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Menú Principal",
            style = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))

        // Items del menú
        val menuItems = listOf(
            "Inicio" to Icons.Default.Home,
            "Catálogo" to Icons.Outlined.ThumbUp,
            "Mi Perfil" to Icons.Default.Person,
            "Mis Reseñas" to Icons.Default.Edit,
            "Historial de pedidos" to Icons.Filled.MoreVert,
            "Ayuda" to Icons.Default.Info
        )

        menuItems.forEach { (item, icon) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch { drawerState.close() }

                        when (item) {
                            "Inicio" ->{
                                navController.navigateTo(AppRoutes.Inicio)
                                selected = "Inicio"
                            }

                            "Mi Perfil" -> {
                                if (sharedViewModel.token.value.isNullOrEmpty()) {
                                    //TODO hacer token para logearte o ver tu perfil real
                                    navController.navigateTo(AppRoutes.Login)
                                }else{
                                    navController.navigateTo(AppRoutes.MiPerfil)
                                }
                                selected = "Mi Perfil"
                            }
                            "Catálogo" -> {
                                navController.navigateTo(AppRoutes.LibroLista)
                                selected = "Catálogo"
                            }

                            "Mis Reseñas" ->{
                                if (sharedViewModel.token.value.isNullOrEmpty()) {
                                    uiViewModel.setTextError("Debes iniciar sesión para ver tus reseñas.")
                                    uiViewModel.setShowDialog(true)
                                    navController.navigateTo(AppRoutes.Login)
                                } else {
                                    navController.navigateTo(AppRoutes.MisValoraciones)
                                    selected = "Mis Reseñas"
                                }
                            }

                            "Historial de pedidos" -> {
                                if (sharedViewModel.token.value.isNullOrEmpty()) {
                                    uiViewModel.setTextError("Debes iniciar sesión para ver tus compras.")
                                    uiViewModel.setShowDialog(true)
                                    navController.navigateTo(AppRoutes.Login)
                                } else {
                                    navController.navigateTo(AppRoutes.HistorialCompra)
                                    selected = "Historial de pedidos"
                                }
                            }

                            //TODO implementar las demas ventanitas
                        }
                        println(selected)
                    }
                    .padding(vertical = 14.dp, horizontal = 8.dp)
                    .background(
                        if (item == selected) AppColors.primary.copy(alpha = 0.2f)
                        else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = item,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )

                if (item == "Catálogo") {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Nombre",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Francisco José Batista de los Santos",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}