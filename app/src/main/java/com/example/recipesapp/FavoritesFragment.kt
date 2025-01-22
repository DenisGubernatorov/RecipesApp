package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
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

        val favoritesUtils = FavoritesUtils()
        val favorites = favoritesUtils.getFavorites(context)

        if (favorites.isEmpty()) {
            setViewVisibilityState(favorites.isEmpty())
        } else {
            initRecycler(favorites)
            setViewVisibilityState(favorites.isEmpty())
        }

        return binding.root
    }

    private fun setViewVisibilityState(isEmpty: Boolean) {
        binding.emptyRecipesList.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvFavoritesRecipes.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun initRecycler(favorites: HashSet<String>) {

        val toSet = favorites.map { it.toInt() }.toSet()
        val recipesByIds = STUB.getRecipesByIds(toSet)

        val recipeListAdapter = RecipeListAdapter(recipesByIds)

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        val rvRecipesList = binding.rvFavoritesRecipes
        rvRecipesList.adapter = recipeListAdapter
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)

        val recipeArguments = bundleOf(
            ARG_RECIPE to recipe
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = recipeArguments)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}