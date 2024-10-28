package com.example.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(CategoriesListFragment())
        }

        binding.category.setOnClickListener {
            val categoriesListFragment = CategoriesListFragment()
            loadFragment(categoriesListFragment)
        }

        binding.favorites.setOnClickListener {
            val favoritesFragment = FavoritesFragment()
            loadFragment(favoritesFragment)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.mainContainer, fragment)
            addToBackStack(null)
        }
    }
}