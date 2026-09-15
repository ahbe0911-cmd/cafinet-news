package com.cafinet.news.data.api

import com.cafinet.news.data.api.dto.NewsDto

/**
 * Abstraction the repository depends on. Two implementations exist:
 *  - [com.cafinet.news.data.api.mock.MockNewsDataSource] (active today)
 *  - a future `RemoteNewsDataSource` backed by [NewsApiService] once the
 *    Telegram-fed backend is live.
 * Swapping which one is bound happens in a single Hilt @Provides method
 * (see ApiModule) — no other code needs to change.
 */
interface NewsRemoteDataSource {
    suspend fun getNews(): List<NewsDto>
    suspend fun searchNews(query: String): List<NewsDto>
}
