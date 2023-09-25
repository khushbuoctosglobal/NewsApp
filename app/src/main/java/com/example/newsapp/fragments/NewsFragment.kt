package com.example.newsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.LatestNewsLayoutBinding
import com.example.newsapp.model.Article

class NewsFragment : Fragment(R.layout.latest_news_layout) {
    private lateinit var binding: LatestNewsLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LatestNewsLayoutBinding.bind(view)
    }

    fun bind(article: Article) {
        binding.article = article
        binding.executePendingBindings()
    }

    companion object {
        fun newInstance(article: Article): NewsFragment {
            val fragment = NewsFragment()
            fragment.bind(article)
            return fragment
        }
    }
}
