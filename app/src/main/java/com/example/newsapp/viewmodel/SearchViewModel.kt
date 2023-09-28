package com.example.newsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

sealed class SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent()
}

data class SearchViewState(
    val articles: List<Article> = emptyList(),
    val filteredList: List<Article> = emptyList()
)

class SearchViewModel : ViewModel() {
    private val _state = MutableLiveData<SearchViewState>()
    val state: LiveData<SearchViewState>
        get() = _state

    init {
        _state.value = SearchViewState()
    }

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> filter(intent.query)
        }
    }

    private fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        val currentState = _state.value ?: return

        val filteredList = currentState.articles.filter { it.title?.contains(lowerCaseQuery, true) == true }
        _state.value = currentState.copy(filteredList = filteredList)
    }

    private fun applyFilter(filterType: String) {
        val currentState = _state.value ?: return

        val sortedArticles = when (filterType) {
            "A-Z" -> currentState.articles.sortedBy { it.title }
            "Z-A" -> currentState.articles.sortedByDescending { it.title }
            "Date" -> {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.US)
                currentState.articles.sortedBy { article ->
                    val date = article.publishedAt?.let { it1 -> dateFormat.parse(it1) }
                    date
                }
            }
            else -> currentState.articles
        }

        _state.value = currentState.copy(filteredList = sortedArticles)
    }

    // Function to set initial data
    fun setInitialData(articles: List<Article>) {
        val currentState = _state.value ?: return
        _state.value = currentState.copy(articles = articles)
    }
}