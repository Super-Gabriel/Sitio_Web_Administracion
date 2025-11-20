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
val ThemePink = lightColorScheme(
    surfaceVariant = DarkPink2,
    onSurfaceVariant = White,
    surface = LightPink2,
    secondary = LightPink2,
    onSecondary = Black,
    primary = DarkPink2,
    onPrimary = White,
    background = LightPink1,
    onBackground = White
)

val ThemeOcean = darkColorScheme(
    surfaceVariant = DarkBlue2,
    onSurfaceVariant = White,
    surface = Blue3,
    secondary = DarkBlue6,
    onSecondary = Black,
    primary = DarkBlue4,
    onPrimary = White,
    background = LightBlue4 ,
    onBackground = White
)

val ThemeRed = lightColorScheme(
    surfaceVariant = DarkRed1 ,
    onSurfaceVariant = White,
    surface = LightRed3,
    secondary = LightRed1,
    onSecondary = Black,
    primary = DarkRed1,
    onPrimary = White,
    background = LightRed2,
    onBackground = White
)

val ThemeGreen = lightColorScheme(
    surfaceVariant = DarkGreen2 ,
    onSurfaceVariant = White,
    surface = LightGreen1,
    secondary = LightGreen1,
    onSecondary = Black,
    primary = DarkGreen1,
    onPrimary = White,
    background = LightGreen2,
    onBackground = DarkGreen4
)

val ThemeYellow = lightColorScheme(
    surfaceVariant = DarkYellow2,
    onSurfaceVariant = White,
    surface = LightYellow3,
    secondary = LightYellow4,
    onSecondary = Black,
    primary = LightYellow1,
    onPrimary = White,
    background = LightYellow2,
    onBackground = LightYellow1
)

val ThemePurple = darkColorScheme(
    surfaceVariant = DarkPurple2,
    onSurfaceVariant = White,
    surface = LightPurple1,
    secondary = DarkPurple2,
    onSecondary = White,
    primary = DarkPurple1,
    onPrimary = White,
    background = LightPurple2,
    onBackground = DarkPurple3
)

val ThemeOrange = lightColorScheme(
    surfaceVariant = DarkOrange1,
    onSurfaceVariant = White,
    surface = LightOrange5,
    secondary = LightOrange2,
    onSecondary = Black,
    primary = DarkOrange1,
    onPrimary = White,
    background = LightOrange5,
    onBackground = MediumOrange2
)

val ThemePanda = lightColorScheme(
    surfaceVariant = DarkGray2,
    onSurfaceVariant = White,
    surface = White,
    secondary = SoftGray1,
    onSecondary = Black,
    primary = DarkGray1,
    onPrimary = White,
    background = SoftGray1,
    onBackground = Black
)

val ThemeRainbow = lightColorScheme(
    surfaceVariant = PastelRed1,
    onSurfaceVariant = White,
    surface = PastelLightBlue1,
    secondary = PastelYellow1,
    onSecondary = Black,
    primary = PastelDarkBlue1,
    onPrimary = White,
    background = PastelRed1,
    onBackground = White
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
        5 -> ThemePink
        6 -> ThemeOcean
        7 -> ThemeGreen
        8 -> ThemeYellow
        9 -> ThemePurple
        10 -> ThemeOrange
        11 -> ThemePanda
        12 -> ThemeRainbow

        else -> LightColorScheme2
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
