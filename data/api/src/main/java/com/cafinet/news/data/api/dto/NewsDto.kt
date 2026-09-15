package com.cafinet.news.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Raw API response shape for GET /api/news.
 * Matches the JSON contract agreed with the future Telegram-based backend:
 * {
 *   "id": 1, "title": "...", "description": "...", "imageUrl": "...",
 *   "videoUrl": "...", "category": "...", "source": "...", "publishedAt": "1405/06/25"
 * }
 */
@Serializable
data class NewsDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "",
    @SerialName("imageUrl") val imageUrl: String = "",
    @SerialName("videoUrl") val videoUrl: String? = null,
    @SerialName("category") val category: String = "",
    @SerialName("source") val source: String = "کافی‌نت",
    @SerialName("publishedAt") val publishedAt: String = "",
    @SerialName("viewCount") val viewCount: Int = 0,
)
