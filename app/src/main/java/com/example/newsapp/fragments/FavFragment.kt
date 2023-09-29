package com.example.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.newsapp.R
import com.example.newsapp.adapter.FavCategoryAdapter
import com.example.newsapp.databinding.FragmentFavBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewmodel.FavViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding

    // View Model for managing favorite articles
    private val viewModel: FavViewModel by viewModels()
    private val favCategoryAdapter = FavCategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fav, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView with the favorite news adapter
        binding.rvCategoryNews.adapter = favCategoryAdapter

        // Observe changes in the list of favorite news and update the adapter
        viewModel.allArticles.observe(viewLifecycleOwner) { articles ->
            favCategoryAdapter.submitList(articles)
        }

        // Set click listener for the remove button in the favorite articles adapter
        favCategoryAdapter.itemClickListener = object : FavCategoryAdapter.ItemClickListener {
            override fun onRemoveClick(article: Article) {
                // Handle item removal from the database
                viewModel.deleteArticleById(article.title ?: "")
            }
        }
    }
}
