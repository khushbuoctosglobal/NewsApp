package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

// ViewModel responsible for handling SearchActivity functionality.
class SearchViewModel : ViewModel() {
    private val _state = MutableLiveData<SearchViewState>()
    val state: LiveData<SearchViewState>
        get() = _state

    // Initialize the view state with empty data.
    init {
        _state.value = SearchViewState()
    }

    // Process the provided search intent.
    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> filter(intent.query)
        }
    }

    // Filter articles based on the provided query.
    private fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        val currentState = _state.value ?: return

        val filteredList = currentState.articles.filter { it.title?.contains(lowerCaseQuery, true) == true }
        _state.value = currentState.copy(filteredList = filteredList)
    }

    // Function to set initial data (articles) in the view state.
    fun setInitialData(articles: List<Article>) {
        val currentState = _state.value ?: return
        _state.value = currentState.copy(articles = articles)
    }
}

// Search intents.
sealed class SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent()
}

// Data class representing the view state for the search screen.
data class SearchViewState(
    val articles: List<Article> = emptyList(),
    val filteredList: List<Article> = emptyList()
)
