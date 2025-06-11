package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.TicketViewModel
import org.example.projects.ViewModel.UiStateViewModel

@Composable
fun AyudaScreen(
    navController: Navegator,
    uiViewModel: UiStateViewModel,
    authViewModel: AuthViewModel,
    librosViewModel: LibrosViewModel,
    carritoViewModel: CarritoViewModel,
    sharedViewModel: SharedViewModel,
    ticketViewModel: TicketViewModel
) {
    val titulo by ticketViewModel.tituloTicket.collectAsState()
    val cuerpo by ticketViewModel.cuerpo.collectAsState()

    val isLoading by uiViewModel.isLoading.collectAsState()
    val textError by uiViewModel.textError.collectAsState()
    val showDialiog by uiViewModel.showDialog.collectAsState()

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
            MenuBurger(drawerState,navController, uiViewModel,authViewModel,sharedViewModel = sharedViewModel)
        }
    ) { paddingValues ->

        if (isLoading){
            Box (
                modifier = Modifier.fillMaxSize()
            ){
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AppColors.primary,
                    strokeWidth = 2.dp
                )
            }
        }else{

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Centro de Ayuda",
                    style = MaterialTheme.typography.h4,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Bienvenido a la sección de ayuda. Aquí encontrarás información sobre cómo usar la aplicación:" +
                            "\n\n• Navega por el catálogo para ver los productos." +
                            "\n• Desde tu perfil puedes gestionar tus direcciones y avatar." +
                            "\n• Administra tus compras, valoraciones y tickets desde el menú correspondiente." +
                            "\n\nSi tienes algún problema o duda, puedes enviarnos un ticket con tu consulta.",
                    style = MaterialTheme.typography.body1,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Formulario de ticket
            item {
                Text(
                    text = "Enviar un Ticket",
                    style = MaterialTheme.typography.h5,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { ticketViewModel.onChangeTickt(it, cuerpo) },
                    label = { Text("Título") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.primary,
                        unfocusedBorderColor = AppColors.primary.copy(alpha = 0.6f),
                        cursorColor = AppColors.primary,
                        textColor = AppColors.primary
                    ),
                )

                OutlinedTextField(
                    value = cuerpo,
                    onValueChange = { ticketViewModel.onChangeTickt(titulo, it) },
                    label = { Text("Cuerpo del mensaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppColors.primary,
                        unfocusedBorderColor = AppColors.primary.copy(alpha = 0.6f),
                        cursorColor = AppColors.primary,
                        textColor = AppColors.primary
                    ),
                    maxLines = 5
                )

                Button(
                    onClick = {
                        if (!sharedViewModel.token.value.isNullOrEmpty()) {
                            ticketViewModel.addTicketDuda()
                        } else {
                            uiViewModel.setTextError("Tienes que registrarte si quieres enviar un ticket.")
                            uiViewModel.setShowDialog(true)
                            navController.navigateTo(AppRoutes.Login)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.primary,
                        contentColor = AppColors.white
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Enviar Ticket")
                }
            }

            // Datos de soporte
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Soporte Técnico",
                    style = MaterialTheme.typography.h4,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Horario de atención:\nLunes a Viernes de 09:00 a 18:00 hrs",
                    style = MaterialTheme.typography.body1,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Datos de contacto:\n• Email: soporte@leafread.com\n• Teléfono: +34 646 321 177",
                    color = AppColors.primary,
                    style = MaterialTheme.typography.body1
                )
            }
        }
        }
    }
}
