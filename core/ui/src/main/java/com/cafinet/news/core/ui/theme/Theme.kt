package com.cafinet.news.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = CafinetPrimary,
    secondary = CafinetSecondary,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    surfaceVariant = CategoryChipBackground,
)

private val DarkColors = darkColorScheme(
    primary = CafinetPrimaryDark,
    secondary = CafinetSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    surfaceVariant = CategoryChipBackgroundDark,
)

/**
 * App-wide Material3 theme. [useDarkTheme] and [fontOption]/[textScale] are
 * driven by the user's Settings preferences (see PreferencesManager),
 * defaulting to the system theme when not explicitly overridden.
 */
@Composable
fun CafinetNewsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    fontOption: AppFontOption = AppFontOption.VAZIRMATN,
    textScale: AppTextScale = AppTextScale.MEDIUM,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors
    val typography = buildAppTypography(fontOption.fontFamily, textScale)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = AppShapes,
        content = content,
    )
}

/** Convenience overlay tint for image scrims on news cards (title readability). */
val NewsCardScrim = Color.Black.copy(alpha = 0.55f)
