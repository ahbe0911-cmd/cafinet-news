package com.cafinet.news.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafinet.news.core.database.AppFontFamily
import com.cafinet.news.core.database.AppThemeMode
import com.cafinet.news.core.database.AppTextSize
import com.cafinet.news.core.database.PreferencesManager
import com.cafinet.news.core.database.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences> = preferencesManager.userPreferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserPreferences(),
    )

    fun onThemeSelected(mode: AppThemeMode) {
        viewModelScope.launch { preferencesManager.setThemeMode(mode) }
    }

    fun onFontSelected(font: AppFontFamily) {
        viewModelScope.launch { preferencesManager.setFontFamily(font) }
    }

    fun onTextSizeSelected(size: AppTextSize) {
        viewModelScope.launch { preferencesManager.setTextSize(size) }
    }
}
