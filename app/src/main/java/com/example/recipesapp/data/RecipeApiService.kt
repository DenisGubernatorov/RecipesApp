package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("recipe/{id}")
    fun getRecipe(@Path("id") id: String): Call<Recipe>

    @GET("recipes")
    fun getRecipes(@Query("ids") ids: String): Call<List<Recipe>>
}