package com.example.newsapp.viewmodel

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(private val context: Context) : ViewModel() {

    val selectedItem = MutableLiveData<String>()
    fun selectItem(item: String) {
        selectedItem.value = item
    }

    private var selectedItemImage: ImageView? = null
    fun selectNavItemIv(item: ImageView, ImageViews: List<ImageView>) {
        // Reset text colors for all items
        ImageViews.forEach { it.setColorFilter(context.getColor(R.color.light_gray)) }
        val isSelected = item != selectedItemImage
        if (isSelected) {
            item.setColorFilter(context.getColor(R.color.light_red))
            selectedItemImage = item
        } else {
            selectedItemImage = null
        }
    }
}
