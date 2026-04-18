package com.yoonjin.safebox.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary          = Dark_Primary,
    onPrimary        = Dark_OnPrimary,
    secondary        = Dark_Secondary,
    onSecondary      = Dark_OnSecondary,
    background       = Dark_Background,
    onBackground     = Dark_OnBackground,
    surface          = Dark_Surface,
    onSurface        = Dark_OnSurface,
    surfaceVariant   = Dark_SurfaceVar,
    outline          = Dark_Outline,
)

private val LightColorScheme = lightColorScheme(
    primary          = Light_Primary,
    onPrimary        = Light_OnPrimary,
    secondary        = Light_Secondary,
    onSecondary      = Light_OnSecondary,
    background       = Light_Background,
    onBackground     = Light_OnBackground,
    surface          = Light_Surface,
    onSurface        = Light_OnSurface,
    surfaceVariant   = Light_SurfaceVar,
    outline          = Light_Outline,
)

@Composable
fun SafeBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}