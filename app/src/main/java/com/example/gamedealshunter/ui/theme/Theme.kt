package com.example.gamedealshunter.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColors = lightColorScheme(
    primary       = Sky,
    onPrimary     = Color.White,
    secondary     = Emerald,
    onSecondary   = Color.White,
    background    = Black,
    onBackground  = Color.White,
    surface       = CardDarkGrayPurple,
    onSurface     = Color.White,
    tertiary      = Coral,
    onTertiary    = Color.White
)

private val DarkColors = darkColorScheme(
    primary       = SkyDark,
    onPrimary     = Color.White,
    secondary     = EmeraldDark,
    onSecondary   = Color.White,
    background    = Black,
    onBackground  = Color.White,
    surface       = CardDarkGrayPurple,
    onSurface     = Color.White,
    tertiary      = CoralDark,
    onTertiary    = Color.White
)

@Composable
fun GameDealsHunterTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        (view.context as? Activity)?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Black.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography  = Typography,
        shapes      = AppShapes,
        content     = content
    )
}
