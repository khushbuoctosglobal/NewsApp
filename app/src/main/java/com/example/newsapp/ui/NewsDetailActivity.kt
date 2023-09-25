package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsDetailBinding
import com.example.newsapp.model.Article

class NewsDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_news_detail)

         //get news detail from home page
         val author: String? = intent.getStringExtra("author")
         binding.tvAuthor.text = author

         val publishedAt: String? = intent.getStringExtra("publishedAt")
         binding.tvDate.text = publishedAt

        val title: String? = intent.getStringExtra("title")
        binding.tvTitle.text = title

         val content: String? = intent.getStringExtra("content")
         binding.tvNewsDetails.text = content

        val urltoimage: String? = intent.getStringExtra("urltoimage")
        Glide.with(this)
            .load(urltoimage)
            .into(binding.imgNews)

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}