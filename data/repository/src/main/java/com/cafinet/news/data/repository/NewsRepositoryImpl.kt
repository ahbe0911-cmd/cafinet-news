package com.cafinet.news.data.repository

import com.cafinet.news.core.common.Result
import com.cafinet.news.data.api.NewsRemoteDataSource
import com.cafinet.news.data.local.NewsDao
import com.cafinet.news.domain.model.News
import com.cafinet.news.domain.model.NewsCategory
import com.cafinet.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Offline-first implementation: Room ([NewsDao]) is the single source of
 * truth consumed by the UI; [NewsRemoteDataSource] (mock today, real
 * backend later) is only used to refresh that cache.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsDao: NewsDao,
) : NewsRepository {

    override fun observeNews(): Flow<List<News>> =
        newsDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshNews(): Result<Unit> = runCatching {
        val remote = remoteDataSource.getNews()
        newsDao.upsertAll(remote.map { it.toEntity() })
    }.fold(
        onSuccess = { Result.Success(Unit) },
        onFailure = { Result.Error(it) },
    )

    override suspend fun getNewsById(id: Long): Result<News> = runCatching {
        newsDao.getById(id)?.toDomain()
            ?: error("خبر مورد نظر یافت نشد")
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) },
    )

    override suspend fun getCategories(): Result<List<NewsCategory>> = runCatching {
        newsDao.observeAll()
        // Categories are derived from cached news until a dedicated
        // /api/categories endpoint exists on the backend.
        val all = remoteDataSource.getNews()
        all.map { it.category }.distinct().filter { it.isNotBlank() }.map { NewsCategory(id = it, title = it) }
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) },
    )

    override suspend fun searchNews(query: String): Result<List<News>> = runCatching {
        newsDao.search(query).map { it.toDomain() }
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) },
    )
}
