package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(private val repository: NewsRepository) :
    ViewModel() {

    // LiveData property to hold a list of category-specific news articles
    private val _categoryNews = MutableLiveData<List<Article>>()
    val categoryNews: LiveData<List<Article>> = _categoryNews

  fun deleteArticleById(title: String) {
        viewModelScope.launch {
            repository.deleteArticleByTitle(title)
            _categoryNews.value = _categoryNews.value?.filter { it.title != title }
        }
    }

    val allArticles: LiveData<List<Article>> = repository.getAllArticles()

}