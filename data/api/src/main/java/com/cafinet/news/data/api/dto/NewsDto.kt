package com.cafinet.news.data.api.dto

data class NewsDto(
    val id: Long,
    val telegramMessageId: Long,
    val channelUsername: String,
    val title: String,
    val description: String = "",
    val imageUrl: String = "",
    val videoUrl: String? = null,
    val category: String = "",
    val source: String,
    val publishedAt: String = "",
    val publishedAtEpochMillis: Long = 0L,
    val viewCount: Int = 0,
    val postUrl: String,
)
