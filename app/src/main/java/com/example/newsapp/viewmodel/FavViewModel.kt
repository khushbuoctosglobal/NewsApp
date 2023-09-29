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

// favFragment view model
@HiltViewModel
class FavViewModel @Inject constructor(private val repository: NewsRepository) :
    ViewModel() {

    // LiveData property to hold a list of news
    private val _categoryNews = MutableLiveData<List<Article>>()
    val categoryNews: LiveData<List<Article>> = _categoryNews

    // Function to delete an news by its title
    fun deleteArticleById(title: String) {
        viewModelScope.launch {
            // Call the repository to delete the article by its title
            repository.deleteArticleByTitle(title)

            // Update the LiveData list of news
            _categoryNews.value = _categoryNews.value?.filter { it.title != title }
        }
    }

    // LiveData property to hold a list of all articles
    val allArticles: LiveData<List<Article>> = repository.getAllArticles()
}
