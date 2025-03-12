package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.data.RepositoryResult
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private var _catLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val catLiveData: LiveData<CategoriesState> get() = _catLiveData


    fun loadCategories() {
        viewModelScope.launch {
            _catLiveData.postValue(CategoriesState(RecipesRepository().getCategories()))
        }
    }
}

data class CategoriesState(
    val result: RepositoryResult<List<Category>>
)


