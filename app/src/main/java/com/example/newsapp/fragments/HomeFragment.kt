package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.adapter.CategoryListAdapter
import com.example.newsapp.adapter.NewsCategoryAdapter
import com.example.newsapp.adapter.NewsPagerAdapter
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.Article
import com.example.newsapp.model.CategoryList
import com.example.newsapp.ui.NewsDetailActivity
import com.example.newsapp.ui.SearchActivity
import com.example.newsapp.ui.SeeAllNews
import com.example.newsapp.viewmodel.NewsIntent
import com.example.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment()  {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: NewsViewModel by viewModels()

    private val newsCategoryAdapter = NewsCategoryAdapter()
    private lateinit var categoryListAdapter: CategoryListAdapter

    private var article = listOf<Article>()
    lateinit var newsPagerAdapter : NewsPagerAdapter

    private val categoryList_ = listOf("Science", "Business", "Health", "Technology", "Art")
    private val categoryList = categoryList_.map { CategoryList(it) }

    var selectedCategory: CategoryList? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding= DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView - category item
        categoryListAdapter =
            CategoryListAdapter(categoryList, object : CategoryListAdapter.ItemClickListener {
                override fun onItemClick(category: CategoryList) {
                    selectedCategory = category
                    val selectedItemName = selectedCategory!!.category
                    loadCategoryNews(selectedItemName)
                }
            })

        //search click event / pass data to search activity
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val intent = Intent(activity, SearchActivity::class.java)
                startActivity(intent)
            }
        })

        //filter activity - SearchActivity
        binding.btnNotification.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        //select category news list value set
        selectedCategory = categoryListAdapter.getSelectedItem()
        if (selectedCategory != null) {
            val selectedItemName = selectedCategory!!.category
            loadCategoryNews(selectedItemName)
        } else {
            // No item is selected
        }

        binding.rvCategoryList.adapter = categoryListAdapter

        binding.rvCategoryNews.adapter = newsCategoryAdapter

        val viewPager2 = binding.vpLatestNews
        // Load latest news
        viewModel.processIntent(
            NewsIntent.LoadLatestNews(
                "business"
            )
        )

        viewModel.news.observe(viewLifecycleOwner, Observer { newsItems ->
            if (newsItems != null) {
                article = newsItems.articles as List<Article>
                newsPagerAdapter = NewsPagerAdapter(article)
                binding.vpLatestNews.adapter = newsPagerAdapter
            }
        })

        newsPagerAdapter = NewsPagerAdapter(article)
        newsPagerAdapter.itemClickListener = object : NewsPagerAdapter.ItemClickListener {
            override fun onItemClick(article: Article) {
                val intent = Intent(activity, NewsDetailActivity::class.java)
                intent.putExtra("article", article)
                startActivity(intent)
            }
        }

        viewPager2.adapter = newsPagerAdapter

        viewModel.news.observe(viewLifecycleOwner, Observer { newsItems ->
            if (newsItems != null) {
                newsCategoryAdapter.submitList(newsItems.articles)
            }
        })

        newsCategoryAdapter.itemClickListener = object : NewsCategoryAdapter.ItemClickListener {
            override fun onItemClick(article: Article) {
                val intent = Intent(activity, NewsDetailActivity::class.java)
                intent.putExtra("article", article)
                startActivity(intent)
            }
        }

        viewPager2.offscreenPageLimit = 3
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL;
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.margin_xx).toFloat()
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.elevation).toFloat()

        viewPager2.setPageTransformer { page, position ->
            val myOffset: Float = position * -(2 * pageOffset + pageMargin)
            if (position < -1) {
                page.translationX = -myOffset
                page.alpha = 0.5F
            } else if (position <= 1) {
                val scaleFactor =
                    0.7f.coerceAtLeast(1 - abs(position - 0.14285715f))
                page.translationX = myOffset
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
            } else {
                page.alpha = 0.5F
                page.translationX = myOffset
            }
        }

        binding.txtALlNews.setOnClickListener {
            val intent = Intent(activity, SeeAllNews::class.java)
            startActivity(intent)
        }
    }

    private fun loadCategoryNews(query: String) {
         viewModel.processIntent(NewsIntent.LoadCategoryNews(query))
    }
}
