package com.cafinet.news.domain.repository

import com.cafinet.news.core.common.Result
import com.cafinet.news.domain.model.News
import com.cafinet.news.domain.model.NewsCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contract for news data access. The domain layer only knows this
 * interface; the concrete implementation (remote + local caching) lives in
 * the `data:repository` module, keeping domain independent of
 * Retrofit/Room.
 */
interface NewsRepository {

    /** Cached-first, reactive stream of the news feed (Room as single source of truth). */
    fun observeNews(): Flow<List<News>>

    /** Triggers a network refresh and writes results into local cache. */
    suspend fun refreshNews(): Result<Unit>

    suspend fun getNewsById(id: Long): Result<News>

    suspend fun getCategories(): Result<List<NewsCategory>>

    suspend fun searchNews(query: String): Result<List<News>>
}
