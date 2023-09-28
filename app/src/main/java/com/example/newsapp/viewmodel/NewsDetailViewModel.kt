package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.intent.NewsDetailViewState
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsDetailViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _viewState = MutableStateFlow(NewsDetailViewState(null, null, null, null, null))
    val viewState: StateFlow<NewsDetailViewState> = _viewState

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

    fun toggleFavorite() {
        val currentState = _viewState.value.copy(isFavorite = !_viewState.value.isFavorite)
        _viewState.value = currentState
    }

    fun insertArticle(articleX: List<Article>){
        viewModelScope.launch {
            repository.insertArticle(articleX)
        }
    }

    fun checkArticleExists(title: String, callback: ArticleExistsCallback) {
        viewModelScope.launch {
            val exists = repository.isArticleExists(title)
            callback.onArticleExists(exists)
        }
    }
    
    fun deleteArticleByTitle(title: String) {
        viewModelScope.launch {
            repository.deleteArticleByTitle(title)
        }
    }
}
interface ArticleExistsCallback {
    fun onArticleExists(exists: Boolean)
}