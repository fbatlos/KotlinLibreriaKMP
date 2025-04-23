package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorDialog(textError:String,onDismiss:(Boolean)->Unit) {

        AlertDialog(
            onDismissRequest = {
                onDismiss(false)
            },
            title = {
                Text(
                    text = "Error",
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = textError,
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss(false)
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )

}
