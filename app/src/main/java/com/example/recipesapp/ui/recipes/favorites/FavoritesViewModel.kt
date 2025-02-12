package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.FavoritesUtils
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import java.io.IOException

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites() {

        val recipesByIds = STUB.getRecipesByIds(FavoritesUtils(application).getFavorites())
        val favoritesImage = getDrawable()
        val emptyListVisibility = if (recipesByIds.isEmpty()) View.VISIBLE else View.GONE
        val rvFavoritesVisibility = if (recipesByIds.isEmpty()) View.GONE else View.VISIBLE

        _ffLiveData.value = FavoritesState(
            favoritesList = recipesByIds,
            favoritesImage = favoritesImage,
            emptyListVisibility = emptyListVisibility,
            rvFavoritesVisibility = rvFavoritesVisibility
        )


    }

    private fun getDrawable(): Drawable? {
        return try {
            application.assets.open("bcg_favorites.png")
                .use { inputStream ->
                    Drawable.createFromStream(inputStream, null)
                }
        } catch (e: IOException) {
            Log.e("!!!!__", "image  for favorites not found ", e)
            null
        }

    }

    data class FavoritesState(
        val favoritesList: List<Recipe>,
        val favoritesImage: Drawable?,
        val emptyListVisibility: Int,
        val rvFavoritesVisibility: Int,

        )
}