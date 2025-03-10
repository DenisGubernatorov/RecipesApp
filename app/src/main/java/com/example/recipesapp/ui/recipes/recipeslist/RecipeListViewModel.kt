package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import java.io.IOException

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _rlfLiveData: MutableLiveData<RecipeListViewModelState> = MutableLiveData()
    val rlfLiveData: LiveData<RecipeListViewModelState> get() = _rlfLiveData

    fun loadRecipesList(categoryId: Int, categoryName: String?, categoryImageUrl: String?) {
        val categoryImage = getDrawable(categoryImageUrl, categoryName)

        RecipesRepository().getRecipesByCategoryId(categoryId) {
            _rlfLiveData.postValue(RecipeListViewModelState(it, categoryName, categoryImage))

        }

    }

    private fun getDrawable(categoryImageUrl: String?, categoryName: String?): Drawable? {

        return try {
            categoryImageUrl?.let {
                application.assets.open(it)
                    .use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
            }
        } catch (e: IOException) {
            Log.e("!!!!__", "image not found $categoryName", e)
            null
        }

    }

    data class RecipeListViewModelState(
        val result: RepositoryResult<List<Recipe>>,
        val categoryName: String?,
        val categoryImage: Drawable?

    )
}