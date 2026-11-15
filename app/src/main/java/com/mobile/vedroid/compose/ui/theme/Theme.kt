package com.mobile.vedroid.compose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import android.content.res.Configuration
import java.util.Locale

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
fun MobileAndroidAppComposeTheme (
    setRussian : Boolean = false,
    themeMode: AppMode = AppMode.SYSTEM,
    dynamicColor: Boolean = true,

    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = when(themeMode){
        AppMode.DARK -> true
        AppMode.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        // Dynamic color is available on Android 12+
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val currentConfig = LocalConfiguration.current
    val currentContext = LocalContext.current
    val targetLocale = if (setRussian) Locale("ru") else LocalLocale.current.platformLocale
    val newConfig = Configuration(currentConfig).apply { setLocale(targetLocale) }
    val newContext = currentContext.createConfigurationContext(newConfig)

    CompositionLocalProvider(
        LocalConfiguration provides newConfig,
        LocalContext provides newContext
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}