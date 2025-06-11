package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Avatar
import org.example.projects.BaseDeDatos.model.Usuario
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.Screens.DireccionFormulario.DialogoDirecciones
import org.example.projects.Screens.DireccionFormulario.FormularioDireccion
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//Mapea la imagen obtenia a un MapaDeBits
expect fun convertByteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap
//Pantalla de mi perfil sonde puedes cambiar direcciones o el avatar y cerrar sesión
@Composable
fun MiPerfilScreen(
    librosViewModel: LibrosViewModel,
    navController: Navegator,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    uiViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel
) {
    val username by authViewModel.username.collectAsState()
    val email by authViewModel.email.collectAsState()
    val imagenUsuario by authViewModel.imagenUsuario.collectAsState()
    val idAvatarUsuario by authViewModel.idAvatarUsuario.collectAsState()
    val listaAvatares by authViewModel.listaAvatares.collectAsState()

    val showDialogError by uiViewModel.showDialog.collectAsState()
    val textError by uiViewModel.textError.collectAsState()

    var showAvataresDialog by remember { mutableStateOf(false) }
    var showDireccionesDialog by remember { mutableStateOf(false) }
    var showAddDireccionDialog by remember { mutableStateOf(false) }


    LayoutPrincipal(
        headerContent = { drawerState, scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { },
                onSearchClick = { },
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = { drawerState ->
            MenuBurger(
                drawerState = drawerState,
                navController = navController,
                uiViewModel = uiViewModel,
                authViewModel = authViewModel,
                sharedViewModel = sharedViewModel
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp) // Espaciado entre items
        ) {
            item {
                Text(
                    text = "Mi Perfil",
                    style = MaterialTheme.typography.h4,
                    color = AppColors.primary
                )
            }

            item {
                // Avatar clicable para cambiar
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(4.dp, AppColors.primary, CircleShape)
                        .clickable { showAvataresDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagenUsuario != null) {
                        Image(
                            bitmap = imagenUsuario!!,
                            contentDescription = "Avatar del usuario",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        CircularProgressIndicator(color = AppColors.primary, strokeWidth = 2.dp)
                    }
                }
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nombre de usuario",
                        style = MaterialTheme.typography.caption,
                        color = AppColors.primary
                    )
                    Text(
                        text = username,
                        style = MaterialTheme.typography.h6,
                        color = AppColors.primary
                    )
                }
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Correo electrónico",
                        style = MaterialTheme.typography.caption,
                        color = AppColors.primary
                    )
                    Text(
                        text = email,
                        style = MaterialTheme.typography.body2,
                        color = AppColors.primary
                    )
                }
            }

            item {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.primary,
                        contentColor = AppColors.white
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    Text("Volver")
                }
            }

            item {
                Button(
                    onClick = { showDireccionesDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.primary,
                        contentColor = AppColors.white
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    Text("Ver mis Direcciones")
                }
            }

            item {
                Button(
                    onClick = {
                        authViewModel.logout()
                        librosViewModel.limpiar()
                        carritoViewModel.limpiar()
                        sharedViewModel.limpiar()
                        navController.navigateTo(AppRoutes.Login)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    Text("Cerrar sesión", color = AppColors.white)
                }
            }
        }


        // Dialog para seleccionar avatar
        if (showAvataresDialog) {

                AvatarSelectionDialog(
                    avatares = listaAvatares,
                    onAvatarSelected = { avatarId ->
                        authViewModel.cambiarAvatar(avatarId)
                        showAvataresDialog = false
                    },
                    onDismiss = { showAvataresDialog = false }
                )

        }


        // Diálogos
        if (showDireccionesDialog) {
            DialogoDirecciones(
                authViewModel = authViewModel,
                onConfirm = {
                    showDireccionesDialog = false
                    showAddDireccionDialog = false
                },
                onCancel = { showDireccionesDialog = false },
                onAddNew = {
                    showDireccionesDialog = false
                    showAddDireccionDialog = true
                }
            )
        }

        if (showAddDireccionDialog) {
            AlertDialog(
                onDismissRequest = { showAddDireccionDialog = false },
                title = { Text("Nueva dirección"  ,style = MaterialTheme.typography.h6,
                    color = AppColors.primary) },
                text = {
                    FormularioDireccion(
                        onSave = { nuevaDireccion ->
                            authViewModel.addDireccion(nuevaDireccion)
                            showAddDireccionDialog = false
                            showDireccionesDialog = true
                        },
                        onCancel = { showAddDireccionDialog = false }
                    )
                },
                buttons = {} // Los botones están dentro del formulario
            )
        }

        if (showDialogError){
            ErrorDialog(textError = textError) {
                uiViewModel.setShowDialog(it)
            }
        }
    }
}

//Seleccionar el avatar
@Composable
private fun AvatarSelectionDialog(
    avatares: List<Avatar>,
    onAvatarSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(avatares) { avatar ->
                    val bitmap = remember(avatar.data) {
                        convertByteArrayToImageBitmap(avatar.data.toByteArray())
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .border(2.dp, AppColors.primary, CircleShape)
                            .clickable { onAvatarSelected(avatar.id!!) }
                            .background(AppColors.lightGrey.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Avatar del usuario",
                                contentScale = ContentScale.Crop, // Esto es crucial
                                modifier = Modifier
                                    .fillMaxWidth().height(180.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            CircularProgressIndicator(
                                color = AppColors.primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = AppColors.primary)
            }
        }
    )
}
