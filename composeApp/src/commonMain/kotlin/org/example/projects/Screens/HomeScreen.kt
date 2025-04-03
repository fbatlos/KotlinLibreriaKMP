package org.example.projects.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.example.projects.NavController.AppNavigator

@Composable
fun HomeScreen(navigator: AppNavigator) {
    Column {
        Text("Pantalla de Inicio (Common)")
        Button(onClick = { navigator.navigateTo("detail") }) {
            Text("Ir a Detalle")
        }
    }
}