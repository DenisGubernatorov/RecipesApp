package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository private constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val categoriesDao: CategoriesDao,
    private val recipesDao: RecipesDao,
    private val service: RecipeApiService,

    ) {
    companion object {
        @Volatile
        private var INSTANCE: RecipesRepository? = null

        fun getInstance(
            dispatcher: CoroutineDispatcher,
            categoriesDao: CategoriesDao,
            recipesDao: RecipesDao,
            service: RecipeApiService
        ): RecipesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RecipesRepository(
                    dispatcher = dispatcher,
                    categoriesDao = categoriesDao,
                    recipesDao = recipesDao,
                    service = service
                ).also { INSTANCE = it }
            }
        }
    }

    suspend fun getCategoriesFromCache(): RepositoryResult<List<Category>> {
        return try {
            val categories = categoriesDao.getCategories()
            if (categories.isNotEmpty()) {
                Log.d(
                    "RRD",
                    "success get CATEGORIES data from DB"
                )
                RepositoryResult.Success(categories)
            } else {
                Log.d(
                    "RRD",
                    "empty data from  CATEGORIES data from DB"
                )
                getError()
            }
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed get data from  CATEGORIES data from DB"
            )
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        try {
            categoriesDao.insertCategories(categories)
            Log.d("RRD", "save categories to DB")
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed to save categories to DB"
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

    suspend fun getCachedRecipes(rangeStart: Int, rangeEnd: Int): RepositoryResult<List<Recipe>> {
        return try {
            val recipeList = recipesDao.getRecipesList(rangeStart, rangeEnd)
            if (recipeList.isNotEmpty()) {
                Log.d(
                    "RRD",
                    "success  get RECIPES data from DB"
                )
                RepositoryResult.Success(recipeList)
            } else {
                Log.d(
                    "RRD",
                    "empty data from RECIPES data from DB"
                )
                getError()
            }
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed get data from RECIPES data from DB"
            )
            RepositoryResult.Error(e)
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        try {
            recipesDao.insertRecipes(recipes)
            Log.d(
                "RRD",
                "success  save RECIPES to DB"
            )
        } catch (e: Exception) {
            Log.e(
                "RRE",
                "failed to save recipe to DB"
            )
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
            val favoriteRecipes = recipesDao.getFavoriteRecipes()
            RepositoryResult.Success(favoriteRecipes)
        } catch (e: Exception) {
            RepositoryResult.Error(e)
        }
    }

    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean) {
        try {
            recipesDao.setIsFavorite(recipeId, isFavorite)
        } catch (e: Exception) {
            Log.e("RRE", "Failed to update favorite status ${e.message}")
        }
    }

    private fun getError() = RepositoryResult.Error(Exception("Ошибка получения данных"))

}