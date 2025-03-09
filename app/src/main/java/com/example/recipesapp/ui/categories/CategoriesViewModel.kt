package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Category

class CategoriesViewModel : ViewModel() {
    private var _catLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val catLiveData: LiveData<CategoriesState> get() = _catLiveData


    fun loadCategories() {
        RecipesRepository().getCategories { result ->
            _catLiveData.postValue(CategoriesState(result))
        }
    }
}

data class CategoriesState(
    val result: RepositoryResult<List<Category>>
)


