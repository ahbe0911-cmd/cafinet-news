package com.cafinet.news

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cafinet.news.core.database.AppFontFamily
import com.cafinet.news.core.database.AppThemeMode
import com.cafinet.news.core.database.AppTextSize
import com.cafinet.news.core.ui.theme.AppFontOption
import com.cafinet.news.core.ui.theme.AppTextScale
import com.cafinet.news.core.ui.theme.CafinetNewsTheme
import com.cafinet.news.navigation.CafinetNavGraph
import com.cafinet.news.ui.settings.SettingsViewModel
import androidx.compose.material3.MaterialTheme

/**
 * Root composable: reads the user's saved theme/font/text-size
 * preferences and applies them app-wide before rendering the nav graph.
 */
@Composable
fun CafinetNewsRoot() {
    // Reuse SettingsViewModel purely as a Hilt-scoped holder of the
    // preferences flow - avoids duplicating DataStore-reading wiring.
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val prefs by settingsViewModel.userPreferences.collectAsState()

    val useDarkTheme = when (prefs.themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val fontOption = when (prefs.fontFamily) {
        AppFontFamily.VAZIRMATN -> AppFontOption.VAZIRMATN
        AppFontFamily.NAZANIN -> AppFontOption.NAZANIN
        AppFontFamily.TITR -> AppFontOption.TITR
    }

    val textScale = when (prefs.textSize) {
        AppTextSize.SMALL -> AppTextScale.SMALL
        AppTextSize.MEDIUM -> AppTextScale.MEDIUM
        AppTextSize.LARGE -> AppTextScale.LARGE
    }

    CafinetNewsTheme(useDarkTheme = useDarkTheme, fontOption = fontOption, textScale = textScale) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            CafinetNavGraph()
        }
    }
}
