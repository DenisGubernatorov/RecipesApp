package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    private var _rfLiveData: MutableLiveData<RecipeViewModelState> = MutableLiveData()
    val rfLiveData: LiveData<RecipeViewModelState> get() = _rfLiveData

    init {
        Log.i("RST", "recipe model created")
        _rfLiveData.value = RecipeViewModelState(isFavorite = true)
    }

}

data class RecipeViewModelState(
    val recipe: Recipe? = null,
    val seekBarProgress: Int = 1,
    val isFavorite: Boolean = false
) {

}