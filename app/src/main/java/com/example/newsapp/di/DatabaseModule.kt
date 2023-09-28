package com.example.newsapp.di

import android.app.Application
import android.content.Context
import com.example.newsapp.db.NewsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDb(@ApplicationContext context: Context): NewsDb {
        return NewsDb.getDatabase(context)
    }
}
