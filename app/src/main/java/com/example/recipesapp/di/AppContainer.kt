package com.example.recipesapp.di

import android.content.Context
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.RecipesDatabase
import com.example.recipesapp.data.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(
    context: Context,
    private val baseUrl: String = "https://recipes.androidsprint.ru/api/",
    val imageUrl: String = "$baseUrl/images/"
) {


    private val contentType = "application/json".toMediaType()
    private val recipesDatabase = RecipesDatabase.getDatabase(context)
    private val categoriesDao = recipesDatabase.categoriesDao()
    private val recipesDao = recipesDatabase.recipesDao()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    val repository =
        RecipesRepository.getInstance(Dispatchers.IO, categoriesDao, recipesDao, service)

    val categoriesListViewModel = CategoriesListViewModel(this)
}