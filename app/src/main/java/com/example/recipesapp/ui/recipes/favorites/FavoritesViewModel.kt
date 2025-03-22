package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites(applicationContext: Context) {
        viewModelScope.launch {
            _ffLiveData.postValue(
                FavoritesState(
                    RecipesRepository.getInstance(applicationContext).getFavoriteRecipes()
                )
            )
        }
    }

    data class FavoritesState(
        val result: RepositoryResult<List<Recipe>>,
    )
}