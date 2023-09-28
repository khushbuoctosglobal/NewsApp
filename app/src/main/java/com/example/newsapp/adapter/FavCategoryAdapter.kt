package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FavNewsLayoutBinding
import com.example.newsapp.model.Article

class FavCategoryAdapter :
    ListAdapter<Article, FavCategoryAdapter.ViewHolder>(FavCategoryItemDiffCallback()) {

    interface ItemClickListener {
        fun onRemoveClick(article: Article)
    }

    var itemClickListener: ItemClickListener? = null

    class ViewHolder(val binding: FavNewsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.category = item
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavNewsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.btnRemove.setOnClickListener {
            itemClickListener?.onRemoveClick(item)
        }
    }

    fun updateList(newList: List<Article>) {
        submitList(newList)
    }
}

class FavCategoryItemDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}
