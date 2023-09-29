package com.example.newsapp.repo

import androidx.lifecycle.LiveData
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.auth.NewsErrorHandling
import com.example.newsapp.db.NewsDb
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDb: NewsDb
) {

    // Retrieves the latest news articles based on a query from the News API.
    suspend fun getLatestNews(query: String): NewsErrorHandling<News> {
        try {
            val response = newsApiService.getArticles(
                query,
                "2023-08-29",
                "publishedAt",
                "cc2a0e57d02b4293aaa9cc102480e0a6"
            )

            if (response.isSuccessful) {
                val userData = response.body()
                if (userData != null) {
                    return NewsErrorHandling.Success(userData)
                }
            }

            // Handle unsuccessful response (e.g., HTTP error status code)
            return NewsErrorHandling.Error("API request failed with ${response.code()}")
        } catch (e: Exception) {
            // Handle other exceptions (e.g., network issues, unexpected errors)
            return NewsErrorHandling.Error("An error occurred: ${e.message ?: "Unknown error"}")
        }
    }

    // Retrieves news articles based on a date filter from the News API.
    suspend fun getDateNews(query: String): NewsErrorHandling<News> {
        try {
            val response = newsApiService.getDateFilter(query, "cc2a0e57d02b4293aaa9cc102480e0a6")

            if (response.isSuccessful) {
                val userData = response.body()
                if (userData != null) {
                    return NewsErrorHandling.Success(userData)
                }
            }

            // Handle unsuccessful response (e.g., HTTP error status code)
            return NewsErrorHandling.Error("API request failed with ${response.code()}")
        } catch (e: Exception) {
            // Handle other exceptions (e.g., network issues, unexpected errors)
            return NewsErrorHandling.Error("An error occurred: ${e.message ?: "Unknown error"}")
        }
    }

    // Inserts a list of articles into the local database.
    suspend fun insertArticle(articleX: List<Article>) {
        newsDb.dao().insertUser(articleX)
    }

    // Retrieves all articles from the local database as LiveData.
    fun getAllArticles(): LiveData<List<Article>> {
        return newsDb.dao().getAllArticles()
    }

    // Deletes an article from the local database by its title.
    suspend fun deleteArticleByTitle(title: String) {
        newsDb.dao().deleteArticleByTitle(title)
    }

    // Checks if an article with the specified title exists in the local database.
    suspend fun isArticleExists(title: String): Boolean {
        return newsDb.dao().isArticleExists(title) > 0
    }
}
