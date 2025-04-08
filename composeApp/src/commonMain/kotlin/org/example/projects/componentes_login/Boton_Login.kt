package com.example.actapp.componentes_login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp


@Composable
fun BottonLogin(texto:String="Iniciar Sesión", onBotonChange:  () -> Unit, enable:Boolean){
    Spacer(Modifier.height(15.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),

        onClick = {
            onBotonChange()
        },

        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Cyan,    // Color de fondo de nuestro boton
            contentColor = Color.Cyan, // Color del texto
        ),

        shape =  RoundedCornerShape(10.dp),
        enabled = enable
    ) {
        Text(
            text = texto,
            color = Color.White
        )
    }
}