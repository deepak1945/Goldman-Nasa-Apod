package com.goldmen.android.nasa_apod.di

import android.content.Context
import androidx.room.Room
import com.goldmen.android.nasa_apod.domain.dao.ApodDao
import com.goldmen.android.nasa_apod.domain.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDatabase.db"
        ).allowMainThreadQueries().build()

    @Provides
    fun provideApodDao(database: AppDatabase): ApodDao =
        database.apodListDao()
}