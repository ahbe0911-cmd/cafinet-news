package com.cafinet.news.ui.detail

import com.cafinet.news.domain.model.News

data class NewsDetailUiState(
    val isLoading: Boolean = true,
    val news: News? = null,
    val errorMessage: String? = null,
)
