package com.cafinet.news.core.common

private val telegramUsernamePattern = Regex("^[A-Za-z0-9_]{5,32}$")

/**
 * Turns an @username or a public t.me link into Telegram's canonical username.
 * Private invite links (+hash / joinchat) deliberately return null.
 */
fun normalizeTelegramUsername(rawValue: String): String? {
    var value = rawValue.trim()
    if (value.isBlank()) return null

    value = value
        .removePrefix("https://")
        .removePrefix("http://")
        .removePrefix("www.")
        .removePrefix("telegram.me/")
        .removePrefix("t.me/")
        .removePrefix("s/")
        .substringBefore('?')
        .substringBefore('#')
        .substringBefore('/')
        .removePrefix("@")
        .trim()

    if (value.startsWith('+') || value.equals("joinchat", ignoreCase = true)) return null
    return value.takeIf(telegramUsernamePattern::matches)?.lowercase()
}

fun telegramPublicUrl(username: String): String = "https://t.me/${normalizeTelegramUsername(username) ?: username}"

/** Refresh produced new data for at least one channel, but one or more sources failed. */
class PartialTelegramFetchException(message: String) : Exception(message)
