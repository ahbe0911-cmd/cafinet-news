package com.cafinet.news.data.api.telegram

import com.cafinet.news.core.common.normalizeTelegramUsername
import com.cafinet.news.data.api.NewsRemoteDataSource
import com.cafinet.news.data.api.TelegramFetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramPublicDataSource @Inject constructor(
    private val client: OkHttpClient,
) : NewsRemoteDataSource {
    private val parser = TelegramPageParser()

    override suspend fun getNews(channels: List<String>): TelegramFetchResult = supervisorScope {
        val usernames = channels.mapNotNull(::normalizeTelegramUsername).distinct()
        val results = usernames.map { username ->
            async(Dispatchers.IO) {
                username to runCatching { fetchChannel(username) }.onFailure { error ->
                    if (error is CancellationException) throw error
                }
            }
        }.awaitAll()

        val successful = results.mapNotNull { (username, result) ->
            username.takeIf { result.isSuccess }
        }.toSet()
        val failures = results.mapNotNull { (username, result) ->
            result.exceptionOrNull()?.let { username to it.toUserMessage() }
        }.toMap()
        val news = results.flatMap { (_, result) ->
            result.getOrNull()?.posts.orEmpty()
        }.sortedByDescending { it.publishedAtEpochMillis }

        TelegramFetchResult(
            news = news,
            successfulChannels = successful,
            failedChannels = failures,
        )
    }

    private suspend fun fetchChannel(username: String): ParsedTelegramChannel = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://t.me/s/$username")
            .header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 " +
                    "Chrome/124.0 Mobile Safari/537.36 CafinetNews/1.1",
            )
            .header("Accept", "text/html,application/xhtml+xml")
            .header("Accept-Language", "fa-IR,fa;q=0.9,en;q=0.7")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("پاسخ ${response.code} از تلگرام")
            }
            val html = response.body?.string().orEmpty()
            if (html.isBlank()) throw IOException("پاسخ تلگرام خالی بود")

            val parsed = parser.parse(html, username)
            if (parsed.posts.isEmpty()) {
                throw IOException("پیش‌نمایش عمومی @$username در دسترس نیست")
            }
            parsed
        }
    }

    private fun Throwable.toUserMessage(): String = when (this) {
        is SocketTimeoutException -> "مهلت اتصال تمام شد"
        is IOException -> message ?: "دریافت کانال ناموفق بود"
        else -> "دریافت کانال ناموفق بود"
    }
}
