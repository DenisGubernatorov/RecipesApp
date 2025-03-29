package com.example.recipesapp.di

import android.content.Context
import com.example.recipesapp.data.CategoriesDao
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.RecipesDao
import com.example.recipesapp.data.RecipesDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule(

) {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://recipes.androidsprint.ru/api/"

    @Provides
    @Named("imageUrl")
    fun provideImageUrl(@Named("baseUrl") baseUrl: String): String = "$baseUrl/images/"

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipesDatabase =
        RecipesDatabase.getDatabase(context)

    @Provides
    fun provideCategoriesDao(recipesDatabase: RecipesDatabase): CategoriesDao =
        recipesDatabase.categoriesDao()

    @Provides
    fun provideRecipesDao(recipesDatabase: RecipesDatabase): RecipesDao =
        recipesDatabase.recipesDao()

    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }


    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}