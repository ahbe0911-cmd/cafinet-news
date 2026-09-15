package com.cafinet.news.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafinet.news.core.database.AppFontFamily
import com.cafinet.news.core.database.AppThemeMode
import com.cafinet.news.core.database.AppTextSize
import com.cafinet.news.core.database.PreferencesManager
import com.cafinet.news.core.database.UserPreferences
import com.cafinet.news.core.common.normalizeTelegramUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _channelUiState = MutableStateFlow(ChannelUiState())
    val channelUiState = _channelUiState.asStateFlow()

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

    fun onChannelInputChanged(value: String) {
        _channelUiState.update { it.copy(input = value, errorMessage = null) }
    }

    fun addChannel() {
        val username = normalizeTelegramUsername(_channelUiState.value.input)
        val channels = userPreferences.value.telegramChannels
        when {
            username == null -> _channelUiState.update {
                it.copy(errorMessage = "نام کاربری یا لینک عمومی تلگرام معتبر نیست")
            }

            username in channels -> _channelUiState.update {
                it.copy(errorMessage = "این کانال قبلاً اضافه شده است")
            }

            channels.size >= PreferencesManager.MAX_TELEGRAM_CHANNELS -> _channelUiState.update {
                it.copy(errorMessage = "حداکثر ${PreferencesManager.MAX_TELEGRAM_CHANNELS} کانال قابل افزودن است")
            }

            else -> viewModelScope.launch {
                preferencesManager.setTelegramChannels(channels + username)
                _channelUiState.update { it.copy(input = "", errorMessage = null) }
            }
        }
    }

    fun removeChannel(username: String) {
        viewModelScope.launch {
            preferencesManager.setTelegramChannels(
                userPreferences.value.telegramChannels.filterNot { it == username },
            )
        }
    }

    fun restoreDefaultChannels() {
        viewModelScope.launch {
            preferencesManager.setTelegramChannels(PreferencesManager.DEFAULT_TELEGRAM_CHANNELS)
            _channelUiState.update { it.copy(errorMessage = null) }
        }
    }
}

data class ChannelUiState(
    val input: String = "",
    val errorMessage: String? = null,
)
