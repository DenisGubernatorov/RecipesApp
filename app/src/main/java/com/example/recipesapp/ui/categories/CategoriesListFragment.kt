package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater)

        categoriesViewModel.loadCategories()
        initUI()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initUI() {

        val categoriesListAdapter = CategoriesListAdapter(emptyList())

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        val rvCategories = binding.rvCategories
        rvCategories.adapter = categoriesListAdapter

        categoriesViewModel.catLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                categoriesListAdapter.updateState(state.categories)
            }
        }

    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val category =
            categoriesViewModel.catLiveData.value?.categories?.find { it.id == categoryId }
        val categoryTitle = category?.title ?: ""
        val categoryImageUrl = category?.imageUrl ?: ""

        val categoriesListToRecipesList =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                categoryId,
                categoryTitle,
                categoryImageUrl
            )
        findNavController().navigate(categoriesListToRecipesList)

    }
}
