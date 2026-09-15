package com.cafinet.news.data.api.mock

import com.cafinet.news.data.api.NewsRemoteDataSource
import com.cafinet.news.data.api.dto.NewsDto
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temporary stand-in for [com.cafinet.news.data.api.NewsApiService] until the
 * real Telegram-fed backend is connected. Returns the same DTO shape the
 * real API is expected to return, so swapping this out later only touches
 * one @Provides method in ApiModule, not any domain/presentation code.
 */
@Singleton
class MockNewsDataSource @Inject constructor() : NewsRemoteDataSource {

    override suspend fun getNews(): List<NewsDto> {
        delay(400) // simulate network latency
        return sampleNews
    }

    override suspend fun searchNews(query: String): List<NewsDto> {
        delay(250)
        return sampleNews.filter {
            it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
    }

    private val sampleNews = listOf(
        NewsDto(
            id = 1,
            title = "رونمایی از نسل جدید پردازنده‌های موبایل",
            description = "شرکت‌های بزرگ فناوری از تراشه‌های جدیدی رونمایی کردند که وعده کارایی و مصرف انرژی بهتر را می‌دهند.",
            imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475",
            videoUrl = null,
            category = "فناوری",
            source = "کافی‌نت تک",
            publishedAt = "1405/06/25",
            viewCount = 1240,
        ),
        NewsDto(
            id = 2,
            title = "تحلیل بازار ارزهای دیجیتال در هفته اخیر",
            description = "نوسانات اخیر بازار ارزهای دیجیتال و پیش‌بینی تحلیلگران برای هفته پیش رو بررسی می‌شود.",
            imageUrl = "https://images.unsplash.com/photo-1621761191319-c6fb62004040",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = "اقتصاد",
            source = "کافی‌نت اقتصادی",
            publishedAt = "1405/06/24",
            viewCount = 3420,
        ),
        NewsDto(
            id = 3,
            title = "نتایج نیمه‌نهایی مسابقات فوتبال باشگاهی",
            description = "خلاصه‌ای از دیدارهای نیمه‌نهایی و ترکیب تیم‌های راه‌یافته به فینال.",
            imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2",
            videoUrl = null,
            category = "ورزشی",
            source = "کافی‌نت ورزش",
            publishedAt = "1405/06/23",
            viewCount = 8760,
        ),
        NewsDto(
            id = 4,
            title = "معرفی جدیدترین دستاوردهای هوش مصنوعی",
            description = "بررسی مدل‌های زبانی تازه و کاربردهای آن‌ها در صنایع مختلف.",
            imageUrl = "https://images.unsplash.com/photo-1677442136019-21780ecad995",
            videoUrl = null,
            category = "فناوری",
            source = "کافی‌نت تک",
            publishedAt = "1405/06/22",
            viewCount = 5310,
        ),
        NewsDto(
            id = 5,
            title = "گزارش آب و هوایی هفته پیش رو",
            description = "پیش‌بینی هواشناسی و هشدارهای احتمالی برای استان‌های شمالی کشور.",
            imageUrl = "https://images.unsplash.com/photo-1504608524841-42fe6f032b4b",
            videoUrl = null,
            category = "عمومی",
            source = "کافی‌نت",
            publishedAt = "1405/06/21",
            viewCount = 980,
        ),
    )
}
