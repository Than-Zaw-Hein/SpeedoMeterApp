package com.example.speedometerapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define the light color scheme
val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = TextIconsColor,
    primaryContainer = LightPrimaryColor,
//    secondary = AccentColor,
//    onSecondary = TextIconsColor,
//    background = Color.White,
//    onBackground = PrimaryTextColor,
//    surface = Color.White,
//    onSurface = PrimaryTextColor,
//    error = Color.Red,
//    onError = TextIconsColor
)

// Define the dark color scheme
val DarkColorScheme = darkColorScheme(
    primary = PrimaryDarkColor,
    onPrimary = TextIconsColor,
    primaryContainer = PrimaryColor,
//    secondary = AccentColor,
//    onSecondary = TextIconsColor,
//    background = PrimaryTextColor,
//    onBackground = Color.White,
//    surface = PrimaryTextColor,
//    onSurface = Color.White,
//    error = Color.Red,
//    onError = TextIconsColor
)

@Composable
fun SpeedometerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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