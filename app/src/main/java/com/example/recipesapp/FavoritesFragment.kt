package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import java.io.IOException


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)

        binding.favoritesListHeaderImg.setImageDrawable(
            try {
                binding.root.context.assets.open("bcg_favorites.png")
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: IOException) {
                Log.e("!!!!__", "image  for favorites not found ", e)
                null
            })

        binding.emptyRecipesList.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.GONE

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}