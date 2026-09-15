package com.cafinet.news.data.api.telegram

import com.cafinet.news.data.api.dto.NewsDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

internal class TelegramPageParser(
    private val nowMillis: () -> Long = System::currentTimeMillis,
) {
    fun parse(html: String, requestedUsername: String): ParsedTelegramChannel {
        val document = Jsoup.parse(html, "https://t.me/")
        val channelTitle = document
            .selectFirst(".tgme_channel_info_header_title span, .tgme_channel_info_header_title")
            ?.text()
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?: document.selectFirst(".tgme_widget_message_owner_name span")?.text()?.trim()
            ?: "@$requestedUsername"

        val posts = document.select(".tgme_widget_message[data-post]").mapNotNull { element ->
            parsePost(element, requestedUsername, channelTitle)
        }

        return ParsedTelegramChannel(
            username = requestedUsername,
            title = channelTitle,
            posts = posts,
        )
    }

    private fun parsePost(
        element: Element,
        requestedUsername: String,
        channelTitle: String,
    ): NewsDto? {
        val dataPost = element.attr("data-post").trim()
        val messageId = dataPost.substringAfterLast('/').toLongOrNull() ?: return null
        val canonicalPost = dataPost.takeIf { it.contains('/') } ?: "$requestedUsername/$messageId"
        val postUrl = element.selectFirst("a.tgme_widget_message_date[href]")
            ?.absUrl("href")
            ?.takeIf(String::isNotBlank)
            ?: "https://t.me/$canonicalPost"

        val rawText = element.selectFirst(".tgme_widget_message_text")
            ?.wholeText()
            .orEmpty()
            .cleanTelegramText()

        val videoUrl = element.selectFirst("video[src], source[src]")
            ?.absUrl("src")
            ?.takeIf(String::isNotBlank)
        val imageUrl = extractImageUrl(element)
        val textParts = splitText(
            text = rawText,
            fallback = when {
                !videoUrl.isNullOrBlank() -> "ویدئوی جدید از $channelTitle"
                imageUrl.isNotBlank() -> "تصویر جدید از $channelTitle"
                else -> "پست جدید از $channelTitle"
            },
        )

        val publishedAtEpochMillis = parseDate(
            element.selectFirst("time[datetime]")?.attr("datetime").orEmpty(),
        )

        return NewsDto(
            id = stableId(canonicalPost.lowercase()),
            telegramMessageId = messageId,
            channelUsername = requestedUsername,
            title = textParts.title,
            description = textParts.description,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            category = "@$requestedUsername",
            source = channelTitle,
            publishedAt = formatRelativeTime(publishedAtEpochMillis),
            publishedAtEpochMillis = publishedAtEpochMillis,
            viewCount = parseViews(element.selectFirst(".tgme_widget_message_views")?.text().orEmpty()),
            postUrl = postUrl,
        )
    }

    private fun extractImageUrl(element: Element): String {
        val styledMedia = element.select(
            ".tgme_widget_message_photo_wrap, " +
                ".tgme_widget_message_video_thumb, " +
                ".tgme_widget_message_roundvideo_thumb",
        )
        styledMedia.forEach { media ->
            backgroundUrlRegex.find(media.attr("style"))
                ?.groupValues
                ?.getOrNull(1)
                ?.takeIf(String::isNotBlank)
                ?.let { return it }
        }

        return element.selectFirst(".tgme_widget_message_photo_wrap img[src], video[poster]")
            ?.let { media ->
                media.absUrl(if (media.hasAttr("poster")) "poster" else "src")
            }
            ?.takeIf(String::isNotBlank)
            .orEmpty()
    }

    private fun splitText(text: String, fallback: String): TextParts {
        if (text.isBlank()) return TextParts(title = fallback, description = "")

        val lines = text.lineSequence().map(String::trim).filter(String::isNotBlank).toList()
        val firstLine = lines.firstOrNull().orEmpty()
        val title = firstLine.smartEllipsize(TITLE_LIMIT)
        val remainder = when {
            lines.size > 1 -> lines.drop(1).joinToString("\n")
            firstLine.length > TITLE_LIMIT -> firstLine.drop(title.removeSuffix("…").length).trim()
            else -> ""
        }
        return TextParts(title = title, description = remainder)
    }

    private fun parseDate(rawValue: String): Long {
        if (rawValue.isBlank()) return nowMillis()
        datePatterns.forEach { pattern ->
            runCatching {
                SimpleDateFormat(pattern, Locale.ENGLISH).apply { isLenient = false }.parse(rawValue)?.time
            }.getOrNull()?.let { return it }
        }
        return nowMillis()
    }

    private fun formatRelativeTime(epochMillis: Long): String {
        val difference = (nowMillis() - epochMillis).coerceAtLeast(0L)
        val label = when {
            difference < 60_000L -> "همین حالا"
            difference < 3_600_000L -> "${difference / 60_000L} دقیقه پیش"
            difference < 86_400_000L -> "${difference / 3_600_000L} ساعت پیش"
            difference < 172_800_000L -> "دیروز"
            else -> SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).format(epochMillis)
        }
        return label.toPersianDigits()
    }

    private fun parseViews(rawValue: String): Int {
        val normalized = rawValue
            .toEnglishDigits()
            .replace(",", "")
            .trim()
            .uppercase(Locale.ENGLISH)
        val match = viewCountRegex.find(normalized) ?: return 0
        val value = match.groupValues[1].toDoubleOrNull() ?: return 0
        val multiplier = when (match.groupValues[2]) {
            "K" -> 1_000
            "M" -> 1_000_000
            "B" -> 1_000_000_000
            else -> 1
        }
        return (value * multiplier).coerceAtMost(Int.MAX_VALUE.toDouble()).roundToInt()
    }

    private fun stableId(value: String): Long {
        val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
        return ByteBuffer.wrap(digest).long and Long.MAX_VALUE
    }

    private data class TextParts(val title: String, val description: String)

    private companion object {
        const val TITLE_LIMIT = 110

        val backgroundUrlRegex = Regex(
            pattern = """background-image\s*:\s*url\(['\"]?(.+?)['\"]?\)""",
            option = RegexOption.IGNORE_CASE,
        )
        val viewCountRegex = Regex("""([0-9]+(?:\.[0-9]+)?)\s*([KMB]?)""")
        val datePatterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "EEE, dd MMM yyyy HH:mm:ss Z",
        )
    }
}

internal data class ParsedTelegramChannel(
    val username: String,
    val title: String,
    val posts: List<NewsDto>,
)

private fun String.cleanTelegramText(): String =
    replace('\u00A0', ' ')
        .replace(Regex("[ \\t]+\\n"), "\n")
        .replace(Regex("\\n{3,}"), "\n\n")
        .trim()

private fun String.smartEllipsize(limit: Int): String {
    if (length <= limit) return this
    val candidate = take(limit).substringBeforeLast(' ').trim()
    return "${candidate.takeIf { it.length >= limit / 2 } ?: take(limit).trim()}…"
}

private fun String.toPersianDigits(): String = map { character ->
    when (character) {
        in '0'..'9' -> "۰۱۲۳۴۵۶۷۸۹"[character - '0']
        else -> character
    }
}.joinToString("")

private fun String.toEnglishDigits(): String = map { character ->
    when (character) {
        in '۰'..'۹' -> "0123456789"[character - '۰']
        in '٠'..'٩' -> "0123456789"[character - '٠']
        else -> character
    }
}.joinToString("")
