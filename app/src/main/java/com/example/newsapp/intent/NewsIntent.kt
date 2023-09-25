package com.example.newsapp.intent

sealed class NewsIntent {
    data class LoadLatestNews(val query: String, val fromDate: String, val sortBy: String, val apiKey: String) : NewsIntent()
    data class LoadCategoryNews(val query: String, val fromDate: String, val sortBy: String, val apiKey: String) : NewsIntent()

}