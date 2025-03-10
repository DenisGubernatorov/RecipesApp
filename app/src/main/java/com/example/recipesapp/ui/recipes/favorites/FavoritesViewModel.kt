package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.FavoritesUtils
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites() {
        viewModelScope.launch {
            _ffLiveData.postValue(
                FavoritesState(
                    RecipesRepository().getRecipesByIds(
                        FavoritesUtils(
                            application
                        ).getFavorites()
                    )
                )
            )
        }

    }

    data class FavoritesState(
        val result: RepositoryResult<List<Recipe>>,
    )
}