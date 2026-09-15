package com.cafinet.news.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cafinet.news.R
import com.cafinet.news.core.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    onBackClick: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.news?.category ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
                actions = {
                    uiState.news?.let { news ->
                        IconButton(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    listOf(news.title, news.description, news.postUrl)
                                        .filter { it.isNotBlank() }
                                        .joinToString("\n\n"),
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share)))
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = stringResourceCompat(R.string.share))
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            uiState.news == null -> Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "خطایی رخ داد", color = MaterialTheme.colorScheme.error)
            }

            else -> {
                val news = uiState.news!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                ) {
                    val videoUrl = news.videoUrl
                    if (!videoUrl.isNullOrBlank()) {
                        VideoPlayer(videoUrl = videoUrl)
                    } else if (news.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = news.imageUrl,
                            contentDescription = news.title,
                            modifier = Modifier.fillMaxWidth().padding(0.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Column(modifier = Modifier.padding(AppSpacing.lg)) {
                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Text(
                            text = "${news.source} · ${news.publishedAt}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = AppSpacing.sm),
                        )

                        if (news.description.isNotBlank()) {
                            Text(
                                text = news.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = AppSpacing.lg),
                            )
                        }

                        FilledTonalButton(
                            onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(news.postUrl)))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = AppSpacing.xl),
                        ) {
                            Icon(Icons.Filled.OpenInNew, contentDescription = null)
                            Text(
                                text = stringResourceCompat(R.string.open_in_telegram),
                                modifier = Modifier.padding(start = AppSpacing.sm),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun stringResourceCompat(id: Int) = androidx.compose.ui.res.stringResource(id)
