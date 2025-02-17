package com.example.recipesapp.ui.recipes.favorites

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
import androidx.fragment.app.viewModels
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.common.RecipeListAdapter
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListFragment.Companion.ARG_RECIPE_ID
import java.io.IOException


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")
    private val favoritesViewModel: FavoritesViewModel by viewModels()

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

        favoritesViewModel.loadFavorites()
        initUI()
        return binding.root
    }


    private fun initUI() {


        val recipeListAdapter = RecipeListAdapter(emptyList())
        val rvRecipesList = binding.rvFavoritesRecipes
        rvRecipesList.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }

        })


        favoritesViewModel.ffLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                recipeListAdapter.updateState(state.favoritesList)

                if (state.favoritesList.isEmpty()) {
                    binding.emptyRecipesList.visibility = View.VISIBLE
                    binding.rvFavoritesRecipes.visibility = View.GONE
                } else {
                    binding.emptyRecipesList.visibility = View.GONE
                    binding.rvFavoritesRecipes.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val recipeArguments = bundleOf(
            ARG_RECIPE_ID to recipeId
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