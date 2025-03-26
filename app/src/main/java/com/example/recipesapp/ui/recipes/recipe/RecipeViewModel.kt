package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.di.AppContainer
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(private val appContainer: AppContainer) : ViewModel() {
    private var _rfLiveData: MutableLiveData<RecipeViewModelState> = MutableLiveData()
    val rfLiveData: LiveData<RecipeViewModelState> get() = _rfLiveData

    fun loadRecipe(recipe: Recipe?) {

        viewModelScope.launch {

            if (recipe == null) {
                _rfLiveData.postValue(
                    RecipeViewModelState(
                        result = RepositoryResult.Error(
                            Exception(
                                "Ошибка получения данных"
                            )
                        )
                    )
                )
            } else {
                val recipeImageUrl = recipe.imageUrl
                val isFavorite = recipe.isFavorite
                _rfLiveData.postValue(
                    RecipeViewModelState(
                        recipe = recipe,
                        recipeImageUrl = recipeImageUrl,
                        isFavorite = isFavorite ?: false,
                        result = RepositoryResult.Success(recipe)
                    )
                )
            }
        }

    }

    fun onFavoritesClicked() {
        val currentState = _rfLiveData.value ?: return
        val newIsFavorite = !currentState.isFavorite

        _rfLiveData.value = currentState.copy(isFavorite = newIsFavorite)
        val recipeId = _rfLiveData.value?.recipe?.id ?: -1

        viewModelScope.launch {
            appContainer.repository.setFavorite(recipeId, newIsFavorite)
        }

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
