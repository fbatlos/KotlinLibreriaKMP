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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.DTO.UsuarioDTO
import org.example.projects.BaseDeDatos.DTO.UsuarioLoginDTO
import org.example.projects.BaseDeDatos.Model.AuthResponse
import org.example.projects.NavController.AppNavigator
import org.example.projects.ViewModel.*


@Composable
fun Login(modifier: Modifier, navController: AppNavigator, viewModel: SharedViewModel) {

    val Usuario by viewModel.username.collectAsState()
    val Contrasenia by viewModel.contrasenia.collectAsState()
    val IsEnable by viewModel.isLoginEnable.collectAsState()
    val textError by viewModel.textError.collectAsState()
    val scope = rememberCoroutineScope()
    val showDialog by viewModel.showDialog.collectAsState()
    val focusManager = LocalFocusManager.current

    val isLoading by viewModel.isLoading.collectAsState()

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
            viewModel.onShowDialog(it)
        }

        LoadingOverlay(isLoading)


        Usuario(Usuario, cabecero = "Usuario"){
            viewModel.onLogChange(username = it , contrasenia = Contrasenia)
        }

        Contrasenia(Contrasenia){
            viewModel.onLogChange(username = Usuario , contrasenia = it)
        }

        BottonLogin(
            onBotonChange = {
                viewModel.onIsLoading(true)
                scope.launch {
                    val login = validarUsuario(viewModel.username.value ?: "", viewModel.contrasenia.value ?: "").await()

                    viewModel.onIsLoading(false)
                    if (login.first) {
                        val auth = AuthResponse(login.second, UsuarioDTO(Usuario,"ROLE_USER"))

                        navController.navigateTo("detail" + "/${Json.encodeToString(auth)}")
                    }
                    else {
                        viewModel.textErrorChange(login.second)
                        viewModel.onShowDialog(true)
                        viewModel.onLogChange(username = "", contrasenia = "")
                    }
                }
            },
            enable = IsEnable
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
fun Resgistrarse(navController: AppNavigator){
    Text(
        text = "No tengo cuenta." ,
        color = Color.Cyan,
        modifier = Modifier.clickable {
            navController.goBack()
        }

    )
}


//Mirar muy bien lo de las corrutinas que es muy lioso y no sabes como funciona bien
fun validarUsuario(username: String, password: String): Deferred<Pair<Boolean,String>> {
    val scope = CoroutineScope(Dispatchers.IO)
    val usuarioLoginDTO = UsuarioLoginDTO(username = username, password = password)

    return scope.async(Dispatchers.IO) {
        try {
            val response = API.apiService.postLogin(usuarioLoginDTO)
            if (response.status.value == 200) {
                val tokenbd = response.body<String>()
                println("Token" + tokenbd)
                return@async Pair(true,tokenbd)
            } else {
                val error = API.parseError(response)
                return@async Pair(false,error.message)
            }
        } catch (e: Exception) {
            return@async Pair(false,e.toString())
        }
    }
}
