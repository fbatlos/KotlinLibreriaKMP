package org.example.projects.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.example.projects.R

@Composable
actual fun getAvatarList(): List<Painter> {
    return listOf(
        painterResource(id = R.drawable.avatar1)
    )
}
