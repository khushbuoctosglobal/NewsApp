package com.example.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavBinding

class FavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFavBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_fav, container, false
        )

        // You can interact with UI elements and bind data here
        // For example:
        //binding.textViewTitle.text = "Welcome to the Favorites Fragment"

        return binding.root
    }
}
