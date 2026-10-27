package com.mobile.vedroid.compose.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = BlueDeep,
    // Other default colors to override
    onPrimary = Color.White,
    primaryContainer = BlueDeepLight,
    onPrimaryContainer = BlueDeepDarker,

    secondary = Blue,
    // Other default colors to override
    onSecondary = Color.White,
    secondaryContainer = BlueLight,
    onSecondaryContainer = BlueDarker,

    tertiary = Green,
    // Other default colors to override
    onTertiary = Color.White,
    tertiaryContainer = GreenLight,
    onTertiaryContainer = GreenDarker,

    // Other default colors to override
    surface = ComposeLight,
    onSurface = ComposeDark,

    background = ComposeLight,
    onBackground = ComposeDark
)


private val DarkColorScheme = darkColorScheme(

    primary = BlueDeep,
    // Other default colors to override
    onPrimary = BlueDeepDarker,
    primaryContainer = BlueDeepDark,
    onPrimaryContainer = BlueDeepLight,

    secondary = Blue,
    // Other default colors to override
    onSecondary = BlueDarker,
    secondaryContainer = BlueDark,
    onSecondaryContainer = BlueLight,

    tertiary = Green,
    // Other default colors to override
    onTertiary = GreenDarker,
    tertiaryContainer = GreenDark,
    onTertiaryContainer = GreenLight,

    // Other default colors to override
    surface = ComposeDark,
    onSurface = ComposeLight,

    background = ComposeDark,
    onBackground = ComposeLight
)



@Composable
fun MobileAndroidAppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic color is available on Android 12+
        // But in VEDROID App not needed
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}