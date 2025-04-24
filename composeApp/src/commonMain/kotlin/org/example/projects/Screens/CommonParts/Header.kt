package org.example.projects.Screens.CommonParts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator

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
    onCartClick: () -> Unit = {},
    onSearch: (String) -> Unit,
    navController: Navegator,
    mostrarCarrito: Boolean = true
) {
    var query by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            when(navController.getCurrentRoute() ) {

                is AppRoutes.LibroLista ->TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            onSearch(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        placeholder = {
                            Text(
                                "Buscar libros...",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.White
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            backgroundColor = Color(0xFF4CAF50).copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                )

                is AppRoutes.Login -> {}

                is AppRoutes.LibroDetail -> Row (modifier = Modifier.clickable{navController.popBackStack()}){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )

                    Text(text = "Volver")
                }
                else -> {}
            }
        },
        backgroundColor = Color(0xFF4CAF50),
        contentColor = Color.White,
        elevation = 4.dp,
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú"
                )
            }
        },
        actions = {
            // Icono de carrito (opcional)
            if (mostrarCarrito) {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            }
        }
    )
}

@Composable
fun MenuBurger(
    drawerState: DrawerState,
    navController: Navegator
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
            text = "MostraLibros",
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
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
            "Catálogo" to Icons.Default.Build,
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
                            "Mi Perfil" -> {
                                //TODO hacer token para logearte o ver tu perfil real
                                navController.navigateTo(AppRoutes.Login)
                                selected = "Mi Perfil"
                            }
                            "Inicio" -> {
                                navController.navigateTo(AppRoutes.LibroLista)
                                selected = "Inicio"
                            }
                            //TODO implementar las demas ventanitas
                        }
                        println(selected)
                    }
                    .padding(vertical = 14.dp, horizontal = 8.dp)
                    .background(
                        if (item == selected) MaterialTheme.colors.primary.copy(alpha = 0.2f)
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
                    /* Está bien para notificar nuevos
                    Text(
                        text = "Nuevo",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )*/
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
                contentDescription = "Nose",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "No sé que poner aquí",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}