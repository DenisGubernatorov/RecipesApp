package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.common.RecipeListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")


    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val imageUrl: String by lazy { favoritesViewModel.getImageUrl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)

        Glide.with(binding.favoritesListHeaderImg)
            .load(R.drawable.bcg_favorites)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.favoritesListHeaderImg)



        favoritesViewModel.loadFavorites()
        initUI()
        return binding.root
    }


    private fun initUI() {


        val recipeListAdapter = RecipeListAdapter(emptyList(), imageUrl)
        val rvRecipesList = binding.rvFavoritesRecipes
        rvRecipesList.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipeByRecipeId(recipe)
            }

        })


        favoritesViewModel.ffLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state.result) {
                    is RepositoryResult.Success -> {
                        val recipes = state.result.data
                        recipeListAdapter.updateState(recipes)

                        if (recipes.isEmpty()) {
                            binding.emptyRecipesList.visibility = View.VISIBLE
                            binding.rvFavoritesRecipes.visibility = View.GONE
                        } else {
                            binding.emptyRecipesList.visibility = View.GONE
                            binding.rvFavoritesRecipes.visibility = View.VISIBLE
                        }
                    }

                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка загрузки рецептов",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun openRecipeByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipe
            )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}