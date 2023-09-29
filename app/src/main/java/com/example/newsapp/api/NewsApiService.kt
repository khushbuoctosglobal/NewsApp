package com.example.newsapp.api

import com.example.newsapp.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    // Get request for news
    @GET("everything")
    suspend fun getArticles(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Response<News>

    // Get request for news for date filter
    @GET("top-headlines")
    suspend fun getDateFilter(
        @Query("country") query: String,
        @Query("apiKey") apiKey: String
    ): Response<News>
}