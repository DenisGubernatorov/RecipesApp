package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.util.Log
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
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")

    private val categoriesListViewModel: CategoriesListViewModel by viewModels()
    private val imageUrl: String by lazy { categoriesListViewModel.getImageUrl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater)

        categoriesListViewModel.loadCategories()
        initUI()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initUI() {

        val categoriesListAdapter = CategoriesListAdapter(emptyList(), imageUrl)

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        val rvCategories = binding.rvCategories
        Glide.with(binding.categoriesListHeaderImg)
            .load(R.drawable.bcg_categories)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.categoriesListHeaderImg)

        rvCategories.adapter = categoriesListAdapter

        categoriesListViewModel.catLiveData.observe(viewLifecycleOwner) { state ->
            when (state.result) {
                is RepositoryResult.Success -> {
                    categoriesListAdapter.updateState(state.result.data)
                }

                is RepositoryResult.Error -> {
                    categoriesListAdapter.updateState(emptyList())
                    Toast.makeText(
                        requireContext(),
                        state.result.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        categoriesListViewModel.catLiveData.value?.result?.let { result ->
            when (result) {
                is RepositoryResult.Success -> {
                    result.data.find { categoryId == it.id }?.let { category ->
                        val categoriesListToRecipesList =
                            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                                category
                            )
                        findNavController().navigate(categoriesListToRecipesList)
                    } ?: Log.e("ORE", "Category id not found")
                }

                is RepositoryResult.Error -> {
                    Log.e("ORE", "${result.exception.message}")
                }
            }
        }
    }
}
