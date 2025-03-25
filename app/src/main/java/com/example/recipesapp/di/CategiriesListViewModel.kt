package com.example.recipesapp.di

import com.example.recipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModel(
    private val appContainer: AppContainer
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(appContainer)
    }

}