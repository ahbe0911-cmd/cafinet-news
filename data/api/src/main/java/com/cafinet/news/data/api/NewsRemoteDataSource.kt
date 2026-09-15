package com.cafinet.news.data.api

import com.cafinet.news.data.api.dto.NewsDto

interface NewsRemoteDataSource {
    suspend fun getNews(channels: List<String>): TelegramFetchResult
}

data class TelegramFetchResult(
    val news: List<NewsDto>,
    val successfulChannels: Set<String>,
    val failedChannels: Map<String, String>,
)
