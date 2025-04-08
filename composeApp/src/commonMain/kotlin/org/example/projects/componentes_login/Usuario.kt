package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun Usuario(Usuario:String, cabecero:String,onUsuarioChange: (String) -> Unit){
    TextField(
        value = Usuario,
        onValueChange = onUsuarioChange,
        label = {
            Text(
                text = cabecero,
                color = Color.Blue
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Blue,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Black,

        ),
        modifier = Modifier
            .size(200.dp, 70.dp)
    )
}