package com.cafinet.news.domain.usecase

import com.cafinet.news.core.common.Result
import com.cafinet.news.domain.model.NewsCategory
import com.cafinet.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(): Result<List<NewsCategory>> = repository.getCategories()
}
