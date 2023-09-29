package com.example.newsapp.auth

//handel error state Request and response
sealed class NewsErrorHandling <out T : Any>  {
    object Loading : NewsErrorHandling<Nothing>()
    data class Success<out T : Any>(val data: T) : NewsErrorHandling<T>()
    data class Error(val message: String? = null) : NewsErrorHandling<Nothing>()
}
