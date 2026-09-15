package com.cafinet.news.domain.usecase

import com.cafinet.news.domain.model.News
import com.cafinet.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Exposes the reactive, cache-backed news feed for the Home screen. */
class GetNewsFeedUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    operator fun invoke(): Flow<List<News>> = repository.observeNews()
}
