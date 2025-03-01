package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val mainUrl = " https://recipes.androidsprint.ru/api"
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val threadPool = Executors.newFixedThreadPool(10)
    private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.i("thread", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {

            val catRequest = Request.Builder().url("$mainUrl/category").build()
            val response = client.newCall(catRequest).execute()
            Log.i("thread", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            if (!response.isSuccessful) return@Thread

            try {

                val body = response.body.use {
                    it?.string().orEmpty()
                }

                val json = Json { ignoreUnknownKeys = true }
                val categories: List<Category> = json.decodeFromString(body)

                val catIDs = categories.map { it.id }
                for (id in catIDs) {
                    val getRecipesInCategoryTask = getTaskForRecipesList(id)
                    threadPool.submit(getRecipesInCategoryTask)
                }

            } catch (e: Exception) {
                Log.e("getCategoriesTask", e.toString())
            }
        }

        thread.start()

        binding.category.setOnClickListener {
            val navController = findNavController(R.id.mainContainer)
            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(R.id.global_action_for_categories_list)
            }
        }

        binding.favorites.setOnClickListener {
            val navController = findNavController(R.id.mainContainer)
            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.global_action_for_favorites)
            }
        }
    }

    private fun getTaskForRecipesList(id: Int) = Runnable {

        val request = Request.Builder().url("$mainUrl/category/$id/recipes").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return@Runnable

        try {
            response.body.use { it?.string().orEmpty() }

        } catch (e: Exception) {
            Log.e("getRecipesTask", e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}