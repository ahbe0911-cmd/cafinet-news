package com.cafinet.news.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.cafinet.news.R
import com.cafinet.news.core.database.AppFontFamily
import com.cafinet.news.core.database.AppTextSize
import com.cafinet.news.core.database.AppThemeMode
import com.cafinet.news.core.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val channelUiState by viewModel.channelUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null)
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
        ) {
            SettingsGroup(title = "کانال‌های عمومی تلگرام") {
                Text(
                    text = "نام کاربری مثل @mehrnews یا لینک عمومی t.me را وارد کنید. کانال‌های خصوصی پشتیبانی نمی‌شوند.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = AppSpacing.sm),
                )

                OutlinedTextField(
                    value = channelUiState.input,
                    onValueChange = viewModel::onChannelInputChanged,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("@username یا لینک کانال") },
                    isError = channelUiState.errorMessage != null,
                    supportingText = channelUiState.errorMessage?.let { message ->
                        { Text(message) }
                    },
                )

                Button(
                    onClick = viewModel::addChannel,
                    enabled = channelUiState.input.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppSpacing.sm),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text("افزودن کانال", modifier = Modifier.padding(start = AppSpacing.sm))
                }

                if (prefs.telegramChannels.isEmpty()) {
                    Text(
                        text = "فهرست کانال‌ها خالی است.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = AppSpacing.lg),
                    )
                } else {
                    prefs.telegramChannels.forEach { username ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = AppSpacing.sm),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "@$username", style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = "t.me/$username",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            IconButton(onClick = { viewModel.removeChannel(username) }) {
                                Icon(
                                    Icons.Filled.DeleteOutline,
                                    contentDescription = "حذف @$username",
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }

                TextButton(
                    onClick = viewModel::restoreDefaultChannels,
                    modifier = Modifier.padding(top = AppSpacing.xs),
                ) {
                    Icon(Icons.Filled.Restore, contentDescription = null)
                    Text("بازگردانی کانال‌های پیش‌فرض", modifier = Modifier.padding(start = AppSpacing.sm))
                }
            }

            SettingsGroup(title = "پوسته برنامه") {
                RadioOption("روشن", prefs.themeMode == AppThemeMode.LIGHT) { viewModel.onThemeSelected(AppThemeMode.LIGHT) }
                RadioOption("تیره", prefs.themeMode == AppThemeMode.DARK) { viewModel.onThemeSelected(AppThemeMode.DARK) }
                RadioOption("سیستم", prefs.themeMode == AppThemeMode.SYSTEM) { viewModel.onThemeSelected(AppThemeMode.SYSTEM) }
            }

            SettingsGroup(title = stringResource(R.string.font_family)) {
                RadioOption("وزیرمتن", prefs.fontFamily == AppFontFamily.VAZIRMATN) { viewModel.onFontSelected(AppFontFamily.VAZIRMATN) }
                RadioOption("نازنین", prefs.fontFamily == AppFontFamily.NAZANIN) { viewModel.onFontSelected(AppFontFamily.NAZANIN) }
                RadioOption("تیتر", prefs.fontFamily == AppFontFamily.TITR) { viewModel.onFontSelected(AppFontFamily.TITR) }
            }

            SettingsGroup(title = stringResource(R.string.text_size)) {
                RadioOption(stringResource(R.string.text_size_small), prefs.textSize == AppTextSize.SMALL) { viewModel.onTextSizeSelected(AppTextSize.SMALL) }
                RadioOption(stringResource(R.string.text_size_medium), prefs.textSize == AppTextSize.MEDIUM) { viewModel.onTextSizeSelected(AppTextSize.MEDIUM) }
                RadioOption(stringResource(R.string.text_size_large), prefs.textSize == AppTextSize.LARGE) { viewModel.onTextSizeSelected(AppTextSize.LARGE) }
            }
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppSpacing.sm)
                .selectableGroup(),
        ) {
            content()
        }
    }
}

@Composable
private fun RadioOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect)
            .padding(vertical = AppSpacing.xs),
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = AppSpacing.sm),
        )
    }
}
