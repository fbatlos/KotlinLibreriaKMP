package org.example.projects.Screens.AdminScreens

import AppColors
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.example.projects.BaseDeDatos.model.Ticket
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.AdminViewModel.TicketDudaAdminViewModel
import org.example.projects.ViewModel.AuthViewModel
import org.example.projects.ViewModel.CarritoViewModel
import org.example.projects.ViewModel.LibrosViewModel
import org.example.projects.ViewModel.SharedViewModel
import org.example.projects.ViewModel.UiStateViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketsDudaAdminScreen(
    navController: Navegator,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel,
    uiViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel,
    ticketDudaAdminViewModel: TicketDudaAdminViewModel
) {
    val tickets by ticketDudaAdminViewModel.allTickets.collectAsState()
    var selectedTicket by remember { mutableStateOf<Ticket?>(null) }

    LaunchedEffect(Unit) {
      ticketDudaAdminViewModel.gatAllTickets()
    }

    LayoutPrincipal(
        headerContent = { drawerState,scope ->
            HeaderConHamburguesa(
                onMenuClick = { scope.launch { drawerState.open() } },
                onSearch = { },
                onSearchClick = {},
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        },
        drawerContent = {drawerState->
            MenuBurger(drawerState,navController, uiViewModel, authViewModel,sharedViewModel)
        }
    ) { padding ->
        if (tickets.isNullOrEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AppColors.primary)
            }
        }else {
            Box(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        tickets!!
                    ) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            onClick = { selectedTicket = ticket },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }

                selectedTicket?.let { ticket ->
                    TicketDetailDialog(
                        ticket = ticket,
                        onDismiss = { selectedTicket = null },
                        onRespond = { response ->
                            selectedTicket = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.titulo,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    color = AppColors.primary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = ticket.userName,
                    style = MaterialTheme.typography.caption,
                    color = AppColors.primary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticket.cuerpo,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                color = AppColors.primary,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Ver detalles",
                    color = AppColors.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TicketDetailDialog(
    ticket: Ticket,
    onDismiss: () -> Unit,
    onRespond: (String) -> Unit
) {
    var responseText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = ticket.titulo,
                    style = MaterialTheme.typography.h5,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "De: ${ticket.userName}, Correo : ${ticket.email}",
                    style = MaterialTheme.typography.subtitle2,
                    color = AppColors.primary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = ticket.cuerpo,
                    style = MaterialTheme.typography.body1,
                    color = AppColors.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp).background(AppColors.primary)
                    ) {
                        Text("Volver", color = AppColors.white)
                    }
                }
            }
        }
    }
}