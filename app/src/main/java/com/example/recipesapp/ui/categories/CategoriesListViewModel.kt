package com.example.recipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val repository: RecipesRepository,
    @Named("imageUrl") private val imageUrl: String
) : ViewModel() {
    private var _catLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val catLiveData: LiveData<CategoriesState> get() = _catLiveData

    fun getImageUrl(): String = imageUrl
    fun loadCategories() {
        viewModelScope.launch {

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


