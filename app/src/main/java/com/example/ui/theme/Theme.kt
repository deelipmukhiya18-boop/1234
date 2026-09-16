package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MayaDarkColorScheme = darkColorScheme(
    primary = MayaRed,
    onPrimary = TextPrimary,
    primaryContainer = MayaCrimsonDark,
    onPrimaryContainer = MayaCoral,
    secondary = MayaCoral,
    onSecondary = MayaBlack,
    secondaryContainer = MayaCardHighlight,
    onSecondaryContainer = TextPrimary,
    tertiary = MayaBlue,
    background = MayaBlack,
    onBackground = TextPrimary,
    surface = MayaSurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = MayaCardDark,
    onSurfaceVariant = TextSecondary,
    outline = MayaBorder
)

@Composable
fun MayaTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = MayaBlack.toArgb()
                window.navigationBarColor = MayaBlack.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = MayaDarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MayaTheme(content = content)
}
