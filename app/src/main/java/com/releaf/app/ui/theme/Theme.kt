package com.releaf.app.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = ReleafTextPrimary,
    secondary = ReleafTextSecondary, 
    tertiary = AccentTertiary,
    background = ReleafBackgroundPrimary,
    surface = ReleafSurface,
    onPrimary = ReleafBackgroundPrimary,
    onSecondary = ReleafBackgroundPrimary,
    onTertiary = OnSurfaceDark,
    onBackground = ReleafTextPrimary,
    onSurface = ReleafTextPrimary,
    error = ErrorDark,
    onError = OnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = ReleafTextPrimary,
    secondary = ReleafTextSecondary,
    tertiary = AccentTertiary,
    background = ReleafBackgroundPrimary,
    surface = ReleafSurface,
    onPrimary = ReleafBackgroundPrimary,
    onSecondary = ReleafBackgroundPrimary, 
    onTertiary = OnSurfaceLight,
    onBackground = ReleafTextPrimary,
    onSurface = ReleafTextPrimary,
    error = ErrorLight,
    onError = OnSurfaceLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ - disabled to keep custom green theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}