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
object AppModule {
    // Provides a singleton instance of the Android Application's context.
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        // Returns the application context, which can be used for retrofit/api
        return application.applicationContext
    }
}
