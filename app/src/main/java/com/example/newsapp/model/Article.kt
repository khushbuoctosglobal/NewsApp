package com.example.newsapp.model

import android.os.Parcel
import android.os.Parcelable

data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)