package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Dialog de error genérico
@Composable
fun ErrorDialog(
    textError: String,
    onDismiss: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        backgroundColor = AppColors.white,
        title = {
            Text(
                text = "Error",
                fontSize = 22.sp,
                color = AppColors.error
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = textError,
                    fontSize = 16.sp,
                    color = AppColors.darkGrey
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss(false) },
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.primary)
            ) {
                Text(
                    text = "Aceptar",
                    color = AppColors.white,
                    fontSize = 16.sp
                )
            }
        }
    )
}
