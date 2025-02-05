package com.example.recipesapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipesapp.R
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListFragment.Companion.ARG_RECIPE_ID
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {

    // private var sliderPositionState: Int = 1
    //  private var recipe: Recipe? = null
    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")
    // private var isFavorite = false

    private val recipeViewModel: RecipeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater)

        arguments?.getInt(ARG_RECIPE_ID)?.let { recipeId ->
            recipeViewModel.loadRecipe(recipeId)
        }

        initUI()
        initRecycler()

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



        binding.rvIngredients.adapter = IngredientsAdapter(emptyList(), requireContext())
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


        val methodAdapter = MethodAdapter(emptyList(), requireContext())
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(itemDecoration)
    }

    private fun updateIngredients(progress: Int) {

        binding.portionCount.text = "$progress"
        val ingredientsAdapter = binding.rvIngredients.adapter as? IngredientsAdapter
        ingredientsAdapter?.updateQuantity(progress)

    }

    private fun initUI() {

        recipeViewModel.rfLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.recipesHeaderImg.setImageDrawable(
                    try {
                        requireActivity().assets.open(RECIPE_IMAGE_PREFIX + state.recipe?.imageUrl)
                            .use { inputStream ->
                                Drawable.createFromStream(inputStream, null)
                            }
                    } catch (e: IOException) {
                        Log.e("!!!!__", "image not found ${state.recipe?.title}", e)
                        null
                    }
                )
                binding.recipeHeaderText.text =
                    state.recipe?.title ?: getString(R.string.get_recipes_error)
                (binding.rvIngredients.adapter as? IngredientsAdapter)?.updateDataSet(state.recipe?.ingredients)
                (binding.rvMethod.adapter as? MethodAdapter)?.updateDataSet(state.recipe?.method)

                setFavoritesButtonImage(state.isFavorite)
                updateIngredients(state.seekBarProgress)

            }
        }

        binding.favoritesImage.setOnClickListener {
            recipeViewModel.onFavoritesClicked()
        }
    }

    private fun setFavoritesButtonImage(isFavorite: Boolean) {
        val toDrawId = when (isFavorite) {
            true -> R.drawable.ic_heart_40
            false -> R.drawable.ic_heart_40_empty
        }
        binding.favoritesImage.setImageResource(toDrawId)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RECIPE_IMAGE_PREFIX = "Property 1="
    }
}
