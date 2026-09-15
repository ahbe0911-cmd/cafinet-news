package com.cafinet.news.domain.usecase

import com.cafinet.news.core.common.Result
import com.cafinet.news.domain.repository.NewsRepository
import javax.inject.Inject

/** Triggers a remote fetch and refreshes the local cache. Used on pull-to-refresh and by WorkManager sync. */
class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshNews()
}
