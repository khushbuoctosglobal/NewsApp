package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsDetailBinding
import com.example.newsapp.intent.NewsDetailViewState
import com.example.newsapp.model.Article
import com.example.newsapp.viewmodel.ArticleExistsCallback
import com.example.newsapp.viewmodel.NewsDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity(), ArticleExistsCallback {

    private lateinit var binding: ActivityNewsDetailBinding
    private val viewModel: NewsDetailViewModel by viewModels()
    val receivedArticlesList = mutableListOf<Article>()

    var exists_ by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)


        // Use a CoroutineScope for collecting StateFlow
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                render(state)
            }
        }

        // Initialize the ViewModel with data from the intent
        val receivedArticle: Article? = intent.getParcelableExtra("article")

        if (receivedArticle != null) {
            viewModel.initViewState(
                receivedArticle.author,
                receivedArticle.publishedAt,
                receivedArticle.title,
                receivedArticle.content,
                receivedArticle.urlToImage
            )
        }

        if (receivedArticle != null) {
            receivedArticle.title?.let { viewModel.checkArticleExists(it,this@NewsDetailActivity) }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.ivFav.setOnClickListener {
            viewModel.toggleFavorite()
             if (exists_) {
                 receivedArticle?.title?.let { it1 -> viewModel.deleteArticleByTitle(it1) }
                 binding.ivFav.setImageResource(R.drawable.ic_fluent_heart_24_regular)
                 exists_ = false
            } else {
                 if (receivedArticle != null) {
                     receivedArticlesList.add(receivedArticle)
                 }
                 viewModel.insertArticle(receivedArticlesList)
                binding.ivFav.setImageResource(R.drawable.ic_fluent_heart_24_filled)
            }
        }
    }

    private fun render(state: NewsDetailViewState) {
      /*  binding.tvAuthor.text = state.author
        binding.tvDate.text = state.formattedDate
        binding.tvTitle.text = state.title*/
        binding.tvNewsDetails.text = state.content

        Glide.with(this)
            .load(state.imageUrl)
            .into(binding.imgNews)

        binding.ivFav.setImageResource(
            if (state.isFavorite) R.drawable.ic_fluent_heart_24_filled
            else R.drawable.ic_fluent_heart_24_regular
        )
    }

    override fun onArticleExists(exists: Boolean) {
        if (exists) {
            exists_ = true
            binding.ivFav.setImageResource(R.drawable.ic_fluent_heart_24_filled)
        } else {
            exists_ = false
            binding.ivFav.setImageResource(R.drawable.ic_fluent_heart_24_regular)
        }
    }
}
