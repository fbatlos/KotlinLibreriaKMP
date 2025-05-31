package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
@Composable
expect fun getAvatarList(): List<Painter>

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

    authViewModel.fetchUsuario()

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

            // Card de datos del usuario
            androidx.compose.material.Card(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = getAvatarList()[0],
                        contentDescription = "Avatar seleccionado",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(4.dp, AppColors.primary, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Campo nombre de usuario
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

                    // Campo email
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
                colors = androidx.compose.material.ButtonDefaults.buttonColors(
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
}
