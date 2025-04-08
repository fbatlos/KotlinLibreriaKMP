package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp



@Composable
fun Contrasenia(contrasenia: String, onContraseniaChange: (String) -> Unit){
    var active by remember { mutableStateOf(false) }

    TextField(
        value = contrasenia,
        onValueChange = onContraseniaChange,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Cyan,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Cyan,
            focusedLabelColor = Color.Cyan,
            unfocusedLabelColor = Color.Black,
        ),
        label = {
            Text(
                text = "Contraseña" ,
                color = Color.Blue
            )
        },

        trailingIcon = {
            if (contrasenia.isNotEmpty()) {
                IconButton(onClick = { active=!active }) {
                    Icon(if (active) Icons.Filled.Lock else Icons.Filled.Lock, contentDescription = "Ver texto" , tint = Color.Black)
                }
            }
        },
        modifier = Modifier
            .size(200.dp, 70.dp),

        visualTransformation = if (!active) PasswordVisualTransformation() else VisualTransformation.None

    )
}