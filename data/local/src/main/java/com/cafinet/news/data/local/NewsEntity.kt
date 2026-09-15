package com.cafinet.news.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room cache entity, mirrors the domain News model but stays local-only. */
@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String?,
    val category: String,
    val source: String,
    val publishedAt: String,
    val viewCount: Int,
    /** epoch millis this row was cached, used for future cache-expiry policies */
    val cachedAt: Long = System.currentTimeMillis(),
)
