package com.cafinet.news.data.api.remote

import com.cafinet.news.data.api.NewsApiService
import com.cafinet.news.data.api.NewsRemoteDataSource
import com.cafinet.news.data.api.dto.NewsDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real implementation backed by Retrofit's [NewsApiService]. Not yet bound
 * as the active [NewsRemoteDataSource] in ApiModule (mock is active until
 * the Telegram-fed backend goes live) — flip the @Provides method there to
 * switch over.
 */
@Singleton
class RemoteNewsDataSource @Inject constructor(
    private val api: NewsApiService,
) : NewsRemoteDataSource {
    override suspend fun getNews(): List<NewsDto> = api.getNews()
    override suspend fun searchNews(query: String): List<NewsDto> = api.searchNews(query)
}
