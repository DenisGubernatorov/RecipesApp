package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.FavoritesUtils
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var _rfLiveData: MutableLiveData<RecipeViewModelState> = MutableLiveData()
    val rfLiveData: LiveData<RecipeViewModelState> get() = _rfLiveData

    private val favoritesUtils = FavoritesUtils(application)

    fun loadRecipe(recipeId: Int, applicationContext: Context) {

        viewModelScope.launch {
            val result = RecipesRepository.getInstance(applicationContext).getRecipeById(recipeId)
            if (result is RepositoryResult.Error) {
                _rfLiveData.postValue(RecipeViewModelState(result = result))
            } else if (result is RepositoryResult.Success) {
                val recipe = result.data
                val recipeImageUrl = result.data.imageUrl
                val isFavorite = favoritesUtils.getFavorites().contains(recipeId)
                _rfLiveData.postValue(
                    RecipeViewModelState(
                        recipe = recipe,
                        recipeImageUrl = recipeImageUrl,
                        isFavorite = isFavorite,
                        result = result
                    )
                )
            }
        }

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

    fun onPortionCountChanged(portionCount: Int) {
        val currentState = _rfLiveData.value ?: return
        _rfLiveData.value = currentState.copy(portionCount = portionCount)
    }
}

data class RecipeViewModelState(
    val recipe: Recipe? = null,
    val recipeImageUrl: String? = null,
    val portionCount: Int = 1,
    val isFavorite: Boolean = false,
    val result: RepositoryResult<Recipe>
)
