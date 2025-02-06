package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
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

class RecipeFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")

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

        return binding.root
    }


    private fun initUI() {

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

        val ingredientsAdapter = IngredientsAdapter(emptyList(), requireContext())
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(
            itemDecoration
        )

        binding.SeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                portionCount: Int,
                isFromUser: Boolean
            ) {
                recipeViewModel.onProgressChanged(portionCount)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })


        val methodAdapter = MethodAdapter(emptyList(), requireContext())
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(itemDecoration)

        binding.favoritesImage.setOnClickListener {
            recipeViewModel.onFavoritesClicked()
        }
        recipeViewModel.rfLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                binding.recipesHeaderImg.setImageDrawable(state.recipeImage)
                binding.recipeHeaderText.text =
                    state.recipe?.title ?: getString(R.string.get_recipes_error)
                binding.portionCount.text = "${state.portionCount}"


                ingredientsAdapter.updateDataSet(state.recipe?.ingredients)
                ingredientsAdapter.updateQuantity(state.portionCount)

                methodAdapter.updateDataSet(state.recipe?.method)

                setFavoritesButtonImage(state.isFavorite)


            }
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

}
