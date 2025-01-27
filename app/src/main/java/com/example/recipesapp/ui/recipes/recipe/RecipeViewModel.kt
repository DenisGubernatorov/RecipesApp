package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel() : ViewModel() {

}

data class RecipeViewModelState(
    val recipe: Recipe? = null,
    val seekBarProgress: Int = 1,
    val isFavorite: Boolean = false
) {

}