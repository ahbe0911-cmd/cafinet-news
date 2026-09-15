package com.cafinet.news.data.api

import com.cafinet.news.data.api.mock.MockNewsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Single switch point between mock and real backend.
 * When the Telegram-fed backend is ready: replace `mock` with a
 * `RemoteNewsDataSource` instance (inject NewsApiService) here — nothing
 * else in the app needs to change.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(mock: MockNewsDataSource): NewsRemoteDataSource = mock
}
