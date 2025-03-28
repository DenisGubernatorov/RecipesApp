package com.example.recipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.di.AppContainer
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(private val appContainer: AppContainer) : ViewModel() {
    private var _catLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val catLiveData: LiveData<CategoriesState> get() = _catLiveData


    fun loadCategories() {
        viewModelScope.launch {
            val repository = appContainer.repository
            Log.d("RRD", "try get CATEGORIES from DB")
            val postVal = when (val cachedCategoriesResult = repository.getCategoriesFromCache()) {
                is RepositoryResult.Success -> {

                    cachedCategoriesResult
                }
                is RepositoryResult.Error -> {
                    when (val categoriesApiResult = repository.getCategories()) {
                        is RepositoryResult.Success -> {
                            Log.d("RRD", "try get CATEGORIES from API")
                            repository.saveCategoriesToCache(categoriesApiResult.data)
                            categoriesApiResult
                        }

                        is RepositoryResult.Error -> categoriesApiResult
                    }
                }
            }

            _catLiveData.postValue(CategoriesState(postVal))
        }
    }
}

data class CategoriesState(
    val result: RepositoryResult<List<Category>>
)


