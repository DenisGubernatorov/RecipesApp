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
import java.time.LocalDateTime

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
            if (categories.isNotEmpty()) {
                Log.e(
                    "RRE",
                    "success get CATEGORIES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time: ${LocalDateTime.now()}"
                )
                RepositoryResult.Success(categories)
            } else {
                Log.e(
                    "RRE",
                    "empty data from  CATEGORIES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
                )
                getError()
            }
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed get data from  CATEGORIES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
            )
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        try {
            recipesDatabase.categoriesDao().insertCategories(categories)
            Log.e("RRE", "save categories to DB(${recipesDatabase.hashCode()})  success")
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed to save categories to DB(${recipesDatabase.hashCode()}) ${e.message}"
            )
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

    suspend fun getRecipeById(recipeId: Int): RepositoryResult<Recipe> {
        return withContext(dispatcher) {
            try {
                val response = service.getRecipeById(recipeId.toString()).execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        RepositoryResult.Success(it)
                    } ?: getError()

                } else {
                    getError()
                }

            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe")
                getError()
            }
        }
    }

    suspend fun getCachedRecipes(rangeStart: Int, rangeEnd: Int): RepositoryResult<List<Recipe>> {
        return try {
            val recipeList = recipesDatabase.recipesListDao().getRecipesList(rangeStart, rangeEnd)
            if (recipeList.isNotEmpty()) {
                Log.e(
                    "RRE",
                    "success  get RECIPES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
                )
                RepositoryResult.Success(recipeList)
            } else {
                Log.e(
                    "RRE",
                    "empty data from RECIPES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
                )
                getError()
            }
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed get data from RECIPES data from DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
            )
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveRecipeToCache(recipes: List<Recipe>) {
        try {
            recipesDatabase.recipesListDao().insertRecipes(recipes)
            Log.e(
                "RRE",
                "success  save RECIPES to DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
            )
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed to save recipe to DB DB(${recipesDatabase.hashCode()}) _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()} ___ ${e.message}"
            )
        }
    }

    suspend fun getRecipesByIds(ids: HashSet<Int>): RepositoryResult<List<Recipe>> {
        return withContext(dispatcher) {
            try {
                if (ids.isEmpty()) {
                    return@withContext RepositoryResult.Success(emptyList<Recipe>())
                }
                val response = service.getRecipesByIds(ids.joinToString(",")).execute()

                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.e(
                            "RRE",
                            "success to load recipes from API _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
                        )
                        RepositoryResult.Success(it)
                    } ?: run {
                        Log.e(
                            "RRE",
                            "null query body with recipes from API _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()}"
                        )
                        getError()
                    }

                } else {
                    Log.e(
                        "RRE",
                        "failed load recipes from API _____ thread: ${Thread.currentThread().name} ___ time ${LocalDateTime.now()} ___ e:${response.code()} ${response.message()}"
                    )
                    getError()
                }

            } catch (e: Exception) {
                Log.e("RRE", "Failed to fetch recipe list")
                getError()
            }
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

    private fun getError() = RepositoryResult.Error(Exception("Ошибка получения данных"))

}