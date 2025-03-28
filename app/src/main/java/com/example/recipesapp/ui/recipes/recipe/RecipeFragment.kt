package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for RecipesListFragmentBinding must be not null ")

    private val appContainer by lazy {
        (requireActivity().application as RecipesApplication).appContainer
    }

    private val recipeViewModel: RecipeViewModel by viewModels { appContainer.recipeViewModelFactory }
    private val args by navArgs<RecipeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater)


        recipeViewModel.loadRecipe(args.recipe)
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

        binding.SeekBar.setOnSeekBarChangeListener(PortionSeekBarListener { portionCount ->
            recipeViewModel.onPortionCountChanged(portionCount)
        })


        val methodAdapter = MethodAdapter(emptyList(), requireContext())
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(itemDecoration)

        binding.favoritesImage.setOnClickListener {
            recipeViewModel.onFavoritesClicked()
        }
        recipeViewModel.rfLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                if (state.result is RepositoryResult.Error) {
                    Toast.makeText(
                        requireContext(),
                        state.result.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    Glide.with(this)
                        .load(state.recipeImageUrl.let { "${appContainer.imageUrl}$it" })
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(binding.recipesHeaderImg)

                    binding.recipeHeaderText.text =
                        state.recipe?.title ?: getString(R.string.get_recipes_error)
                    binding.portionCount.text = "${state.portionCount}"


                    ingredientsAdapter.updateDataSet(state.recipe?.ingredients ?: emptyList())
                    ingredientsAdapter.updateQuantity(state.portionCount)

                    methodAdapter.updateDataSet(state.recipe?.method ?: emptyList())

                    setFavoritesButtonImage(state.isFavorite)

                }


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

    class PortionSeekBarListener(private val onChangeIngredients: (Int) -> Unit) :
        OnSeekBarChangeListener {

        override fun onProgressChanged(
            seekBar: SeekBar?,
            portionCount: Int,
            isFromUser: Boolean
        ) {
            onChangeIngredients(portionCount)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {/*to do*/
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {/*to do*/
        }

    }
}
