package com.example.recipesapp.ui.recipes.recipeslist

import android.app.Application
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

    fun loadRecipesList(categoryId: Int, categoryName: String?, categoryImageUrl: String?) {

        viewModelScope.launch {
            val result = RecipesRepository().getRecipesByCategoryId(categoryId)
            _rlfLiveData.postValue(RecipeListViewModelState(result, categoryName, categoryImageUrl))
        }

    }


    data class RecipeListViewModelState(
        val result: RepositoryResult<List<Recipe>>,
        val categoryName: String?,
        val categoryImageUrl: String?,

        )
}