package com.cafinet.news.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class AppTextScale(val multiplier: Float) {
    SMALL(0.9f),
    MEDIUM(1.0f),
    LARGE(1.15f),
}

/** Builds Material3 [Typography] scaled by the user's text-size preference and font choice. */
fun buildAppTypography(fontFamily: FontFamily, scale: AppTextScale): Typography {
    val m = scale.multiplier
    return Typography(
        headlineSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = (24 * m).sp, lineHeight = (30 * m).sp),
        titleLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = (20 * m).sp, lineHeight = (26 * m).sp),
        titleMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = (17 * m).sp, lineHeight = (23 * m).sp),
        titleSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = (14 * m).sp, lineHeight = (20 * m).sp),
        bodyLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = (16 * m).sp, lineHeight = (24 * m).sp),
        bodyMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = (14 * m).sp, lineHeight = (21 * m).sp),
        bodySmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = (12 * m).sp, lineHeight = (18 * m).sp),
        labelLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = (14 * m).sp, lineHeight = (20 * m).sp),
        labelMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = (12 * m).sp, lineHeight = (16 * m).sp),
        labelSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = (11 * m).sp, lineHeight = (15 * m).sp),
    )
}
