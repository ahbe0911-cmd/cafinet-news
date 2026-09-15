package com.cafinet.news.data.api

import com.cafinet.news.data.api.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit contract for the future news backend (fed from Telegram channels).
 * Real base URL is injected via core:network's BuildConfig.BASE_URL - no
 * secrets/keys are hardcoded here. While the backend isn't live, a
 * [com.cafinet.news.data.api.mock.MockNewsDataSource] is used instead.
 */
interface NewsApiService {

    @GET("api/news")
    suspend fun getNews(): List<NewsDto>

    @GET("api/news/search")
    suspend fun searchNews(@Query("q") query: String): List<NewsDto>
}
