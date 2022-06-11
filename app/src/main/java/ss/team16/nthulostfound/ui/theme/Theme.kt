package ss.team16.nthulostfound.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = NTHU400,
    primaryVariant = NTHU900,
    secondary = NTHU700,
    secondaryVariant = NTHU900,
    background = TocasGray900,
    surface = TocasGray700,
    onPrimary = TocasGray100,
    onSecondary = TocasGray100,
    onBackground = TocasGray100,
    onSurface = TocasGray100
)

private val LightColorPalette = lightColors(
    primary = NTHU700,
    primaryVariant = NTHU400,
    secondary = NTHUBlack900,
    secondaryVariant = NTHUBlack800,
    surface = TocasGray100,
    onSecondary = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun NTHULostFoundTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}