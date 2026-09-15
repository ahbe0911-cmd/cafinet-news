package com.cafinet.news.data.repository

import com.cafinet.news.data.api.dto.NewsDto
import com.cafinet.news.data.local.NewsEntity
import com.cafinet.news.domain.model.News

fun NewsDto.toEntity(): NewsEntity = NewsEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    viewCount = viewCount,
)

fun NewsEntity.toDomain(): News = News(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    viewCount = viewCount,
)

fun NewsDto.toDomain(): News = News(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    category = category,
    source = source,
    publishedAt = publishedAt,
    viewCount = viewCount,
)
