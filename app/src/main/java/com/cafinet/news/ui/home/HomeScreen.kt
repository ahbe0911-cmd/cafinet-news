package com.cafinet.news.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cafinet.news.R
import com.cafinet.news.core.ui.components.CategoryChip
import com.cafinet.news.core.ui.components.NewsCard
import com.cafinet.news.core.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewsClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(stringResourceCompat(R.string.app_name))
                        Text(
                            text = "${uiState.telegramChannels.size} کانال عمومی",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh, enabled = !uiState.isRefreshing) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.Refresh, contentDescription = stringResourceCompat(R.string.refresh))
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResourceCompat(R.string.settings))
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
                placeholder = { Text(stringResourceCompat(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = if (uiState.searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Filled.Close, contentDescription = stringResourceCompat(R.string.clear_search))
                        }
                    }
                } else {
                    null
                },
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                ),
            )

            if (uiState.categories.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    contentPadding = PaddingValues(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
                ) {
                    item {
                        CategoryChip(
                            title = stringResourceCompat(R.string.all_channels),
                            selected = uiState.selectedCategory == null,
                            onClick = { viewModel.onCategorySelected(null) },
                        )
                    }
                    items(uiState.categories) { category ->
                        CategoryChip(
                            title = category,
                            selected = uiState.selectedCategory == category,
                            onClick = { viewModel.onCategorySelected(category) },
                        )
                    }
                }
            }

            uiState.errorMessage?.let { message ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.xs),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.errorContainer,
                ) {
                    Column(modifier = Modifier.padding(AppSpacing.md)) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            TextButton(onClick = viewModel::dismissError) {
                                Text(stringResourceCompat(R.string.dismiss))
                            }
                            TextButton(onClick = viewModel::refresh) {
                                Text(stringResourceCompat(R.string.retry))
                            }
                        }
                    }
                }
            }

            when {
                uiState.telegramChannels.isEmpty() -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppSpacing.xl),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResourceCompat(R.string.no_channels_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResourceCompat(R.string.no_channels_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = AppSpacing.sm),
                    )
                    Button(
                        onClick = onSettingsClick,
                        modifier = Modifier.padding(top = AppSpacing.lg),
                    ) {
                        Text(stringResourceCompat(R.string.manage_channels))
                    }
                }

                uiState.isLoading || (uiState.isRefreshing && uiState.news.isEmpty()) -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                uiState.filteredNews.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (uiState.searchQuery.isBlank()) {
                            stringResourceCompat(R.string.no_posts)
                        } else {
                            stringResourceCompat(R.string.no_search_results)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
                ) {
                    items(uiState.filteredNews, key = { it.id }) { news ->
                        NewsCard(news = news, onClick = { onNewsClick(news.id) })
                    }
                    item { Box(Modifier.padding(bottom = AppSpacing.xl)) }
                }
            }
        }
    }
}

/** Thin wrapper kept local to avoid pulling in extra Compose resource imports at call sites. */
@Composable
private fun stringResourceCompat(id: Int) = androidx.compose.ui.res.stringResource(id)
