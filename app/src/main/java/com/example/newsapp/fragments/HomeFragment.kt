package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsCategoryAdapter
import com.example.newsapp.adapter.NewsPagerAdapter
import com.example.newsapp.databinding.CategoryItemNewsLayoutBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.intent.NewsIntent
import com.example.newsapp.model.Article
import com.example.newsapp.model.CategoryList
import com.example.newsapp.ui.NewsDetailActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment()  {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: NewsViewModel by viewModels()
    private val newsCategoryAdapter = NewsCategoryAdapter()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private var article = listOf<Article>()
    lateinit var newsPagerAdapter : NewsPagerAdapter

    private var filteredList = article.toMutableList()

    private val autoSlideHandler = Handler()
    private val AUTO_SWIPE_INTERVAL = 3000

    val categoryList_ = listOf(
        "Science",
        "Business",
        "Health",
        "Technology",
        "Art"
    )
    val categoryList = categoryList_.map { CategoryList(it) }

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
        categoryListAdapter = CategoryListAdapter(categoryList, object : CategoryListAdapter.ItemClickListener {
            override fun onItemClick(category: CategoryList) {
                selectedCategory = category
                val selectedItemName = selectedCategory!!.category
                viewModel.processIntent(NewsIntent.LoadCategoryNews(selectedItemName,"2023-08-25","publishedAt","cc2a0e57d02b4293aaa9cc102480e0a6"))
            }
        })

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        binding.btnNotification.setOnClickListener {
            val dialog = activity?.let { it1 -> BottomSheetDialog(it1) }
            val view = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)

            dialog?.setCancelable(false)
            dialog?.setContentView(view)
            dialog?.show()
        }

        selectedCategory = categoryListAdapter.getSelectedItem()
        if (selectedCategory != null) {
            val selectedItemName = selectedCategory!!.category
            viewModel.processIntent(NewsIntent.LoadCategoryNews(selectedItemName,"2023-08-25","publishedAt","cc2a0e57d02b4293aaa9cc102480e0a6"))
        } else {
            // No item is selected
        }

        binding.rvCategoryList.adapter = categoryListAdapter

        // Set up RecyclerView - latest news
        // binding.rvLatestNews.adapter = newsAdapter

        binding.rvCategoryNews.adapter = newsCategoryAdapter
        val viewPager2 = binding.vpLatestNews
        // Load latest news
        viewModel.processIntent(NewsIntent.LoadLatestNews("business","2023-08-25","publishedAt","cc2a0e57d02b4293aaa9cc102480e0a6"))
        viewModel.latestNews.observe(viewLifecycleOwner, Observer { newsItems ->
           if (newsItems != null) {
                article = newsItems.articles as List<Article>
                newsPagerAdapter = NewsPagerAdapter(article)
                binding.vpLatestNews.adapter = newsPagerAdapter
              // autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SWIPE_INTERVAL.toLong())
            }
        })

        newsPagerAdapter = NewsPagerAdapter(article)
        newsPagerAdapter.itemClickListener = object : NewsPagerAdapter.ItemClickListener {
            override fun onItemClick(article: Article) {
                val intent = Intent(activity, NewsDetailActivity::class.java)
                intent.putExtra("author", article.author)
                intent.putExtra("publishedAt", article.publishedAt)
                intent.putExtra("title", article.title)
                intent.putExtra("content", article.content)
                intent.putExtra("urltoimage", article.urlToImage)
                startActivity(intent)
            }
        }
        viewPager2.adapter = newsPagerAdapter

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer { newsItems ->
            if (newsItems != null) {
                newsCategoryAdapter.submitList(newsItems.articles)
            }
        })

        newsCategoryAdapter.itemClickListener = object : NewsCategoryAdapter.ItemClickListener {
            override fun onItemClick(article: Article) {
                val intent = Intent(activity, NewsDetailActivity::class.java)
                intent.putExtra("author", article.author)
                intent.putExtra("publishedAt", article.publishedAt)
                intent.putExtra("title", article.title)
                intent.putExtra("content", article.content)
                intent.putExtra("urltoimage", article.urlToImage)
                startActivity(intent)
            }
        }

        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            val offset = resources.getDimensionPixelOffset(R.dimen.margin_x)
            val scaleFactor = 0.85f // Adjust this value for the desired scaling effect

            val normalizedPosition = abs(position - 1)
            page.apply {
                translationX = -offset * position

                // Apply scaling effect to the current page
                scaleX = 1f - (scaleFactor - 1f) * normalizedPosition
                scaleY = 1f - (scaleFactor - 1f) * normalizedPosition

                // Apply alpha (opacity) to the previous and next pages
                alpha = scaleFactor + (1 - scaleFactor) * (1 - normalizedPosition)
            }
        }

        viewPager2.setPageTransformer(pageTransformer)

/*
        binding.vpLatestNews.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Remove any existing callbacks to prevent overlapping
                autoSlideHandler.removeCallbacks(autoSlideRunnable)
                // Post a new callback with the delay
                autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SWIPE_INTERVAL.toLong())
            }
        })
*/

    }

    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            // Increment the current page to simulate a slide to the next item
            val nextItem = binding.vpLatestNews.currentItem + 1
            if (nextItem < newsPagerAdapter.itemCount) {
                binding.vpLatestNews.setCurrentItem(nextItem, true)
            } else {
                // If it reaches the last item, go back to the first item
                binding.vpLatestNews.setCurrentItem(0, true)
            }
            // Post the same callback to keep auto-sliding
            autoSlideHandler.postDelayed(this, AUTO_SWIPE_INTERVAL.toLong())
        }
    }

    //category list adapter
    class CategoryListAdapter(private val categoryList: List<CategoryList>, private val itemClickListener: ItemClickListener
    ) : RecyclerView.Adapter<CategoryListAdapter.ItemViewHolder>() {
        private var selectedItemPosition: Int = 0

        interface ItemClickListener {
            fun onItemClick(category: CategoryList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding = CategoryItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = categoryList[position]
            holder.bind(item,position)
        }
        fun getSelectedItem(): CategoryList? {
            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                return categoryList[selectedItemPosition]
            }
            return null
        }
        override fun getItemCount(): Int {
            return categoryList.size
        }
        inner class ItemViewHolder(private val binding: CategoryItemNewsLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = categoryList[position]
                        itemClickListener.onItemClick(item)
                        toggleSelectionState(position)
                        notifyDataSetChanged()
                    }
                }
            }

            fun bind(item: CategoryList, position: Int) {
                binding.tvCategory.text = item.category
                // Update the background and text color based on the selection state
                val isSelected = position == selectedItemPosition
                val backgroundResId = if (isSelected) R.drawable.circular_txt_selected else R.drawable.circular_txt
                val textColorResId = if (isSelected) R.color.white else R.color.maroon

                binding.root.setBackgroundResource(backgroundResId)
                binding.tvCategory.setTextColor(ContextCompat.getColor(binding.root.context, textColorResId))
            }
        }
        fun toggleSelectionState(position: Int) {
            if (position != selectedItemPosition) {
                selectedItemPosition = position
            } else {
                selectedItemPosition = RecyclerView.NO_POSITION
            }
        }
    }

    fun filter(query: String) {
        val lowerCaseQuery = query.toLowerCase(Locale.getDefault())
        filteredList.clear()
        for(x in article) {
            x.title?.let {
                if (x.title.contains(lowerCaseQuery, true)) {
                    filteredList.add(x)
                }
            }
        }
        newsCategoryAdapter.updateList(filteredList)
    }

    fun Double.format(digits: Int): String {
        return "%.${digits}f".format(this)
    }

}
