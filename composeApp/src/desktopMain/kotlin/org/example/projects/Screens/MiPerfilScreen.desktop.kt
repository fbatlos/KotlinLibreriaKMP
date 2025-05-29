package org.example.projects.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
actual fun getAvatarList(): List<Painter> {
    return listOf(
        painterResource("avatar1.png")
    )
}
