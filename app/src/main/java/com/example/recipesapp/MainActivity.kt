package com.example.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment<CategoriesListFragment>(false)
        }

        binding.category.setOnClickListener {
            loadFragment<CategoriesListFragment>()
        }

        binding.favorites.setOnClickListener {
            loadFragment<FavoritesFragment>()
        }
    }

    private inline fun <reified T : Fragment> loadFragment(isInBackstack: Boolean = true) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(R.id.mainContainer)
            if (isInBackstack) {
                addToBackStack(null)
            }
        }
    }
}