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

    private val viewModel: NewsViewModel by viewModels()
    private val newsCategoryAdapter = NewsCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SeeAllNews,R.layout.activity_see_all_news)

        loadCategoryNews("business")
        binding.rvAllNews.adapter = newsCategoryAdapter
        viewModel.news.observe(this, Observer { newsItems ->
            if (newsItems != null) {
                newsCategoryAdapter.submitList(newsItems.articles)
            }
        })

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@SeeAllNews, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCategoryNews(category: String) {
        viewModel.processIntent(NewsIntent.LoadCategoryNews(category))
    }

}