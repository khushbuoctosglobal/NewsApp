package com.example.newsapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.newsapp.repo.NewsRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.auth.NewsErrorHandling
import com.example.newsapp.intent.NewsIntent
import com.example.newsapp.model.News
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository,  application: Application) :ViewModel() {

    private val _latestNews = MutableLiveData<News?>()
    val latestNews: LiveData<News?> = _latestNews

    private val _categoryNews = MutableLiveData<News?>()
    val categoryNews: LiveData<News?> = _categoryNews

    // Function to handle intents
    fun processIntent(intent: NewsIntent) {
        when (intent) {
            is NewsIntent.LoadLatestNews -> {
                // Handle the LoadUsers intent
                fetchLatestNews(
                    intent.query,
                    intent.fromDate,
                    intent.sortBy,
                    intent.apiKey
                )
            }
            is NewsIntent.LoadCategoryNews -> {
                // Handle the LoadUsers intent
                fetchCategoryNews(
                    intent.query,
                    intent.fromDate,
                    intent.sortBy,
                    intent.apiKey
                )
            }

            else -> {
                // Handle other intents if needed
            }
        }

        // Function to show a toast message on error

    }

    //get latest news data
    fun fetchLatestNews(query: String, fromDate: String, sortBy: String, apiKey: String) {
        viewModelScope.launch {
            when (val result = repository.getLatestNews(query, fromDate, sortBy, apiKey)) {
                is NewsErrorHandling.Success -> {
                    //success state
                    _latestNews.postValue(result.data)
                }

                is NewsErrorHandling.Error -> {
                    // Handle the error state
                    Log.d("NewsViewModel", "An error occurred: ${result.message}")
                }
            }
        }
    }
    fun fetchCategoryNews(query: String, fromDate: String, sortBy: String, apiKey: String) {
        viewModelScope.launch {
            when (val result = repository.getLatestNews(query, fromDate, sortBy, apiKey)) {
                is NewsErrorHandling.Success -> {
                    //success state
                    _categoryNews.postValue(result.data)
                }

                is NewsErrorHandling.Error -> {
                    // Handle the error state
                    Log.d("NewsViewModel", "An error occurred: ${result.message}")
                }
            }
        }
    }

}
