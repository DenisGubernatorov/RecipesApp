package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _rfLiveData: MutableLiveData<RecipeViewModelState> = MutableLiveData()
    val rfLiveData: LiveData<RecipeViewModelState> get() = _rfLiveData

    private val favoriteFileKey = "favorites"
    private val idsKey = "ids"

    fun loadRecipe(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = getFavorites().contains(recipeId)
        val currentState = _rfLiveData.value ?: RecipeViewModelState()
        _rfLiveData.value = RecipeViewModelState(
            recipe = recipe,
            seekBarProgress = currentState.seekBarProgress,
            isFavorite = isFavorite
        )

        // TODO: load from network
    }

    fun getFavorites(): HashSet<Int> {

        val sharedPrefs = application.applicationContext?.getSharedPreferences(
            favoriteFileKey,
            Context.MODE_PRIVATE
        )
        val idsAsStrings = sharedPrefs?.getStringSet(idsKey, emptySet()) ?: emptySet()
        return idsAsStrings.mapNotNull { it.toIntOrNull() }.toHashSet()

    }

    private fun setFavorites(context: Context?, ids: Set<String>) {
        val sharedPrefs =
            context?.getSharedPreferences(favoriteFileKey, Context.MODE_PRIVATE) ?: return
        sharedPrefs.edit().putStringSet(idsKey, ids).apply()

    }

    private fun saveFavorites(isFavorite: Boolean, recipeId: Int) {
        val favorites = getFavorites()
        if (isFavorite) {
            favorites.add(recipeId)
        } else {
            favorites.remove(recipeId)
        }


        setFavorites(application.applicationContext, favorites.map { it.toString() }.toSet())
    }

    fun onFavoritesClicked(recipeId: Int) {
        val currentState = _rfLiveData.value ?: return
        val newIsFavorite = !currentState.isFavorite

        _rfLiveData.value = currentState.copy(isFavorite = newIsFavorite)

        saveFavorites(newIsFavorite, recipeId)
    }
}

data class RecipeViewModelState(
    val recipe: Recipe? = null,
    val seekBarProgress: Int = 1,
    val isFavorite: Boolean = false
)