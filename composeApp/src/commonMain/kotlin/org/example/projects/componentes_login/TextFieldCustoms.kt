package org.example.projects.componentes_login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.body1,
                color = AppColors.grey
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = label,
                tint = if (isError) AppColors.error else AppColors.primary
            )
        },
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.textFieldColors(
            textColor = AppColors.black,
            backgroundColor = AppColors.silver,
            cursorColor = AppColors.primary,
            focusedIndicatorColor = AppColors.primary,
            unfocusedIndicatorColor = AppColors.lightGrey,
            errorIndicatorColor = AppColors.error,
            focusedLabelColor = AppColors.primary,
            unfocusedLabelColor = AppColors.grey,
            errorLabelColor = AppColors.error
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    label: String = "Contraseña"
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.body1,
                color = AppColors.grey
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Contraseña",
                tint = if (isError) AppColors.error else AppColors.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Close else Icons.Default.Lock,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = if (isError) AppColors.black else AppColors.black
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            textColor = AppColors.black,
            backgroundColor = AppColors.silver,
            cursorColor = AppColors.primary,
            focusedIndicatorColor = AppColors.primary,
            unfocusedIndicatorColor = AppColors.lightGrey,
            errorIndicatorColor = AppColors.error,
            focusedLabelColor = AppColors.primary,
            unfocusedLabelColor = AppColors.grey,
            errorLabelColor = AppColors.error
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}
