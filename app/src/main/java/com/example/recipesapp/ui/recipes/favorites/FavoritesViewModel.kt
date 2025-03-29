package com.example.recipesapp.ui.recipes.favorites

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
class FavoritesViewModel @Inject constructor(
    private val repository: RecipesRepository,
    @Named("imageUrl") private val imageUrl: String
) : ViewModel() {
    private var _ffLiveData: MutableLiveData<FavoritesState> = MutableLiveData()

    val ffLiveData: LiveData<FavoritesState> get() = _ffLiveData

    fun loadFavorites() {
        viewModelScope.launch {
            _ffLiveData.postValue(
                FavoritesState(
                    repository.getFavoriteRecipes()
                )
            )
        }
    }

    fun getImageUrl(): String {
        return imageUrl
    }

    data class FavoritesState(
        val result: RepositoryResult<List<Recipe>>,
    )
}