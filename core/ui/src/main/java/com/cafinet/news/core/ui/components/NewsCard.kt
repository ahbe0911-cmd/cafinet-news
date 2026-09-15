package com.cafinet.news.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import coil.compose.AsyncImage
import com.cafinet.news.core.ui.theme.AppSpacing
import com.cafinet.news.core.ui.theme.NewsCardRadius
import com.cafinet.news.domain.model.News

/**
 * Instagram-feed-style news card: large image, gradient scrim for
 * readability, category tag, title, summary, timestamp and view count.
 */
@Composable
fun NewsCard(
    news: News,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(NewsCardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .clip(RoundedCornerShape(topStart = NewsCardRadius, topEnd = NewsCardRadius)),
        ) {
            if (news.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = news.imageUrl,
                    contentDescription = news.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Forum,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.size(52.dp),
                    )
                }
            }

            // Gradient scrim so category chip / play icon stay legible over any photo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)),
                        ),
                    ),
            )

            Surface(
                modifier = Modifier
                    .padding(AppSpacing.md)
                    .align(Alignment.TopStart),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    text = news.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
                )
            }

            if (!news.videoUrl.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(AppSpacing.md)
                        .clip(RoundedCornerShape(50)),
                )
            }
        }

        Column(modifier = Modifier.padding(AppSpacing.lg)) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
            )

            if (news.description.isNotBlank()) {
                Text(
                    text = news.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    modifier = Modifier.padding(top = AppSpacing.xs),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = news.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = " · ${news.publishedAt}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (news.viewCount > 0) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RemoveRedEye,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = AppSpacing.xs),
                        )
                        Text(
                            text = news.viewCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/** Horizontal, scrollable category filter chip. */
@Composable
fun CategoryChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
        )
    }
}

internal val CardContentPadding = PaddingValues(AppSpacing.lg)
