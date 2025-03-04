package com.example.recipesapp.data

import com.example.recipesapp.model.Category
import retrofit2.Call
import retrofit2.http.GET

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}