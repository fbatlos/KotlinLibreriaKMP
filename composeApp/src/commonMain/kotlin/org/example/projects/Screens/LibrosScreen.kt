package org.example.projects.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.actapp.componentes_login.ErrorDialog
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.*
import org.example.projects.BaseDeDatos.API
import org.example.projects.BaseDeDatos.model.Libro
import org.example.projects.NavController.AppNavigator
import org.example.projects.Screens.LibrosMostrar.TarjetaLibro
import org.example.projects.ViewModel.SharedViewModel

// LibrosScreen.kt (commonMain)
@Composable
fun LibrosScreen(
    navigator: AppNavigator,
    viewModel: SharedViewModel
) {
    val token = viewModel.token.value ?: ""

    val isLoading by viewModel.isLoading.collectAsState()
    val textError by viewModel.textError.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val libros = viewModel.libros.collectAsState().value
    val scope = rememberCoroutineScope()


    viewModel.onIsLoading(true)
    scope.launch {
        viewModel.getLibros(getLibros(token, viewModel).await())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (libros.isNullOrEmpty()) {
            Text(
                text = textError,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {
                items(libros) { libro ->
                    TarjetaLibro(libro = libro)
                }
            }
        }

        ErrorDialog(showDialog = showDialog, textError = textError){
            viewModel.onShowDialog(it)
        }
    }
}

// Función suspend (compatible con KMP)
private fun getLibros(token: String, viewModel:SharedViewModel ): Deferred<List<Libro>> {
    val scope = CoroutineScope(Dispatchers.IO)
    val libros = scope.async(Dispatchers.IO){
        try {

            return@async API.apiService.listarLibros(token) // Llama a tu API Ktor

        } catch (e: Exception) {
            viewModel.textErrorChange("Error al cargar libros: ${e.message}")

            return@async listOf()
        } finally {
            viewModel.onIsLoading(false)
        }
    }
    return libros
}