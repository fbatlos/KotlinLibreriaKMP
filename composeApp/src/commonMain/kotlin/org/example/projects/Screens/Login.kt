package org.example.projects.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.actapp.componentes_login.BottonLogin
import com.example.actapp.componentes_login.Contrasenia
import com.example.actapp.componentes_login.ErrorDialog
import com.example.actapp.componentes_login.Usuario
import kotlinx.coroutines.*
import org.example.projects.NavController.AppRoutes
import org.example.projects.NavController.Navegator
import org.example.projects.ViewModel.*

@Composable
fun Login(modifier: Modifier, navController: Navegator, authViewModel: AuthViewModel, uiStateViewModel: UiStateViewModel, sharedViewModel: SharedViewModel) {

    val userName by authViewModel.username.collectAsState()
    val contrasenia by authViewModel.contrasenia.collectAsState()
    val isEnable by authViewModel.isLoginEnable.collectAsState()

    val textError by uiStateViewModel.textError.collectAsState()
    val showDialog by uiStateViewModel.showDialog.collectAsState()
    val isLoading by uiStateViewModel.isLoading.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier =modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        ErrorDialog(showDialog = showDialog, textError = textError ?: "Error común"){
            uiStateViewModel.setShowDialog(it)
        }

        LoadingOverlay(isLoading)

        Usuario(userName, cabecero = "Usuario"){
            authViewModel.onLogChange(username = it , contrasenia = contrasenia)
        }

        Contrasenia(contrasenia){
            authViewModel.onLogChange(username = userName , contrasenia = it)
        }

        BottonLogin(
            onBotonChange = {
                authViewModel.fetchLogin(username = userName, password = contrasenia, callback = {
                    if (it == true) {
                        navController.navigateTo(AppRoutes.LibroLista)
                    }
                    else {
                        authViewModel.onLogChange(username = "", contrasenia = "")
                    }
                })

            },
            enable = isEnable
        )

        Spacer(Modifier.height(9.dp))

        Resgistrarse(navController)
        
    }
}

@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // Fondo semitransparente
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}

@Composable
fun Resgistrarse(navController: Navegator){
    Text(
        text = "No tengo cuenta." ,
        color = Color.Cyan,
        modifier = Modifier.clickable {
            navController.popBackStack()
        }

    )
}

