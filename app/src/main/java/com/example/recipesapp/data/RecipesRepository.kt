package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository {
    private val threadPool = Executors.newFixedThreadPool(10)
    private val contentType = "application/json".toMediaType()

    private val retrofit =
        Retrofit.Builder().baseUrl("https://recipes.androidsprint.ru/api/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    private val service = retrofit.create(RecipeApiService::class.java)

    fun getCategories(callback: (RepositoryResult<List<Category>>) -> Unit) {
        threadPool.submit {
            try {
                val response = service.getCategories().execute()
                callback(
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepositoryResult.Success(it)
                        } ?: getError()

                    } else {
                        getError()
                    }
                )
            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch categories")
                callback(getError())
            }
        }
    }

    fun getRecipe(recipeId: Int, callback: (RepositoryResult<Recipe>) -> Unit) {
        threadPool.submit {
            try {
                val response = service.getRecipe(recipeId.toString()).execute()
                callback(
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepositoryResult.Success(it)
                        } ?: getError()

                    } else {
                        getError()
                    }
                )
            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe")
                callback(getError())
            }
        }
    }

    fun getRecipes(ids: HashSet<Int>, callback: (RepositoryResult<List<Recipe>>) -> Unit) {
        threadPool.submit {

            try {
                val response = service.getRecipes(ids.joinToString(",")).execute()
                callback(
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepositoryResult.Success(it)
                        } ?: getError()

                    } else {
                        getError()
                    }
                )
            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe list")
                callback(getError())
            }
        }
    }

    fun getRecipesByCategoryId(id: Int, callback: (RepositoryResult<List<Recipe>>) -> Unit) {
        threadPool.submit {
            try {

                val response = service.getRecipesByCategoryId(id.toString()).execute()
                callback(
                    if (response.isSuccessful) {
                        response.body()?.let {
                            RepositoryResult.Success(it)
                        } ?: getError()

                    } else {
                        getError()
                    }
                )
            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe list")
                callback(getError())
            }
        }
    }

    private fun getError() = RepositoryResult.Error(Exception("Ошибка получения данных"))

}