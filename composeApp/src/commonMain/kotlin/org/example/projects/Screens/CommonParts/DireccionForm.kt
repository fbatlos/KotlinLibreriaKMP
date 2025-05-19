package org.example.projects.Screens.CommonParts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.projects.BaseDeDatos.model.Direccion

@Composable
fun DireccionForm(
    onGuardar: (Direccion) -> Unit,
    onCancelar: () -> Unit
) {
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Añadir Dirección", style = MaterialTheme.typography.h6)

        OutlinedTextField(value = calle, onValueChange = { calle = it }, label = { Text("Calle") })
        OutlinedTextField(value = num, onValueChange = { num = it }, label = { Text("Número") })
        OutlinedTextField(value = municipio, onValueChange = { municipio = it }, label = { Text("Municipio") })
        OutlinedTextField(value = provincia, onValueChange = { provincia = it }, label = { Text("Provincia") })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val direccion = Direccion(calle, num, municipio, provincia)
                    onGuardar(direccion)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.primary)
            ) {
                Text("Guardar", color = AppColors.white)
            }

            OutlinedButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    }
}
