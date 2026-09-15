package com.cafinet.news.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafinet.news.core.common.Result
import com.cafinet.news.domain.usecase.GetNewsDetailUseCase
import com.cafinet.news.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNewsDetail: GetNewsDetailUseCase,
) : ViewModel() {

    private val newsId: Long = checkNotNull(savedStateHandle[Destination.NewsDetail.ARG_NEWS_ID])

    private val _uiState = MutableStateFlow(NewsDetailUiState())
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = getNewsDetail(newsId)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, news = result.data) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                Result.Loading -> Unit
            }
        }
    }
}
