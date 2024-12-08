package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding  for FragmentListCategoriesBinding must be not null ")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater)
        initRecycler()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {

        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })

        val rvCategories = binding.rvCategories
        rvCategories.adapter = categoriesListAdapter

    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val recipesListFragment = RecipesListFragment()
        val category = STUB.getCategories()[categoryId]

        recipesListFragment.apply {
            arguments = bundleOf(
                RecipesListFragment.ARG_CATEGORY_ID to categoryId,
                RecipesListFragment.ARG_CATEGORY_NAME to category.title,
                RecipesListFragment.ARG_CATEGORY_IMAGE_URL to category.imageUrl
            )
        }

        parentFragmentManager.commit {
            hide(this@CategoriesListFragment)
            add(R.id.mainContainer, recipesListFragment)
            addToBackStack(null)
        }
    }
}
