package org.example.projects.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.example.projects.NavController.AppNavigator

// DetailScreen.kt (commonMain)
@Composable
fun DetailScreen(navigator: AppNavigator) {
    Column {
        Text("Pantalla de Detalle (Common)")
        Button(onClick = { navigator.navigateTo("login") }) {
            Text("Volver")
        }
    }
}