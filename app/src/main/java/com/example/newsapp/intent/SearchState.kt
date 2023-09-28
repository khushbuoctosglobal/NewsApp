package com.example.newsapp.intent

import com.example.newsapp.model.Article
import com.example.newsapp.model.CategoryList

data class SearchState(
    val articles: List<Article> = emptyList(),
    val selectedCategory: CategoryList? = null,
    val filteredList: List<Article> = emptyList()
)
