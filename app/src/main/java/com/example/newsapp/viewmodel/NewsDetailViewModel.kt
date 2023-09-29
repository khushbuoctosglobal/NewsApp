package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    // MutableStateFlow to hold the view state of the news detail
    private val _viewState = MutableStateFlow(NewsDetailViewState(null, null, null, null, null))
    val viewState: StateFlow<NewsDetailViewState> = _viewState

    // Function to initialize the view state with news details
    fun initViewState(
        author: String?,
        formattedDate: String?,
        title: String?,
        content: String?,
        imageUrl: String?
    ) {
        val currentState = _viewState.value.copy(
            author = author,
            formattedDate = formattedDate,
            title = title,
            content = content,
            imageUrl = imageUrl
        )
        _viewState.value = currentState
    }

    // Function to toggle the favorite status of the news article
    fun toggleFavorite() {
        val currentState = _viewState.value.copy(isFavorite = !_viewState.value.isFavorite)
        _viewState.value = currentState
    }

    // Function to insert an news into the database
    fun insertArticle(articleX: List<Article>){
        viewModelScope.launch {
            repository.insertArticle(articleX)
        }
    }

    // Function to check if an news exists in the database
    fun checkArticleExists(title: String, callback: ArticleExistsCallback) {
        viewModelScope.launch {
            val exists = repository.isArticleExists(title)
            callback.onArticleExists(exists)
        }
    }

    // Function to delete an news by its title
    fun deleteArticleByTitle(title: String) {
        viewModelScope.launch {
            repository.deleteArticleByTitle(title)
        }
    }
}

// Callback interface to handle news existence check
interface ArticleExistsCallback {
    fun onArticleExists(exists: Boolean)
}

// Data class representing the view state of the news detail
data class NewsDetailViewState(
    val author: String?,
    val formattedDate: String?,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)
