package com.example.newsapp.auth

import com.example.newsapp.model.Article

sealed class NewsErrorHandling <out T : Any>  {
       data class Success<out T : Any>(val data: T) : NewsErrorHandling<T>()
        data class Error(val message: String? = null) : NewsErrorHandling<Nothing>()
}
