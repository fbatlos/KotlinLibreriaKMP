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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Avatar
import org.example.projects.BaseDeDatos.model.Usuario
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

expect fun convertByteArrayToImageBitmap(byteArray: ByteArray): ImageBitmap


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

    var showAvataresDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.fetchUsuario()
        authViewModel.fetchAllAvatares()
    }

    LaunchedEffect(idAvatarUsuario) {
        if (idAvatarUsuario != null) {
            authViewModel.fetchMiAvatar()
        }
    }

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
                sharedViewModel = sharedViewModel
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.h4,
                color = AppColors.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre de usuario
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

            Spacer(modifier = Modifier.height(12.dp))

            // Email
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

            Spacer(modifier = Modifier.height(40.dp))

            // Botón volver
            Button(
                onClick = {
                    navController.popBackStack()
                },
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

            Spacer(modifier = Modifier.height(20.dp))

            // Botón cerrar sesión
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

        // Dialog para seleccionar avatar
        if (showAvataresDialog) {
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
        }
    }
}

@Composable
private fun AvatarSelectionDialog(
    avatares: List<Avatar>,
    onAvatarSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona un avatar") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(200.dp)
            ) {
                items(avatares) { avatar ->
                    val bitmap = remember(avatar.data) {
                        convertByteArrayToImageBitmap(avatar.data.toByteArray())
                    }
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Avatar ${avatar.id}",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, AppColors.primary, CircleShape)
                            .clickable { onAvatarSelected(avatar.id!!) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
