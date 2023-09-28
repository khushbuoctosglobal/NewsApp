package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.model.Article
import javax.inject.Singleton

@Singleton
@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class NewsDb : RoomDatabase() {

    abstract fun dao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDb? = null

        fun getDatabase(context: Context): NewsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDb::class.java,
                    "news.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
