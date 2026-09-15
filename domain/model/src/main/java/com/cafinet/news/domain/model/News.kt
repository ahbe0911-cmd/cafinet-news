package com.cafinet.news.domain.model

/**
 * Core domain entity for a news item. Framework-agnostic on purpose (no
 * Room/Retrofit annotations here) so it can be shared cleanly across
 * data/domain/presentation layers per Clean Architecture.
 */
data class News(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    val category: String,
    val source: String,
    val publishedAt: String,
    val viewCount: Int = 0,
)

/** Lightweight category used for the horizontal filter chips on the Home feed. */
data class NewsCategory(
    val id: String,
    val title: String,
)
