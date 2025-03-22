package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    private var _rlfLiveData: MutableLiveData<RecipeListViewModelState> = MutableLiveData()
    val rlfLiveData: LiveData<RecipeListViewModelState> get() = _rlfLiveData

    fun loadRecipesList(
        categoryId: Int,
        categoryName: String?,
        categoryImageUrl: String?,
        applicationContext: Context
    ) {

        viewModelScope.launch {
            val repository = RecipesRepository.getInstance(applicationContext)

            val rangeStart = categoryId * 100
            val rangeEnd = (categoryId + 1) * 100
            Log.e("RRE", "try get RECIPES from DB")
            val postVal =
                when (val cachedRecipesResult = repository.getCachedRecipes(rangeStart, rangeEnd)) {
                    is RepositoryResult.Success -> {
                        cachedRecipesResult
                    }

                    is RepositoryResult.Error -> {
                        Log.e("RRE", "try get RECIPES from API")
                        when (val recipesApiResult =

                            repository.getRecipesByCategoryId(categoryId)) {
                            is RepositoryResult.Success -> {

                                repository.saveRecipesToCache(recipesApiResult.data)
                                recipesApiResult
                            }

                            is RepositoryResult.Error -> recipesApiResult
                        }
                    }
                }

            _rlfLiveData.postValue(
                RecipeListViewModelState(
                    postVal,
                    categoryName,
                    categoryImageUrl
                )
            )
        }
    }

    data class RecipeListViewModelState(
        val result: RepositoryResult<List<Recipe>>,
        val categoryName: String?,
        val categoryImageUrl: String?,

        )
}