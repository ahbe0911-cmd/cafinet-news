package com.cafinet.news.ui.home

import com.cafinet.news.domain.model.News

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val news: List<News> = emptyList(),
    val telegramChannels: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null,
) {
    val filteredNews: List<News>
        get() = news.filter { item ->
            (selectedCategory == null || item.category == selectedCategory) &&
                (searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true) ||
                    item.source.contains(searchQuery, ignoreCase = true) ||
                    item.channelUsername.contains(searchQuery.removePrefix("@"), ignoreCase = true))
        }
}
