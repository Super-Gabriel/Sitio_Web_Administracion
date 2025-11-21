package com.focusup.focusupapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.focusup.focusupapp.ui.theme.Black

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
    surface = LightPink3,
    secondary = LightPink2,
    onSecondary = Black,
    primary = Pink,
    onPrimary = White,
    background = LightPink1,
    onBackground = White
)

val ThemeOcean = lightColorScheme(
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
    surfaceVariant = Yellow6,
    onSurfaceVariant = Yellow4,
    surface = LightYellow3,
    secondary = LightYellow4,
    onSecondary = Yellow4,
    primary = LightYellow1,
    onPrimary = White,
    background = LightYellow4,
    onBackground = Yellow4
)

val ThemePurple = lightColorScheme(
    surfaceVariant = DarkPurple2,
    onSurfaceVariant = White,
    surface = LightPurple1,
    secondary = DarkPurple2,
    onSecondary = Black,
    primary = DarkPurple1,
    onPrimary = White,
    background = LightPurple2,
    onBackground = White
)

val ThemeOrange = lightColorScheme(
    surfaceVariant = DarkOrange1,
    onSurfaceVariant = White,
    surface = LightOrange2,
    secondary = MediumOrange2,
    onSecondary = Black,
    primary = Orange1,
    onPrimary = White,
    background = LightOrange5,
    onBackground = White
)

val ThemePanda = lightColorScheme(
    surfaceVariant = DarkGray2,
    onSurfaceVariant = White,
    surface = SoftGray2,
    secondary = SoftGray1,
    onSecondary = Black,
    primary = DarkGray1,
    onPrimary = White,
    background = White,
    onBackground = Black
)

val ThemeRainbow = lightColorScheme(
    surfaceVariant = PastelLightBlue1,
    onSurfaceVariant = PastelDarkBlue1,
    surface = PastelPurple1,
    secondary = PastelYellow1,
    onSecondary = PastelDarkBlue1,
    primary = PastelRed1,
    onPrimary = PastelDarkBlue1,
    background = PastelGreen1,
    onBackground = PastelDarkBlue1
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
