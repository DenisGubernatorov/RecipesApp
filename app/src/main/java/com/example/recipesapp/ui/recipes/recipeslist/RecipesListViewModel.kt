package com.example.recipesapp.ui.recipes.recipeslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlinx.coroutines.launch

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val repository: RecipesRepository,
    @Named("imageUrl") private val imageUrl: String
) : ViewModel() {
    private var _rlfLiveData: MutableLiveData<RecipeListViewModelState> = MutableLiveData()
    val rlfLiveData: LiveData<RecipeListViewModelState> get() = _rlfLiveData
    fun getUrl(): String = imageUrl
    fun loadRecipesList(
        categoryId: Int,
        categoryName: String?,
        categoryImageUrl: String?
    ) {

        viewModelScope.launch {
            val repository = repository

            val rangeStart = categoryId * 100
            val rangeEnd = (categoryId + 1) * 100
            Log.d("RRD", "try get RECIPES from DB")
            val postVal =
                when (val cachedRecipesResult = repository.getCachedRecipes(rangeStart, rangeEnd)) {
                    is RepositoryResult.Success -> {
                        cachedRecipesResult
                    }

                    is RepositoryResult.Error -> {
                        Log.d("RRD", "try get RECIPES from API")
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