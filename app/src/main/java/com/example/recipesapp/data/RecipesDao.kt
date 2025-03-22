package com.example.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE id >= :rangeStart AND id < :rangeEnd")
    suspend fun getRecipesList(rangeStart: Int, rangeEnd: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipe WHERE isfavorite = 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("UPDATE recipe SET isfavorite = :isFavorite WHERE id = :recipeId")
    suspend fun setIsFavorite(recipeId: Int, isFavorite: Boolean)
    @Query("DELETE FROM recipe")
    suspend fun clearRecipes()
}