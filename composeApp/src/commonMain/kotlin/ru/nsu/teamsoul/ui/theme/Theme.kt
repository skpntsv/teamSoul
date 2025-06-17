package ru.nsu.teamsoul.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colors = lightColorScheme(
    primary = Color(0xFF0D47A1),
    onPrimary = Color.White,

    secondary = Color(0xFF42A5F5),
    onSecondary = Color.White,

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF212121),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),

    error = Color(0xFFD32F2F)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}