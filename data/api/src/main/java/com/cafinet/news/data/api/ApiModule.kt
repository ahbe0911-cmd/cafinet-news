package com.cafinet.news.data.api

import com.cafinet.news.data.api.telegram.TelegramPublicDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(source: TelegramPublicDataSource): NewsRemoteDataSource = source
}
