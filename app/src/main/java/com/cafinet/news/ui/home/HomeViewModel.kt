package com.cafinet.news.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafinet.news.core.common.Result
import com.cafinet.news.core.database.PreferencesManager
import com.cafinet.news.domain.usecase.GetNewsFeedUseCase
import com.cafinet.news.domain.usecase.RefreshNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsFeed: GetNewsFeedUseCase,
    private val refreshNews: RefreshNewsUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var refreshJob: Job? = null

    init {
        observeFeed()
        observeChannels()
    }

    private fun observeFeed() {
        viewModelScope.launch {
            getNewsFeed().collect { newsList ->
                val availableCategories = newsList
                    .map { it.category }
                    .distinct()
                    .filter(String::isNotBlank)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        news = newsList,
                        categories = availableCategories,
                        selectedCategory = it.selectedCategory?.takeIf(availableCategories::contains),
                    )
                }
            }
        }
    }

    private fun observeChannels() {
        viewModelScope.launch {
            preferencesManager.telegramChannels.distinctUntilChanged().collect { channels ->
                _uiState.update { it.copy(telegramChannels = channels) }
                refresh()
            }
        }
    }

    fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            when (val result = refreshNews()) {
                is Result.Error -> _uiState.update {
                    it.copy(isRefreshing = false, isLoading = false, errorMessage = result.message ?: "خطا در دریافت اخبار")
                }
                else -> _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
