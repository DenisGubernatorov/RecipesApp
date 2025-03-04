package com.example.recipesapp.data

import android.util.Log
import com.example.recipesapp.model.Category
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository {


    fun getCategories(): List<Category> {
        var result = emptyList<Category>()
        val thread = Thread {
            try {

                val contentType = "application/json".toMediaType()

                val retrofit =
                    Retrofit.Builder().baseUrl("https://recipes.androidsprint.ru/api/")
                        .addConverterFactory(Json.asConverterFactory(contentType))
                        .build()
                val service = retrofit.create(RecipeApiService::class.java)
                val categoryCall = service.getCategories()
                val categoriseResponse = categoryCall.execute()
                result = categoriseResponse.body().orEmpty()

            } catch (e: Exception) {
                Log.e("!!!", e.toString())

            }

        }
        thread.start()
        thread.join()
        return result
    }
}