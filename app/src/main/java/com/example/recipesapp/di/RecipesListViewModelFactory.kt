package com.example.recipesapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.ui.recipes.recipeslist.RecipesListViewModel

class RecipesListViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipesListViewModel(appContainer) as T
    }
}
