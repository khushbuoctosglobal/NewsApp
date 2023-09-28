package com.example.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.model.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<Article>)

    @Query("DELETE FROM news_table WHERE title = :title")
    suspend fun deleteArticleByTitle(title: String)

    @Query("SELECT COUNT(*) FROM news_table WHERE title = :title")
    suspend fun isArticleExists(title:String): Int
}