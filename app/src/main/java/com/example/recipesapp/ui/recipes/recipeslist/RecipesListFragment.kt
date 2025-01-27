package com.example.recipesapp.ui.recipes.recipeslist

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
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.RecipesListFragmentBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID: String = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME: String = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL: String = "ARG_CATEGORY_IMAGE_URL"
        const val ARG_RECIPE: String = "ARG_RECIPE"
    }

    private var _binding: RecipesListFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesListFragmentBinding.inflate(inflater)

        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            categoryName = it.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(ARG_CATEGORY_IMAGE_URL)
        }
        val drawableImage = try {
            Drawable.createFromStream(
                binding.root.context.assets.open(categoryImageUrl ?: ""),
                null
            )

        } catch (e: Exception) {
            Log.e("!!!!__", "image not found $categoryName")
            null
        }
        binding.recipesListHeaderImg.setImageDrawable(drawableImage)
        binding.recipesListHeaderImg.contentDescription =
            getString(R.string.category_header_image_description, categoryName)
        binding.recipesListTitle.text = categoryName

        initRecycler()
        return binding.root
    }

    private fun initRecycler() {

        val recipeListAdapter = RecipeListAdapter(STUB.getRecipesByCategoryId(categoryId))

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        val rvRecipesList = binding.rvRecipes
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