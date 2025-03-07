package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.FavoritesUtils
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites() {

        // val recipesByIds = STUB.getRecipesByIds(FavoritesUtils(application).getFavorites())

        RecipesRepository().getRecipes(FavoritesUtils(application).getFavorites()) { result ->
            _ffLiveData.postValue(FavoritesState(result = result))
        }

    }

    data class FavoritesState(
        val result: RepositoryResult<List<Recipe>>,


        )
}