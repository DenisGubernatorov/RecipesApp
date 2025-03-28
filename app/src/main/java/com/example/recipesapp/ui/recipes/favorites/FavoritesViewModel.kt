package com.example.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.di.AppContainer
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(private val appContainer: AppContainer) : ViewModel() {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites() {
        viewModelScope.launch {
            _ffLiveData.postValue(
                FavoritesState(
                    appContainer.repository.getFavoriteRecipes()
                )
            )
        }
    }

    data class FavoritesState(
        val result: RepositoryResult<List<Recipe>>,
    )
}