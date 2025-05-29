package org.example.projects.Screens

import AppColors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.actapp.componentes_login.ErrorDialog
import kotlinx.coroutines.*
import org.example.projects.BaseDeDatos.API
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.Screens.CommonParts.HeaderConHamburguesa
import org.example.projects.Screens.CommonParts.LayoutPrincipal
import org.example.projects.Screens.CommonParts.MenuBurger
import org.example.projects.ViewModel.*
import org.example.projects.Screens.componentes_login.CustomPasswordField
import org.example.projects.Screens.componentes_login.CustomTextField

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: Navegator,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    uiStateViewModel: UiStateViewModel,
    sharedViewModel: SharedViewModel
) {
    val userName by authViewModel.username.collectAsState()
    val contrasenia by authViewModel.contrasenia.collectAsState()
    val email by authViewModel.email.collectAsState()
    val isEnable by authViewModel.isLoginEnable.collectAsState()

    val textError by uiStateViewModel.textError.collectAsState()
    val showDialog by uiStateViewModel.showDialog.collectAsState()
    val isLoading by uiStateViewModel.isLoading.collectAsState()
    val focusManager = LocalFocusManager.current

    var contraseniaRepeat  by remember { mutableStateOf("") }

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
            MenuBurger(drawerState,navController, sharedViewModel = sharedViewModel)
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AppColors.white)
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo o título de la app
                Text(
                    text = "LeafRead",
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold,
                        color = AppColors.primary
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            value = userName,
                            onValueChange = { authViewModel.onRegisterChange(it, contrasenia,email) },
                            label = "Usuario",
                            leadingIcon = Icons.Default.Person,
                            isError = textError?.contains("usuario") == true
                        )

                        CustomTextField(
                            value = email,
                            onValueChange = { authViewModel.onRegisterChange(userName, contrasenia, it) },
                            label = "Email",
                            leadingIcon = Icons.Default.Email,
                            isError = textError?.contains("email") == true
                        )

                        CustomPasswordField(
                            value = contrasenia,
                            onValueChange = { authViewModel.onRegisterChange(userName, it,email) },
                            isError = textError?.contains("contraseña") == true
                        )

                        CustomPasswordField(
                            value = contraseniaRepeat,
                            label = "Repetir contraseña ",
                            onValueChange = {contraseniaRepeat = it},
                            isError = contrasenia != contraseniaRepeat || textError?.contains("contraseña") == true
                        )
                    }
                }

                Button(
                    onClick = {
                        authViewModel.fetchRegister(
                            username = userName.replace(" ",""),
                            email = email,
                            password = contrasenia,
                            passwordRepeat = contraseniaRepeat,
                            callback = { success ->
                                if (success) {
                                    navController.navigateTo(AppRoutes.LibroLista)
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = isEnable,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.primary,
                        disabledBackgroundColor = AppColors.secondary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Registrarse",
                            color = AppColors.white
                        )
                    }
                }

                Text(
                    text = "¿Tienes cuenta? Iniciar Sesión",
                    color = AppColors.primary,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.clickable {
                        navController.navigateTo(AppRoutes.Login)
                    }
                )
            }

            if (showDialog) {
                ErrorDialog(
                    textError = textError ?: "Error desconocido",
                    onDismiss = { uiStateViewModel.setShowDialog(false) }
                )
            }
        }

    }
}