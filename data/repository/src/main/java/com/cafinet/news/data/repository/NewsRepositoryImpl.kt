package com.cafinet.news.data.repository

import com.cafinet.news.core.common.Result
import com.cafinet.news.core.common.PartialTelegramFetchException
import com.cafinet.news.core.database.PreferencesManager
import com.cafinet.news.data.api.NewsRemoteDataSource
import com.cafinet.news.data.local.NewsDao
import com.cafinet.news.domain.model.News
import com.cafinet.news.domain.model.NewsCategory
import com.cafinet.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Offline-first implementation: Room ([NewsDao]) is the single source of
 * truth consumed by the UI; [NewsRemoteDataSource] reads public Telegram
 * previews and only refreshes that cache.
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsDao: NewsDao,
    private val preferencesManager: PreferencesManager,
) : NewsRepository {

    override fun observeNews(): Flow<List<News>> =
        newsDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshNews(): Result<Unit> = runCatching {
        val channels = preferencesManager.telegramChannels.first()
        if (channels.isEmpty()) {
            newsDao.clearAll()
            return@runCatching
        }

        val remote = remoteDataSource.getNews(channels)
        if (remote.successfulChannels.isEmpty()) {
            error(buildFailureMessage(remote.failedChannels, allFailed = true))
        }

        newsDao.deleteInactiveChannels(channels)
        remote.successfulChannels.forEach { channel ->
            newsDao.cacheChannel(
                channelUsername = channel,
                items = remote.news.filter { it.channelUsername == channel }.map { it.toEntity() },
            )
        }

        if (remote.failedChannels.isNotEmpty()) {
            throw PartialTelegramFetchException(
                buildFailureMessage(remote.failedChannels, allFailed = false),
            )
        }
    }.fold(
        onSuccess = { Result.Success(Unit) },
        onFailure = {
            if (it is CancellationException) throw it
            Result.Error(it)
        },
    )

    override suspend fun getNewsById(id: Long): Result<News> = runCatching {
        newsDao.getById(id)?.toDomain()
            ?: error("خبر مورد نظر یافت نشد")
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it) },
    )

    override suspend fun getCategories(): Result<List<NewsCategory>> = runCatching {
        newsDao.getChannelUsernames().map { username ->
            NewsCategory(id = username, title = "@$username")
        }
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

    private fun buildFailureMessage(failures: Map<String, String>, allFailed: Boolean): String {
        val prefix = if (allFailed) {
            "دریافت کانال‌های تلگرام ناموفق بود"
        } else {
            "فید به‌روز شد، اما این کانال‌ها دریافت نشدند"
        }
        val details = failures.entries.joinToString("، ") { (username, reason) -> "@$username ($reason)" }
        return "$prefix: $details"
    }
}
