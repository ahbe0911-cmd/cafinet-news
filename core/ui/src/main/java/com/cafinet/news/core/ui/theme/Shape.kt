package com.cafinet.news.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Standard spacing scale used throughout Cafinet News for consistent, calm layout. */
object AppSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}

/** Card corner radius per design spec (~20dp, "media-app" feel). */
val NewsCardRadius = 20.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(NewsCardRadius),
    extraLarge = RoundedCornerShape(28.dp),
)
