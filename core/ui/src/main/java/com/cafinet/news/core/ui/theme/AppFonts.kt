package com.cafinet.news.core.ui.theme

import androidx.compose.ui.text.font.FontFamily

/**
 * Maps the user-selectable font choice (Settings screen) to a Compose
 * [FontFamily]. Real .ttf assets are not bundled in this skeleton (see
 * core/ui/src/main/res/font/README.md) so every entry safely falls back to
 * [FontFamily.Default] until the assets are added — swap the values below
 * once the font files exist, e.g.:
 *   FontFamily(Font(R.font.vazirmatn_regular, FontWeight.Normal), ...)
 */
enum class AppFontOption(val displayName: String, val fontFamily: FontFamily) {
    VAZIRMATN("وزیرمتن", FontFamily.Default),
    NAZANIN("نازنین", FontFamily.Default),
    TITR("تیتر", FontFamily.Default),
}
