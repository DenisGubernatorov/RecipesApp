package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.recipesapp.databinding.RecipesListFragmentBinding

class RecipesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID: String = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME: String = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL: String = "ARG_CATEGORY_IMAGE_URL"
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
        binding.recipesListHeaderImg.contentDescription = "Изображение для категории $categoryName"
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

    private fun openRecipeByRecipeId(categoryId: Int) {

        val recipeFragment = RecipeFragment()


        parentFragmentManager.commit {
            hide(this@RecipesListFragment)
            add(R.id.mainContainer, recipeFragment)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}