package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.LatestNewsLayoutBinding
import com.example.newsapp.model.Article

class NewsPagerAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsPagerAdapter.NewsViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(article: Article)
    }

    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = LatestNewsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = articles[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class NewsViewHolder(private val binding: LatestNewsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.article = item
            binding.executePendingBindings()
        }
    }
}

