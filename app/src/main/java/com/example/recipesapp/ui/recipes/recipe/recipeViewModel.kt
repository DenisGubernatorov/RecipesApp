package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.Ingredient

class RecipeViewModel() : ViewModel() {

}

data class RecipeViewModelState(
    val rId: Int = 0,
    val rTitle: String? = null,
    val rIngredients: List<Ingredient> = emptyList(),
    val rMethod: List<String> = emptyList(),
    val rImageUrl: String? = null
) {

}