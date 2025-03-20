package com.example.recipesapp.data

import android.content.Context
import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository private constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    context: Context
) {

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val IMAGE_URL = "$BASE_URL/images/"

        @Volatile
        private var INSTANCE: RecipesRepository? = null

        fun getInstance(context: Context): RecipesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RecipesRepository(context = context).also { INSTANCE = it }
            }
        }
    }


    private val contentType = "application/json".toMediaType()
    private val recipesDatabase = RecipesDatabase.getDatabase(context)

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    private val service = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategoriesFromCache(): RepositoryResult<List<Category>> {
        return try {
            val categories = recipesDatabase.categoriesDao().getCategories()
            if (categories.isNotEmpty()) RepositoryResult.Success(categories) else getError()
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        try {
            recipesDatabase.categoriesDao().insertCategories(categories)
        } catch (e: Exception) {
            Log.e("RRE", "Failed to save categories to DB ${e.message}")
        }
    }

    suspend fun getCategories(): RepositoryResult<List<Category>> {
        return withContext(dispatcher) {
            try {
                val response = service.getCategories().execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        RepositoryResult.Success(it)
                    } ?: getError()

                } else {
                    getError()
                }

            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch categories")
                getError()
            }
        }
    }

    suspend fun getCachedRecipes(rangeStart: Int, rangeEnd: Int): RepositoryResult<List<Recipe>> {
        return try {
            val recipeList = recipesDatabase.recipesListDao().getRecipesList(rangeStart, rangeEnd)
            if (recipeList.isNotEmpty()) RepositoryResult.Success(recipeList) else getError()
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        try {
            recipesDatabase.recipesListDao().insertRecipes(recipes)
        } catch (e: Exception) {
            Log.e("RRE", "Failed to save recipes to DB ${e.message}")
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): RepositoryResult<List<Recipe>> {
        return withContext(dispatcher) {
            try {
                val response = service.getRecipesByCategoryId(id.toString()).execute()

                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        RepositoryResult.Success(recipes)
                    } ?: getError()
                } else {
                    getError()
                }

            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe list", e)
                getError()
            }
        }
    }

    suspend fun getFavoriteRecipes(): RepositoryResult<List<Recipe>> {
        return try {
            val favoriteRecipes = recipesDatabase.recipesListDao().getFavoriteRecipes()
            RepositoryResult.Success(favoriteRecipes)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean) {
        try {
            recipesDatabase.recipesListDao().setIsFavorite(recipeId, isFavorite)
        } catch (e: Exception) {
            Log.e("RRE", "Failed to update favorite status ${e.message}")
        }
    }

    private fun getError() = RepositoryResult.Error(Exception("Ошибка получения данных"))

}