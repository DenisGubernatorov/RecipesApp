package com.example.recipesapp.ui.categories

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
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListFragment

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

        arguments = bundleOf(
            RecipesListFragment.ARG_CATEGORY_ID to categoryId,
            RecipesListFragment.ARG_CATEGORY_NAME to category?.title,
            RecipesListFragment.ARG_CATEGORY_IMAGE_URL to category?.imageUrl
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = arguments)
            addToBackStack(null)
        }
    }
}
