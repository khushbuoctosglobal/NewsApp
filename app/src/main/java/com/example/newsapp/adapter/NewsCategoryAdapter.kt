package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.CategoryNewsLayoutBinding
import com.example.newsapp.model.Article

class NewsCategoryAdapter :
    ListAdapter<Article, NewsCategoryAdapter.ViewHolder>(NewsCategoryItemDiffCallback()) {

    interface ItemClickListener {
        fun onItemClick(article: Article)
    }

    var itemClickListener: ItemClickListener? = null

    class ViewHolder(private val binding: CategoryNewsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.category = item
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryNewsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(item)
        }
    }

    fun updateList(newList: List<Article>) {
        submitList(newList)
    }
}

class NewsCategoryItemDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
