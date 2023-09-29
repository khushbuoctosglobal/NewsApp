package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.CategoryListAdapter
import com.example.newsapp.adapter.FilterListAdapter
import com.example.newsapp.adapter.NewsCategoryAdapter
import com.example.newsapp.databinding.ActivitySearchBinding
import com.example.newsapp.model.Article
import com.example.newsapp.model.CategoryList
import com.example.newsapp.viewmodel.NewsIntent
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.viewmodel.SearchIntent
import com.example.newsapp.viewmodel.SearchViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: NewsViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    // News vise Category Adapter
    private var newsCategoryAdapter = NewsCategoryAdapter()

    // Category Adapter
    private lateinit var categoryListAdapter: CategoryListAdapter
    val categoryList_ = listOf(
        "Science", "Business", "Health", "Technology", "Art"
    )
    val categoryList = categoryList_.map { CategoryList(it) }
    var selectedCategory: CategoryList? = null

    private lateinit var articles:List<Article>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        binding.etSearch.clearFocus()

        //category adapter
        categoryListAdapter = CategoryListAdapter(categoryList, object : CategoryListAdapter.ItemClickListener {
            override fun onItemClick(category: CategoryList) {
                selectedCategory = category
                val selectedItemName = selectedCategory!!.category
                viewModel.processIntent(NewsIntent.LoadCategoryNews(selectedItemName))
            }
        })

        //select category news list value set
        selectedCategory = categoryListAdapter.getSelectedItem()
        if (selectedCategory != null) {
            val selectedItemName = selectedCategory!!.category
            loadCategoryNews(selectedItemName)
        } else {
            // No item is selected
        }

        //recyclerview category list
        binding.rvCategoryList.adapter = categoryListAdapter

        // Initialize SearchViewModel with initial data
        viewModel.news.observe(this@SearchActivity, Observer { newsItems ->
            if (newsItems != null) {
                newsCategoryAdapter = NewsCategoryAdapter()
                articles = newsItems.articles as List<Article>
                newsCategoryAdapter.submitList(articles)
                binding.rvCategoryNews.adapter = newsCategoryAdapter
            }
        })

        //news category vise click listener
        // intent pass data to NewsDetailActivity
        newsCategoryAdapter.itemClickListener = object : NewsCategoryAdapter.ItemClickListener {
            override fun onItemClick(article: Article) {
                val intent = Intent(this@SearchActivity, NewsDetailActivity::class.java)
                intent.putExtra("author", article.author)
                intent.putExtra("publishedAt", article.publishedAt)
                intent.putExtra("title", article.title)
                intent.putExtra("content", article.content)
                intent.putExtra("urltoimage", article.urlToImage)
                startActivity(intent)
            }
        }

        //filter bottom sheet
        binding.tvfilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)

            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.COLUMN
            layoutManager.justifyContent = JustifyContent.FLEX_END

            val recyclerView = view.findViewById<RecyclerView>(R.id.rvFilterList)
            //filter list
            val filterItemList = listOf("A-Z", "Z-A", "Date", "Channel", "Following")

            // Create an adapter and set it to the RecyclerView for Filter list
            val filterListAdapter = FilterListAdapter(filterItemList)
            recyclerView.adapter = filterListAdapter

            //filter click listener
            filterListAdapter.itemClickListener = object : FilterListAdapter.ItemClickListener {
                override fun onItemClick(filterType: String) {
                    val sortedArticles = when (filterType) {
                        "A-Z" -> articles.sortedBy { it.title }
                        "Z-A" -> articles.sortedByDescending { it.title }
                        "Date" -> {
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.US)
                            articles.sortedBy { article ->
                                val date = article.publishedAt?.let { it1 -> dateFormat.parse(it1) }
                                date
                            }
                        }
                        else -> articles
                    }

                    newsCategoryAdapter = NewsCategoryAdapter()
                    newsCategoryAdapter.submitList(sortedArticles)
                    binding.rvCategoryNews.adapter = newsCategoryAdapter

                    dialog.dismiss()
                }
            }
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(view)
            dialog.show()
        }

        //filter list date wise, Url - https://newsapi.org/v2/top-headlines?country=us&apiKey=
        binding.tvDateFilter.setOnClickListener {
            viewModel.processIntent(NewsIntent.LoadDateNews("us"))
        }

        //search list
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchViewModel.processIntent(SearchIntent.QueryChanged(s.toString()))
            }
        })

        // Observe the SearchViewModel state and update the filtered list
        searchViewModel.state.observe(this, Observer { state ->
            newsCategoryAdapter.submitList(state.filteredList)
            binding.rvCategoryNews.isNestedScrollingEnabled = false
        })
    }
    private fun loadCategoryNews(query: String) {
        viewModel.processIntent(NewsIntent.LoadCategoryNews(query))
    }
}
