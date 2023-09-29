package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsCategoryAdapter
import com.example.newsapp.databinding.ActivitySeeAllNewsBinding
import com.example.newsapp.viewmodel.NewsIntent
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeAllNews : AppCompatActivity() {

    lateinit var binding: ActivitySeeAllNewsBinding

    // View Model for managing news data
    private val viewModel: NewsViewModel by viewModels()

    // Adapter for displaying news articles
    private val newsCategoryAdapter = NewsCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the data binding
        binding = DataBindingUtil.setContentView(this@SeeAllNews, R.layout.activity_see_all_news)

        // Load news articles of a specific category
        loadCategoryNews("business")

        // Set up the RecyclerView with the news adapter
        binding.rvAllNews.adapter = newsCategoryAdapter

        // Observe changes in news data and update the adapter
        viewModel.news.observe(this, Observer { newsItems ->
            if (newsItems != null) {
                newsCategoryAdapter.submitList(newsItems.articles)
            }
        })

        // Handle the back button click to return to the main activity
        binding.btnBack.setOnClickListener {
            val intent = Intent(this@SeeAllNews, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Load news articles of a specific category
    private fun loadCategoryNews(category: String) {
        viewModel.processIntent(NewsIntent.LoadCategoryNews(category))
    }
}
