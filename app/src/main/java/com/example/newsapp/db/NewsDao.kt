package com.example.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.model.Article

@Dao
interface NewsDao {

    // Retrieves all articles from the 'news_table' and returns them as LiveData.
    @Query("SELECT * FROM news_table")
    fun getAllArticles(): LiveData<List<Article>>

    // Inserts a list of articles into the 'news_table' while ignoring conflicts.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<Article>)

    // Deletes an article from the 'news_table' by its title.
    @Query("DELETE FROM news_table WHERE title = :title")
    suspend fun deleteArticleByTitle(title: String)

    // Checks if an article with the specified title exists in the 'news_table' and returns the count.
    @Query("SELECT COUNT(*) FROM news_table WHERE title = :title")
    suspend fun isArticleExists(title: String): Int
}
