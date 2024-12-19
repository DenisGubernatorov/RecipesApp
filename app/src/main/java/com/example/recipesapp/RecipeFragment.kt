package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {
    private val recipeImagePrefix = "Property 1="
    private var recipe: Recipe? = null
    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater)
        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(RecipesListFragment.ARG_RECIPE, Recipe::class.java)
            } else {
                it.getParcelable(RecipesListFragment.ARG_RECIPE)
            }
        }

        initRecycler()
        initUI()

        return binding.root
    }

    private fun initRecycler() {
        val ingredients = recipe?.ingredients ?: emptyList()
        val ingredientsAdapter = IngredientsAdapter(ingredients, requireContext())
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )


        val method = recipe?.method ?: emptyList()
        val methodAdapter = MethodAdapter(method, requireContext())
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initUI() {
        binding.recipeHeaderText.text = recipe?.title ?: getString(R.string.get_recipes_error)

        binding.recipesHeaderImg.setImageDrawable(
            try {
                binding.root.context.assets.open(recipeImagePrefix + recipe?.imageUrl)
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            } catch (e: IOException) {
                Log.e("!!!!__", "image not found ${recipe?.title}", e)
                null
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
