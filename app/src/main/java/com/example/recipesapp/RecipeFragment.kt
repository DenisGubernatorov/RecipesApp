package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
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

        val itemDecoration = MaterialDividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        ).apply {
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.recycler_divider_start)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.recycler_divider_end)
            dividerColor = ContextCompat.getColor(requireContext(), (R.color.divider_color))
            dividerThickness = resources.getDimensionPixelSize(R.dimen.recycler_divider_thickness)
            isLastItemDecorated = false
        }


        val ingredients = recipe?.ingredients ?: emptyList()
        binding.rvIngredients.adapter = IngredientsAdapter(ingredients, requireContext())
        binding.rvIngredients.addItemDecoration(
            itemDecoration
        )

        binding.SeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                sliderPosition: Int,
                isFromUser: Boolean
            ) {

                updateIngredients(sliderPosition)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        val method = recipe?.method ?: emptyList()
        val methodAdapter = MethodAdapter(method, requireContext())
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(itemDecoration)
    }

    private fun updateIngredients(progress: Int) {

        binding.portionCount.text = "$progress"
        val ingredientsAdapter = binding.rvIngredients.adapter as? IngredientsAdapter
        ingredientsAdapter?.updateQuantity(progress)

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
        setFavoritesButtonImage(R.drawable.ic_heart_40_empty)

        binding.favoritesImage.setOnClickListener {
            setFavoritesButtonImage(R.drawable.ic_heart_40)
        }
    }

    private fun setFavoritesButtonImage(favoriteImageId: Int) {
        binding.favoritesImage.setImageDrawable(
            try {
                AppCompatResources.getDrawable(binding.root.context, favoriteImageId)
            } catch (e: IOException) {
                Log.e("!!!!__", "image for favorite button not found", e)
                null
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
