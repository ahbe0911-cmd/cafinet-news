package com.cafinet.news.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.cafinet.news.domain.usecase.RefreshNewsUseCase
import com.cafinet.news.core.common.PartialTelegramFetchException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.cafinet.news.core.common.Result as AppResult

/**
 * Periodic background sync so the feed has fresh content even before the
 * user opens the app. Scheduled from MainActivity via WorkManager's unique
 * periodic work API.
 */
@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshNewsUseCase: RefreshNewsUseCase,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): ListenableWorker.Result {
        return when (val result = refreshNewsUseCase()) {
            is AppResult.Success -> ListenableWorker.Result.success()
            is AppResult.Error -> if (result.throwable is PartialTelegramFetchException) {
                ListenableWorker.Result.success()
            } else {
                ListenableWorker.Result.retry()
            }
            AppResult.Loading -> ListenableWorker.Result.retry()
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "news_sync_work"
    }
}
