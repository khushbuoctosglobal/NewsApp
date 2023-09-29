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

//bottom navigation for filter news list - searchActivity
@HiltViewModel
class BottomNavigationViewModel @Inject constructor(private val context: Context) : ViewModel() {

    // LiveData to track the selected item in the bottom navigation
    val selectedItem = MutableLiveData<String>()

    // Function to update the selected item in the bottom navigation
    fun selectItem(item: String) {
        selectedItem.value = item
    }

    // ImageView to track the currently selected navigation item icon
    private var selectedItemImage: ImageView? = null

    // Function to update the selected navigation item icon
    fun selectNavItemIv(item: ImageView, ImageViews: List<ImageView>) {
        // Reset text colors for all items
        ImageViews.forEach { it.setColorFilter(context.getColor(R.color.light_gray)) }

        // Check if the current item is different from the previously selected item
        val isSelected = item != selectedItemImage

        if (isSelected) {
            // Set the color filter to indicate selection
            item.setColorFilter(context.getColor(R.color.light_red))
            selectedItemImage = item
        } else {
            // If the same item is selected again, clear the selection
            selectedItemImage = null
        }
    }
}
