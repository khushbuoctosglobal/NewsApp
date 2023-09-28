package com.example.newsapp.intent

data class NewsDetailViewState(
    val author: String?,
    val formattedDate: String?,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)
