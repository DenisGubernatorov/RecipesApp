package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.FavoritesUtils
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var _rfLiveData: MutableLiveData<RecipeViewModelState> = MutableLiveData()
    val rfLiveData: LiveData<RecipeViewModelState> get() = _rfLiveData


    private val favoritesUtils = FavoritesUtils(application)

    fun loadRecipe(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = favoritesUtils.getFavorites().contains(recipeId)
        val currentState = _rfLiveData.value ?: RecipeViewModelState()
        _rfLiveData.value = RecipeViewModelState(
            recipe = recipe,
            seekBarProgress = currentState.seekBarProgress,
            isFavorite = isFavorite
        )

        // TODO: load from network
    }



    private fun saveFavorites(isFavorite: Boolean, recipeId: Int) {
        val favorites = favoritesUtils.getFavorites()
        if (isFavorite) {
            favorites.add(recipeId)
        } else {
            favorites.remove(recipeId)
        }


        favoritesUtils.setFavorites(favorites.map { it.toString() }.toSet())
    }

    fun onFavoritesClicked() {
        val currentState = _rfLiveData.value ?: return
        val newIsFavorite = !currentState.isFavorite

        _rfLiveData.value = currentState.copy(isFavorite = newIsFavorite)
        val recipeId = _rfLiveData.value?.recipe?.id ?: -1

        saveFavorites(newIsFavorite, recipeId)
    }
}

data class RecipeViewModelState(
    val recipe: Recipe? = null,
    val seekBarProgress: Int = 1,
    val isFavorite: Boolean = false
)