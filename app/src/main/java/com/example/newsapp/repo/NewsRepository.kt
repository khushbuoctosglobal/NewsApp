package com.example.newsapp.repo

import com.example.newsapp.api.NewsApiService
import com.example.newsapp.auth.NewsErrorHandling
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApiService: NewsApiService) {

    //get Latest news function
    suspend fun getLatestNews(query: String, fromDate: String, sortBy: String, apiKey: String): NewsErrorHandling<News> {
        try {
            val response = newsApiService.getArticles(query,fromDate,sortBy,apiKey)
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
}