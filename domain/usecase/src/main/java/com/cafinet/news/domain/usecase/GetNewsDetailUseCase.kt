package com.cafinet.news.domain.usecase

import com.cafinet.news.core.common.Result
import com.cafinet.news.domain.model.News
import com.cafinet.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsDetailUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(id: Long): Result<News> = repository.getNewsById(id)
}
