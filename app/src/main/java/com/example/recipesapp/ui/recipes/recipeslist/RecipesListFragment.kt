package com.example.recipesapp.ui.recipes.recipeslist

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
import com.example.recipesapp.databinding.RecipesListFragmentBinding
import com.example.recipesapp.ui.common.RecipeListAdapter
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID: String = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME: String = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL: String = "ARG_CATEGORY_IMAGE_URL"
        const val ARG_RECIPE_ID: String = "ARG_RECIPE_ID"
    }

    private var _binding: RecipesListFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")


    private val recipeListViewModel: RecipeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesListFragmentBinding.inflate(inflater)

        arguments?.let {
            recipeListViewModel.loadRecipesList(
                it.getInt(ARG_CATEGORY_ID),
                it.getString(ARG_CATEGORY_NAME),
                it.getString(ARG_CATEGORY_IMAGE_URL)
            )
        }

        initUI()

        return binding.root
    }

    private fun initUI() {

        val recipeListAdapter = RecipeListAdapter(emptyList())

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        val rvRecipesList = binding.rvRecipes
        rvRecipesList.adapter = recipeListAdapter

        recipeListViewModel.rlfLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {

                binding.recipesListHeaderImg.setImageDrawable(state.categoryImage)
                binding.recipesListHeaderImg.contentDescription =
                    getString(R.string.category_header_image_description, state.categoryName)

                binding.recipesListTitle.text = state.categoryName

                recipeListAdapter.updateState(state.categoryList)
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