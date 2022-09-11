package com.goldmen.android.nasa_apod.di

import com.goldmen.android.nasa_apod.api.ApodService
import com.goldmen.android.nasa_apod.domain.dao.ApodDao
import com.goldmen.android.nasa_apod.domain.repository.ApodRepository
import com.goldmen.android.nasa_apod.domain.repository.ApodRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideApodRepository(appDao: ApodDao, apodService: ApodService): ApodRepository =
        ApodRepositoryImpl(appDao, apodService)
}