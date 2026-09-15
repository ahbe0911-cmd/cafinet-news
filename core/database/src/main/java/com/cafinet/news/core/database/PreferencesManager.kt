package com.cafinet.news.core.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "cafinet_settings")

enum class AppThemeMode { LIGHT, DARK, SYSTEM }
enum class AppFontFamily { VAZIRMATN, NAZANIN, TITR }
enum class AppTextSize { SMALL, MEDIUM, LARGE }

data class UserPreferences(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val fontFamily: AppFontFamily = AppFontFamily.VAZIRMATN,
    val textSize: AppTextSize = AppTextSize.MEDIUM,
)

/**
 * Single source of truth for user-configurable app settings (theme, font,
 * text size). Backed by Jetpack DataStore so preferences survive process
 * death and are observed reactively via Flow.
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val TEXT_SIZE = stringPreferencesKey("text_size")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            themeMode = prefs[Keys.THEME_MODE]?.let { runCatching { AppThemeMode.valueOf(it) }.getOrNull() }
                ?: AppThemeMode.SYSTEM,
            fontFamily = prefs[Keys.FONT_FAMILY]?.let { runCatching { AppFontFamily.valueOf(it) }.getOrNull() }
                ?: AppFontFamily.VAZIRMATN,
            textSize = prefs[Keys.TEXT_SIZE]?.let { runCatching { AppTextSize.valueOf(it) }.getOrNull() }
                ?: AppTextSize.MEDIUM,
        )
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setFontFamily(font: AppFontFamily) {
        context.dataStore.edit { it[Keys.FONT_FAMILY] = font.name }
    }

    suspend fun setTextSize(size: AppTextSize) {
        context.dataStore.edit { it[Keys.TEXT_SIZE] = size.name }
    }
}
