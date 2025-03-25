package com.example.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.databinding.RecipesListFragmentBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.categories.CategoriesListFragmentArgs
import com.example.recipesapp.ui.common.RecipeListAdapter

class RecipesListFragment : Fragment() {

    private var _binding: RecipesListFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")

    private val appContainer by lazy {
        (requireActivity().application as RecipesApplication).appContainer
    }

    private val recipeListViewModel: RecipeListViewModel by viewModels()
    private val safeArgs by navArgs<CategoriesListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesListFragmentBinding.inflate(inflater)

        recipeListViewModel.loadRecipesList(
            safeArgs.category.id,
            safeArgs.category.title,
            safeArgs.category.imageUrl,
            requireContext().applicationContext
        )

        initUI()

        return binding.root
    }

    private fun initUI() {

        val recipeListAdapter = RecipeListAdapter(emptyList(), appContainer.imageUrl)

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                openRecipeByRecipeId(recipe)
            }
        })

        val rvRecipesList = binding.rvRecipes
        rvRecipesList.adapter = recipeListAdapter

        recipeListViewModel.rlfLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                when (state.result) {

                    is RepositoryResult.Success -> {
                        Glide.with(this)
                            .load(state.categoryImageUrl.let { RecipesRepository.IMAGE_URL + "$it" })
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .into(binding.recipesListHeaderImg)

                        binding.recipesListHeaderImg.contentDescription =
                            getString(
                                R.string.category_header_image_description,
                                state.categoryName
                            )

                        binding.recipesListTitle.text = state.categoryName

                        recipeListAdapter.updateState(state.result.data)
                    }

                    is RepositoryResult.Error -> {
                        recipeListAdapter.updateState(emptyList())
                        Toast.makeText(
                            requireContext(),
                            state.result.exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }

    }

    private fun openRecipeByRecipeId(recipe: Recipe) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipe
            )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}