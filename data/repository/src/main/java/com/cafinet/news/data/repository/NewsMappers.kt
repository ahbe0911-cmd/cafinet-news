package com.cafinet.news.data.repository

import com.cafinet.news.data.api.dto.NewsDto
import com.cafinet.news.data.local.NewsEntity
import com.cafinet.news.domain.model.News

fun NewsDto.toEntity(): NewsEntity = NewsEntity(
    id = id,
    telegramMessageId = telegramMessageId,
    channelUsername = channelUsername,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    publishedAtEpochMillis = publishedAtEpochMillis,
    viewCount = viewCount,
    postUrl = postUrl,
)

fun NewsEntity.toDomain(): News = News(
    id = id,
    telegramMessageId = telegramMessageId,
    channelUsername = channelUsername,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    publishedAtEpochMillis = publishedAtEpochMillis,
    viewCount = viewCount,
    postUrl = postUrl,
)

fun NewsDto.toDomain(): News = News(
    id = id,
    telegramMessageId = telegramMessageId,
    channelUsername = channelUsername,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    publishedAtEpochMillis = publishedAtEpochMillis,
    viewCount = viewCount,
    postUrl = postUrl,
)
