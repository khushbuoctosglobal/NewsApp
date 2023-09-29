package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.CategoryItemNewsLayoutBinding
import com.example.newsapp.model.CategoryList

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
