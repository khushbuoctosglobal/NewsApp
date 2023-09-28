package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.viewmodel.BottomNavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

//cc2a0e57d02b4293aaa9cc102480e0a6
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    val bottomNavigationViewModel: BottomNavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customBottomNav = findViewById<FrameLayout>(R.id.customBottomNav)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.viewModel = bottomNavigationViewModel

        val itemHome = customBottomNav.findViewById<LinearLayout>(R.id.itemHome)
        val itemProfile = customBottomNav.findViewById<LinearLayout>(R.id.itemProfile)
        val itemFav = customBottomNav.findViewById<LinearLayout>(R.id.itemFav)
        val itemHomeIv = customBottomNav.findViewById<ImageView>(R.id.ivHome)
        val itemProfileIv = customBottomNav.findViewById<ImageView>(R.id.ivProfile)
        val itemFavIv = customBottomNav.findViewById<ImageView>(R.id.ivFav)
        binding.viewModel = bottomNavigationViewModel
        bottomNavigationViewModel.selectItem("Home")
        bottomNavigationViewModel.selectNavItemIv(itemHomeIv, listOf(itemHomeIv, itemFavIv, itemProfileIv))

        itemHome.setOnClickListener {
            binding.viewModel = bottomNavigationViewModel
            bottomNavigationViewModel.selectItem("Home")
            bottomNavigationViewModel.selectNavItemIv(itemHomeIv, listOf(itemHomeIv, itemFavIv, itemProfileIv))
            navController.navigate(R.id.fragmentHome)
        }

        itemFav.setOnClickListener {
            binding.viewModel = bottomNavigationViewModel
            bottomNavigationViewModel.selectItem("Favorite")
            bottomNavigationViewModel.selectNavItemIv(itemFavIv, listOf(itemHomeIv, itemFavIv, itemProfileIv))
            navController.navigate(R.id.fragmentFav)
        }

        itemProfile.setOnClickListener {
            binding.viewModel = bottomNavigationViewModel
            bottomNavigationViewModel.selectItem("Profile")
            bottomNavigationViewModel.selectNavItemIv(itemProfileIv, listOf(itemHomeIv, itemFavIv, itemProfileIv))
            navController.navigate(R.id.fragmentProfile)
        }
    }

}
