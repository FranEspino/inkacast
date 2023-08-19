package com.inkamedia.inkacast.data.remote.localDataSource

import android.content.Context
import androidx.room.Room
import com.fraporitmos.gdrivevideoplayer.repository.WebpageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            WebpageDatabase::class.java,
            "inkacast"
        ).build()

    @Singleton
    @Provides
    fun provideWebpageDao(database: WebpageDatabase) = database.webPageDao()
}