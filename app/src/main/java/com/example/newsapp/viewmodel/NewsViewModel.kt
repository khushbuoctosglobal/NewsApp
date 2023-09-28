package com.example.newsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.newsapp.repo.NewsRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.auth.NewsErrorHandling
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository,  application: Application) :ViewModel() {

    private val _news = MutableLiveData<News?>()
    val news: LiveData<News?> = _news

    // Function to handle intents
    fun processIntent(intent: NewsIntent) {
        when (intent) {
            is NewsIntent.LoadLatestNews -> {
                // Handle the LoadUsers intent
                fetchLatestNews(
                    intent.query
                )
            }
            is NewsIntent.LoadCategoryNews -> {
                // Handle the LoadUsers intent
                fetchCategoryNews(
                    intent.query
                )
            }
            is NewsIntent.LoadDateNews -> {
                // Handle the LoadUsers intent
                fetchDateNews(
                    intent.query
                )
            }
        }

        // Function to show a toast message on error

    }

    //get latest news data
    fun fetchLatestNews(query: String) {
        viewModelScope.launch {
            when (val result = repository.getLatestNews(query)) {
                is NewsErrorHandling.Success -> {
                    //success state
                    _news.postValue(result.data)
                }

                is NewsErrorHandling.Error -> {
                    // Handle the error state
                    Log.d("NewsViewModel", "An error occurred: ${result.message}")
                }
            }
        }
    }
    fun fetchCategoryNews(query: String) {
        viewModelScope.launch {
            when (val result = repository.getLatestNews(query)) {
                is NewsErrorHandling.Success -> {
                    //success state
                    _news.postValue(result.data)
                }

                is NewsErrorHandling.Error -> {
                    // Handle the error state
                    Log.d("NewsViewModel", "An error occurred: ${result.message}")
                }
            }
        }
    }

    fun fetchDateNews(query: String) {
        viewModelScope.launch {
            when (val result = repository.getDateNews(query)) {
                is NewsErrorHandling.Success -> {
                    //success state
                    _news.postValue(result.data)
                }

                is NewsErrorHandling.Error -> {
                    // Handle the error state
                    Log.d("NewsViewModel", "An error occurred: ${result.message}")
                }
            }
        }
    }
}

sealed class NewsIntent {
    data class LoadLatestNews(val query: String) : NewsIntent()
    data class LoadCategoryNews(val query: String) : NewsIntent()
    data class LoadDateNews( val query: String) : NewsIntent()
}
