package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.model.Category

class CategoriesViewModel : ViewModel() {
    private var _catLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val catLiveData: LiveData<CategoriesState> get() = _catLiveData


    fun loadCategories() {
        val categories = RecipesRepository().getCategories(

        )
        _catLiveData.value = CategoriesState(categories = categories)
    }

}

data class CategoriesState(
    val categories: List<Category>
)


