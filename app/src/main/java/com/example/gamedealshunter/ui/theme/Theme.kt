package com.example.gamedealshunter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary   = Coral,
    onPrimary = Color.White,
    secondary = Emerald,
    tertiary  = Sky,
    background = Paper,
    surface    = Paper,
    onBackground = Ink,
    onSurface    = Ink
)

private val DarkColors = darkColorScheme(
    primary   = CoralDark,
    onPrimary = InkLight,
    secondary = EmeraldDark,
    tertiary  = SkyDark,
    background = Graphite,
    surface    = Graphite,
    onBackground = InkLight,
    onSurface    = InkLight
)

val AppShapes = Shapes(
    small  = RoundedCornerShape(12),
    medium = RoundedCornerShape(16),
    large  = RoundedCornerShape(0)
)

@Composable
fun GameDealsHunterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else      -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        (view.context as? Activity)?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        shapes      = AppShapes,
        content     = content
    )
}
