package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
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
                binding.favoritesListHeaderImg.setImageDrawable(state.favoritesImage)
                binding.emptyRecipesList.visibility = state.emptyListVisibility
                binding.rvFavoritesRecipes.visibility = state.rvFavoritesVisibility
                binding.emptyRecipesList.visibility = state.rvFavoritesVisibility

                recipeListAdapter.updateState(state.favoritesList)
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