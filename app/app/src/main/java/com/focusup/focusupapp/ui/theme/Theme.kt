package com.focusup.focusupapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    // Es usada para la top bar y bottom bar
    surfaceVariant = LightBlue,
    // Se usa para los textos del top bar y bottom bar
    onSurfaceVariant = White,
    // Se usa para las celdas de los dias del calendario
    surface = BlueGray,
    // Es usado para las tareas en cada dia del calendario
    secondary = SpecialRed,
    // Se usa para los textos de las tareas en el calendario
    onSecondary = Black,
    // Es usado para los botones
    primary = Blue1,
    // Se usa para los textos de los botones
    onPrimary = White,
    // Es usado para el fondo
    background = LightYellow,
    // Se usa para el texto de los dias y el mes, y cosas dentro de background
    onBackground = Black
)

private val DarkColorScheme = darkColorScheme(
    // Es usada para la top bar y bottom bar
    surfaceVariant = LightBlack,
    // Se usa para los textos del top bar y bottom bar
    onSurfaceVariant = White,
    // Se usa para las celdas de los dias del calendario
    surface = DarkBlueGray,
    // Es usado para las tareas en cada dia del calendario
    secondary = SpecialRed,
    // Se usa para los textos de las tareas en el calendario
    onSecondary = Black,
    // Es usado para los botones
    primary = Blue2,
    // Se usa para los textos de los botones
    onPrimary = White,
    // Es usado para el fondo
    background = DarkGray,
    // Se usa para el texto de los dias y el mes, y cosas dentro de background
    onBackground = White
)

private val LightColorScheme2 = lightColorScheme(
   // Es usada para la top bar y bottom bar
    surfaceVariant = GreenBlue,
    // Se usa para los textos del top bar y bottom bar
    onSurfaceVariant = White,
    // Se usa para las celdas de los dias del calendario
    surface = LightBlueGray,
    // Es usado para las tareas en cada dia del calendario
    secondary = SpecialRed,
    // Se usa para los textos de las tareas en el calendario
    onSecondary = Black,
    // Es usado para los botones
    primary = Purple,
    // Se usa para los textos de los botones
    onPrimary = White,
    // Es usado para el fondo
    background = LightCream,
    // Se usa para el texto de los dias y el mes, y cosas dentro de background
    onBackground = DarkBlue
)

private val DarkColorScheme2 = darkColorScheme(
    // Es usada para la top bar y bottom bar
    surfaceVariant = DarkGreen2,
    // Se usa para los textos del top bar y bottom bar
    onSurfaceVariant = White,
    // Se usa para las celdas de los dias del calendario
    surface = DarkGreen3,
    // Es usado para las tareas en cada dia del calendario
    secondary = SpecialRed,
    // Se usa para los textos de las tareas en el calendario
    onSecondary = Black,
    // Es usado para los botones
    primary = DarkGreen4,
    // Se usa para los textos de los botones
    onPrimary = White,
    // Es usado para el fondo
    background = Black,
    // Se usa para el texto de los dias y el mes, y cosas dentro de background
    onBackground = LightGreen
)

@Composable
fun FocusUpTheme(
    content: @Composable () -> Unit
) {
    val themeState = LocalThemeId.current // viene de ThemeState.kt
    val themeId = themeState.value

    val colorScheme = when (themeId) {
        1 -> LightColorScheme2
        2 -> DarkColorScheme2
        3 -> LightColorScheme
        4 -> DarkColorScheme
        else -> LightColorScheme2
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
