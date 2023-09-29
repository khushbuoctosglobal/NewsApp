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

    // Provides access to the DAO (Data Access Object) for database operations.
    abstract fun dao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDb? = null

        // Provides a singleton instance of the News Database.
        fun getDatabase(context: Context): NewsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDb::class.java,
                    "news.db" // Name of the database file
                )
                    .fallbackToDestructiveMigration() // Allows destructive migrations (e.g., database schema changes)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
