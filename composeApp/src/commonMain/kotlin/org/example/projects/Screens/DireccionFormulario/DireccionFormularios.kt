package org.example.projects.Screens.DireccionFormulario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.example.projects.BaseDeDatos.model.Direccion
import org.example.projects.ViewModel.AuthViewModel
@Composable
fun FormularioDireccion(
    onSave: (Direccion) -> Unit,
    onCancel: () -> Unit
) {
    var calle by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = AppColors.primary,
        unfocusedBorderColor = AppColors.primary.copy(alpha = 0.6f),
        cursorColor = AppColors.primary,
        focusedLabelColor = AppColors.primary,
        textColor = MaterialTheme.colors.onSurface
    )

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Añadir dirección de envío",
            style = MaterialTheme.typography.h6,
            color = AppColors.primary
        )

        OutlinedTextField(
            value = calle,
            onValueChange = { calle = it },
            label = { Text("Calle", color = AppColors.primary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        OutlinedTextField(
            value = num,
            onValueChange = { input ->
                // Solo permitir números
                if (input.all { it.isDigit() } && input.length <= 3) num = input
            },
            label = { Text("Número", color = AppColors.primary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            colors = textFieldColors
        )

        OutlinedTextField(
            value = municipio,
            onValueChange = { municipio = it },
            label = { Text("Municipio", color = AppColors.primary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        OutlinedTextField(
            value = provincia,
            onValueChange = { provincia = it },
            label = { Text("Provincia", color = AppColors.primary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancelar", color = AppColors.error)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    onSave(Direccion(calle, num, municipio, provincia))
                },
                enabled = listOf(calle, num, municipio, provincia).all { it.isNotBlank() },
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.primary)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}

@Composable
fun DialogoDirecciones(
    authViewModel: AuthViewModel,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onAddNew: () -> Unit
) {
    val direcciones by authViewModel.direcciones.collectAsState()
    val direccionSeleccionada by authViewModel.direccionSeleccionada.collectAsState()

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                "Selecciona dirección de envío",
                style = MaterialTheme.typography.h6,
                color = AppColors.primary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (direcciones.isEmpty()) {
                    Text("No tienes direcciones registradas. Añade una para continuar.")
                } else {
                    Spacer(Modifier.height(4.dp))

                    direcciones.forEach { direccion ->
                        Card(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = if (direccion == direccionSeleccionada)
                                AppColors.primary.copy(alpha = 0.05f)
                            else
                                MaterialTheme.colors.surface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    authViewModel.seleccionarDireccion(direccion)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = direccion == direccionSeleccionada,
                                    onClick = { authViewModel.seleccionarDireccion(direccion) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = AppColors.primary
                                    )
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        "${direccion.calle} ${direccion.num}",
                                        style = MaterialTheme.typography.subtitle1,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("${direccion.municipio}, ${direccion.provincia}")
                                }

                                IconButton(onClick = {
                                    authViewModel.deleteDireccion(direccion)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = AppColors.error
                                    )
                                }
                            }
                        }
                    }
                }

                if (direcciones.size < 3) {
                    Button(
                        onClick = onAddNew,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.primary)
                    ) {
                        Text("Añadir nueva dirección", color = Color.White)
                    }
                } else {
                    Text(
                        "Máximo de 3 direcciones alcanzado.",
                        color = AppColors.error,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = direccionSeleccionada != null,
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.primary)
            ) {
                Text("Confirmar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar", color = AppColors.error)
            }
        }
    )
}


