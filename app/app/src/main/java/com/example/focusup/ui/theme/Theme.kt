package com.example.focusup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    /*
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    */
    /*
    // Generamos un numero aleatorio entre 1 y 2
    val numero = (1..2).random()
    val colorScheme = when {
        darkTheme && numero == 1 -> DarkColorScheme
        darkTheme && numero == 2 -> DarkColorScheme2
        !darkTheme && numero == 1 -> LightColorScheme
        else -> LightColorScheme2
    }
    */
    //val colorScheme = DarkColorScheme2
    val colorScheme = if(darkTheme) DarkColorScheme2 else LightColorScheme2

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}